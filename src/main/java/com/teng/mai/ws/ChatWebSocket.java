package com.teng.mai.ws;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.teng.mai.config.HttpSessionConfigurator;
import com.teng.mai.constant.UserConstant;
import com.teng.mai.model.dto.MessageDTO;
import com.teng.mai.model.entity.Chat;
import com.teng.mai.model.entity.User;
import com.teng.mai.model.enums.ChatTypeEnum;
import com.teng.mai.model.vo.MessageVO;
import com.teng.mai.model.vo.UserVO;
import com.teng.mai.model.vo.UserWebSocketVO;
import com.teng.mai.service.ChatService;
import com.teng.mai.service.FriendsService;
import com.teng.mai.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/3/23 23:51
 */
@Component
@Slf4j
@ServerEndpoint(value = "/ws/{userId}", configurator = HttpSessionConfigurator.class)
public class ChatWebSocket {
    /**
     * 线程安全的无序的集合
     */
    private static final Set<Session> SESSIONS = new CopyOnWriteArraySet<>();
    /**
     * 存储在线连接数
     */
    private static final Map<String, Session> SESSION_POOL = new HashMap<>(0);
    private static UserService userService;
    private static ChatService chatService;
    private static FriendsService friendsService;
    /**
     * 房间在线人数
     */
    private static int onlineCount = 0;
    /**
     * 当前信息
     */
    private Session session;
    private HttpSession httpSession;

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        ChatWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        ChatWebSocket.onlineCount--;
    }

    @Resource
    public void setHeatMapService(UserService userService) {
        ChatWebSocket.userService = userService;
    }

    @Resource
    public void setHeatMapService(ChatService chatService) {
        ChatWebSocket.chatService = chatService;
    }

    @Resource
    public void setFriendsService(FriendsService friendsService) {
        ChatWebSocket.friendsService = friendsService;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") String userId, EndpointConfig config) {
        try {
            if (StringUtils.isBlank(userId) || "undefined".equals(userId)) {
                sendError(userId, "参数有误");
                return;
            }
            HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
            UserVO user = (UserVO) httpSession.getAttribute(UserConstant.USER_LOGIN_STATE);
            if (user != null) {
                this.session = session;
                this.httpSession = httpSession;
            }
            SESSIONS.add(session);
            SESSION_POOL.put(userId, session);
            log.info("有新用户加入，userId={}, 当前在线人数为：{}", userId, SESSION_POOL.size());
            sendAllUsers();
        } catch (Exception e) {
            log.error("连接建立失败，原因：{}", e.getMessage());
        }
    }

    @OnClose
    public void onClose(@PathParam("userId") String userId, Session session) {
        try {
            if (!SESSION_POOL.isEmpty()) {
                SESSION_POOL.remove(userId);
                SESSIONS.remove(session);
            }
            log.info("【WebSocket消息】连接断开，总数为：" + SESSION_POOL.size());
            sendAllUsers();
        } catch (Exception e) {
            log.error("连接断开异常，原因：{}", e.getMessage());
        }
    }

    @OnMessage
    public void onMessage(String message, @PathParam("userId") String userId) {
        if ("PING".equals(message)) {
            sendOneMessage(userId, "pong");
            log.error("心跳包，发送给={},在线:{}人", userId, getOnlineCount());
            return;
        }
        log.info("服务端收到用户username={}的消息:{}", userId, message);
        MessageDTO messageDTO = new Gson().fromJson(message, MessageDTO.class);
        Long toId = messageDTO.getToId();
        String text = messageDTO.getText();
        ChatTypeEnum chatType = messageDTO.getChatType();
        User fromUser = userService.getById(userId);
        if (chatType == ChatTypeEnum.PRIVATE_CHAT) {
            // 私聊
            privateChat(fromUser, toId, text, chatType);
        } else {
            // 群聊
            hallChat(fromUser, text, chatType);
        }
    }

    private void hallChat(User fromUser, String text, ChatTypeEnum chatType) {

    }

    /**
     * 私人聊天
     *
     * @param user     使用者
     * @param toId     至id
     * @param text     文本
     * @param chatType 聊天类型
     */
    private void privateChat(User user, Long toId, String text, ChatTypeEnum chatType) {
        Session toSession = SESSION_POOL.get(toId.toString());
        if (toSession != null) {
            MessageVO messageVO = chatService.chatResult(user.getId(), toId, text, chatType, LocalDateTime.now());
            UserVO loginUser = (UserVO) this.httpSession.getAttribute(UserConstant.USER_LOGIN_STATE);
            if (loginUser.getId().equals(user.getId())) {
                messageVO.setIsMy(true);
            }
            String toJson = new Gson().toJson(messageVO);
            sendOneMessage(toId.toString(), toJson);
            log.info("发送给用户username={}，消息：{}", messageVO.getToUser(), toJson);
        } else {
            log.info("用户不在线username={}的session", toId);
        }
        savaChat(user.getId(), toId, text, chatType);
    }

    /**
     * 保存聊天
     *
     * @param userId   用户id
     * @param toId     至id
     * @param text     文本
     * @param chatType 聊天类型
     */
    private void savaChat(Long userId, Long toId, String text, ChatTypeEnum chatType) {
        if (chatType == ChatTypeEnum.PRIVATE_CHAT) {
            if (!friendsService.isFriends(userId, toId)) {
                sendError(String.valueOf(userId), "该用户不是你的好友");
                return;
            }
        }
        Chat chat = new Chat();
        chat.setFromId(userId);
        chat.setText(String.valueOf(text));
        chat.setChatType(chatType);
        chat.setCreateTime(LocalDateTime.now());
        if (toId != null && toId > 0) {
            chat.setToId(toId);
        }
        chatService.save(chat);
    }

    /**
     * 发送消息
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 发送失败
     *
     * @param userId       用户id
     * @param errorMessage 错误消息
     */
    private void sendError(String userId, String errorMessage) {
        JSONObject obj = new JSONObject();
        obj.set("error", errorMessage);
        sendOneMessage(userId, obj.toString());
    }

    /**
     * 此为单点消息
     *
     * @param userId  用户编号
     * @param message 消息
     */
    public void sendOneMessage(String userId, String message) {
        Session session = SESSION_POOL.get(userId);
        if (session != null && session.isOpen()) {
            try {
                synchronized (session) {
                    log.info("【WebSocket消息】单点消息：" + message);
                    session.getAsyncRemote().sendText(message);
                }
            } catch (Exception e) {
                log.error("单点消息发送失败，原因：{}", e.getMessage());
            }
        }
    }

    /**
     * 发送所有在线用户信息
     */
    public void sendAllUsers() {
        log.info("【WebSocket消息】发送所有在线用户信息");
        HashMap<String, List<UserWebSocketVO>> stringListHashMap = new HashMap<>(0);
        List<UserWebSocketVO> webSocketVos = new ArrayList<>();
        stringListHashMap.put("users", webSocketVos);
        for (Serializable key : SESSION_POOL.keySet()) {
            User user = userService.getById(key);
            UserWebSocketVO webSocketVo = new UserWebSocketVO();
            BeanUtils.copyProperties(user, webSocketVo);
            webSocketVos.add(webSocketVo);
        }
        sendAllMessage(JSONUtil.toJsonStr(stringListHashMap));
    }

    /**
     * 此为广播消息
     *
     * @param message 消息
     */
    public void sendAllMessage(String message) {
        log.info("【WebSocket消息】广播消息：" + message);
        for (Session session : SESSIONS) {
            try {
                if (session.isOpen()) {
                    synchronized (session) {
                        session.getBasicRemote().sendText(message);
                    }
                }
            } catch (Exception e) {
                log.error("广播消息发送失败，原因：{}", e.getMessage());
            }
        }
    }
}
