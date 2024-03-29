package com.teng.mai.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.teng.mai.common.ErrorCode;
import com.teng.mai.exception.BusinessException;
import com.teng.mai.mapper.FriendsMapper;
import com.teng.mai.mapper.UserMapper;
import com.teng.mai.model.dto.FriendsChatDTO;
import com.teng.mai.model.entity.Chat;
import com.teng.mai.model.entity.Friends;
import com.teng.mai.model.entity.User;
import com.teng.mai.model.enums.ChatTypeEnum;
import com.teng.mai.model.vo.MessageVO;
import com.teng.mai.model.vo.UserVO;
import com.teng.mai.model.vo.UserWebSocketVO;
import com.teng.mai.service.ChatService;
import com.teng.mai.mapper.ChatMapper;
import com.teng.mai.service.FriendsService;
import com.teng.mai.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author teng
 * @description 针对表【chat(聊天消息表)】的数据库操作Service实现
 * @createDate 2024-03-23 23:44:00
 */
@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat>
        implements ChatService {
    @Resource
    private FriendsService friendsService;
    @Resource
    private UserMapper userMapper;

    @Override
    public List<MessageVO> getPrivateChat(FriendsChatDTO friendsChatDTO, ChatTypeEnum chatType, Long userId) {
        // 1. 判断是否是好友
        Long toId = friendsChatDTO.getToId();
        if (userId.equals(toId)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "自己无法和自己聊天");
        }
//        if (!friendsService.isFriends(userId, toId)) {
//            throw new BusinessException(ErrorCode.OPERATION_ERROR, "双方还未加好友，无法私聊");
//        }
        // 2. 查询聊天记录
        LambdaQueryWrapper<Chat> chatLambdaQueryWrapper = new LambdaQueryWrapper<>();
        chatLambdaQueryWrapper.
                and(privateChat -> privateChat.eq(Chat::getFromId, userId).eq(Chat::getToId, toId)
                        .or().eq(Chat::getToId, userId).eq(Chat::getFromId, toId)
                ).eq(Chat::getChatType, chatType);
        // 3. 两方共有聊天
        List<Chat> chatList = this.list(chatLambdaQueryWrapper);
        return chatList.stream().map(chat -> {
            MessageVO messageVo = chatResult(userId, toId, chat.getText(), chatType, chat.getCreateTime());
            if (chat.getFromId().equals(userId)) {
                messageVo.setIsMy(true);
            }
            return messageVo;
        }).toList();
    }

    @Override
    public MessageVO chatResult(Long userId, Long toId, String text, ChatTypeEnum chatType, LocalDateTime createTime) {
        MessageVO messageVo = new MessageVO();
        User fromUser = userMapper.selectById(userId);
        User toUser = userMapper.selectById(toId);
        UserWebSocketVO fromWebSocketVo = new UserWebSocketVO();
        UserWebSocketVO toWebSocketVo = new UserWebSocketVO();
        BeanUtils.copyProperties(fromUser, fromWebSocketVo);
        BeanUtils.copyProperties(toUser, toWebSocketVo);
        messageVo.setFromUser(fromWebSocketVo);
        messageVo.setToUser(toWebSocketVo);
        messageVo.setChatType(chatType);
        messageVo.setText(text);
        messageVo.setCreateTime(DateUtil.format(createTime, "yyyy年MM月dd日 HH:mm:ss"));
        return messageVo;
    }
}




