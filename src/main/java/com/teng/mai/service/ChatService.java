package com.teng.mai.service;

import com.teng.mai.model.dto.FriendsChatDTO;
import com.teng.mai.model.entity.Chat;
import com.baomidou.mybatisplus.extension.service.IService;
import com.teng.mai.model.enums.ChatTypeEnum;
import com.teng.mai.model.vo.MessageVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author teng
 * @description 针对表【chat(聊天消息表)】的数据库操作Service
 * @createDate 2024-03-23 23:44:00
 */
public interface ChatService extends IService<Chat> {
    /**
     * 获取私聊聊天内容
     */
    List<MessageVO> getPrivateChat(FriendsChatDTO friendsChatDTO, ChatTypeEnum chatType, Long userId);

    /**
     * 聊天记录映射
     */
    MessageVO chatResult(Long userId, Long toId, String text, ChatTypeEnum chatType, LocalDateTime createTime);
}
