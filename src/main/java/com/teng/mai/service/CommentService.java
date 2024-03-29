package com.teng.mai.service;

import com.teng.mai.common.page.PageVO;
import com.teng.mai.model.dto.CommentDTO;
import com.teng.mai.model.dto.CommentPageDTO;
import com.teng.mai.model.dto.CommentSearchDTO;
import com.teng.mai.model.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.teng.mai.model.vo.CommentVO;
import com.teng.mai.model.vo.ReplyVO;

import java.util.List;

/**
 * @author teng
 * @description 针对表【comment(评论表)】的数据库操作Service
 * @createDate 2024-03-22 23:31:27
 */
public interface CommentService extends IService<Comment> {

    /**
     * 添加评论
     */
    void saveComments(CommentDTO commentDTO);

    /**
     * 获取评论
     */
    PageVO<CommentVO> getComments(CommentPageDTO commentPageDTO);

    /**
     * 根据commentId获取回复
     */
    List<ReplyVO> listRepliesByCommentId(Long commentId);

    /**
     * 分页获取我发的评论
     */
    PageVO<CommentVO> getMyComments(CommentSearchDTO commentSearchDTO);
}
