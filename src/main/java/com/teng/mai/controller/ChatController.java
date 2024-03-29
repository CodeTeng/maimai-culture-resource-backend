package com.teng.mai.controller;

import com.teng.mai.common.BaseResponse;
import com.teng.mai.common.ErrorCode;
import com.teng.mai.common.ResultUtils;
import com.teng.mai.exception.BusinessException;
import com.teng.mai.exception.ThrowUtils;
import com.teng.mai.model.dto.FriendsChatDTO;
import com.teng.mai.model.enums.ChatTypeEnum;
import com.teng.mai.model.vo.MessageVO;
import com.teng.mai.service.ChatService;
import com.teng.mai.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/3/23 23:47
 */
@RestController
@RequestMapping("/chats")
@Api(tags = "聊天管理模块")
public class ChatController {
    @Resource
    private ChatService chatService;
    @Resource
    private UserService userService;

    @PostMapping("/privateChat")
    @ApiOperation("获取好友私聊聊天内容")
    public BaseResponse<List<MessageVO>> getPrivateChat(@RequestBody @Validated FriendsChatDTO friendsChatDTO) {
        ThrowUtils.throwIf(friendsChatDTO == null, ErrorCode.PARAMS_ERROR);
        Long userId = userService.getCurrentUser().getId();
        List<MessageVO> privateChat = chatService.getPrivateChat(friendsChatDTO, ChatTypeEnum.PRIVATE_CHAT, userId);
        return ResultUtils.success(privateChat);
    }
}
