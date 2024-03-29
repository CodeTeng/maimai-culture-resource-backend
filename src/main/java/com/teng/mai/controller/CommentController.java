package com.teng.mai.controller;

import com.teng.mai.common.BaseResponse;
import com.teng.mai.common.ErrorCode;
import com.teng.mai.common.ResultUtils;
import com.teng.mai.common.page.PageVO;
import com.teng.mai.exception.ThrowUtils;
import com.teng.mai.model.dto.CommentDTO;
import com.teng.mai.model.dto.CommentPageDTO;
import com.teng.mai.model.dto.CommentSearchDTO;
import com.teng.mai.model.vo.CommentVO;
import com.teng.mai.model.vo.ReplyVO;
import com.teng.mai.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/3/23 14:29
 */
@RestController
@RequestMapping("/comments")
@Api(tags = "评论管理模块")
public class CommentController {
    @Resource
    private CommentService commentService;

    @PostMapping("/save")
    @ApiOperation("添加评论")
    public BaseResponse<Boolean> saveComments(@RequestBody @Validated CommentDTO commentDTO) {
        ThrowUtils.throwIf(commentDTO == null, ErrorCode.PARAMS_ERROR);
        commentService.saveComments(commentDTO);
        return ResultUtils.success(true);
    }

    @GetMapping("/pageList")
    @ApiOperation("获取评论")
    public BaseResponse<PageVO<CommentVO>> getComments(CommentPageDTO commentPageDTO) {
        ThrowUtils.throwIf(commentPageDTO == null, ErrorCode.PARAMS_ERROR);
        PageVO<CommentVO> pageVO = commentService.getComments(commentPageDTO);
        return ResultUtils.success(pageVO);
    }

    @ApiOperation("根据评论id获取回复")
    @GetMapping("/{commentId}/replies")
    public BaseResponse<List<ReplyVO>> listRepliesByCommentId(@PathVariable("commentId") Long commentId) {
        ThrowUtils.throwIf(commentId == null || commentId <= 0, ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(commentService.listRepliesByCommentId(commentId));
    }

    @PostMapping("/getMyComments")
    @ApiOperation("分页获取我发的评论")
    public BaseResponse<PageVO<CommentVO>> getMyComments(@RequestBody @Validated CommentSearchDTO commentSearchDTO) {
        ThrowUtils.throwIf(commentSearchDTO == null, ErrorCode.PARAMS_ERROR);
        PageVO<CommentVO> pageVO = commentService.getMyComments(commentSearchDTO);
        return ResultUtils.success(pageVO);
    }
}
