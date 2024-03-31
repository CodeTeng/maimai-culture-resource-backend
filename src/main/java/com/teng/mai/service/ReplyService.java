package com.teng.mai.service;

import com.teng.mai.common.page.PageVO;
import com.teng.mai.model.dto.MyReplySearchDTO;
import com.teng.mai.model.dto.ReplyAddDTO;
import com.teng.mai.model.dto.ReplySearchDTO;
import com.teng.mai.model.entity.Reply;
import com.baomidou.mybatisplus.extension.service.IService;
import com.teng.mai.model.vo.ReplyVO;

/**
 * @author teng
 * @description 针对表【reply(回复评论表)】的数据库操作Service
 * @createDate 2024-03-26 11:07:05
 */
public interface ReplyService extends IService<Reply> {

    /**
     * 新增回复或评论
     */
    void addReply(ReplyAddDTO replyAddDTO);

    /**
     * 分页查询评论列表
     */
    PageVO<ReplyVO> queryReplyPage(ReplySearchDTO replySearchDTO);

    /**
     * 分页查询我的评论列表
     */
    PageVO<ReplyVO> queryMyReplyPage(MyReplySearchDTO myReplySearchDTO);

    /**
     * 删除我的评论
     */
    void deleteMyReply(Long id);
}
