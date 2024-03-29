package com.teng.mai.service;

import com.teng.mai.model.dto.ReplyAddDTO;
import com.teng.mai.model.entity.Reply;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
