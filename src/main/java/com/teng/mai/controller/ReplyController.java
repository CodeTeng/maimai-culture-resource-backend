package com.teng.mai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.teng.mai.common.BaseResponse;
import com.teng.mai.common.ErrorCode;
import com.teng.mai.common.ResultUtils;
import com.teng.mai.common.page.PageVO;
import com.teng.mai.exception.ThrowUtils;
import com.teng.mai.model.dto.MyReplySearchDTO;
import com.teng.mai.model.dto.ReplyAddDTO;
import com.teng.mai.model.dto.ReplySearchDTO;
import com.teng.mai.model.entity.Reply;
import com.teng.mai.model.vo.ReplyVO;
import com.teng.mai.service.ReplyService;
import com.teng.mai.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @Resource
    private UserService userService;

    @GetMapping("/getMyReplyCount")
    @ApiOperation("查看我评论的数量")
    public BaseResponse<Long> getMyReplyCount() {
        Long userId = userService.getCurrentUser().getId();
        long count = replyService.count(new LambdaQueryWrapper<Reply>().eq(Reply::getUserId, userId));
        return ResultUtils.success(count);
    }

    @PostMapping("/addReply")
    @ApiOperation("新增回复或评论")
    public BaseResponse<Boolean> addReply(@RequestBody @Validated ReplyAddDTO replyAddDTO) {
        ThrowUtils.throwIf(replyAddDTO == null, ErrorCode.PARAMS_ERROR);
        replyService.addReply(replyAddDTO);
        return ResultUtils.success(true);
    }

    @ApiOperation("分页查询评论列表")
    @PostMapping("/pageReply")
    public BaseResponse<PageVO<ReplyVO>> queryReplyPage(@RequestBody @Validated ReplySearchDTO replySearchDTO) {
        ThrowUtils.throwIf(replySearchDTO == null, ErrorCode.PARAMS_ERROR);
        PageVO<ReplyVO> pageVO = replyService.queryReplyPage(replySearchDTO);
        return ResultUtils.success(pageVO);
    }

    @ApiOperation("分页查询我的评论列表")
    @PostMapping("/queryMyReplyPage")
    public BaseResponse<PageVO<ReplyVO>> queryMyReplyPage(@RequestBody @Validated MyReplySearchDTO myReplySearchDTO) {
        ThrowUtils.throwIf(myReplySearchDTO == null, ErrorCode.PARAMS_ERROR);
        PageVO<ReplyVO> pageVO = replyService.queryMyReplyPage(myReplySearchDTO);
        return ResultUtils.success(pageVO);
    }

    @ApiOperation("删除我的评论")
    @DeleteMapping("/deleteMyReply/{id}")
    public BaseResponse<Boolean> deleteMyReply(@PathVariable("id") Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        replyService.deleteMyReply(id);
        return ResultUtils.success(true);
    }
}
