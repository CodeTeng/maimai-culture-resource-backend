package com.teng.mai.controller;

import com.teng.mai.common.BaseResponse;
import com.teng.mai.common.ErrorCode;
import com.teng.mai.common.ResultUtils;
import com.teng.mai.exception.ThrowUtils;
import com.teng.mai.model.dto.ReplyAddDTO;
import com.teng.mai.service.ReplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/3/26 11:07
 */
@RestController
@RequestMapping("/replies")
@Api(tags = "评论管理模块")
public class ReplyController {
    @Resource
    private ReplyService replyService;

    @PostMapping("/addReply")
    @ApiOperation("新增回复或评论")
    public BaseResponse<Boolean> addReply(@RequestBody @Validated ReplyAddDTO replyAddDTO) {
        ThrowUtils.throwIf(replyAddDTO == null, ErrorCode.PARAMS_ERROR);
        replyService.addReply(replyAddDTO);
        return ResultUtils.success(true);
    }
}
