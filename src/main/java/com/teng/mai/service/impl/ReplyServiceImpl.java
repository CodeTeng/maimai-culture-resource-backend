package com.teng.mai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.teng.mai.model.dto.ReplyAddDTO;
import com.teng.mai.model.entity.Article;
import com.teng.mai.model.entity.Reply;
import com.teng.mai.service.ArticleService;
import com.teng.mai.service.ReplyService;
import com.teng.mai.mapper.ReplyMapper;
import com.teng.mai.service.UserService;
import com.teng.mai.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author teng
 * @description 针对表【reply(回复评论表)】的数据库操作Service实现
 * @createDate 2024-03-26 11:07:05
 */
@Service
public class ReplyServiceImpl extends ServiceImpl<ReplyMapper, Reply>
        implements ReplyService {
    @Resource
    private UserService userService;
    @Resource
    private ArticleService articleService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addReply(ReplyAddDTO replyAddDTO) {
        // 1. 获取当前登录用户
        Long userId = userService.getCurrentUser().getId();
        // 2. 新增评论
        Reply reply = BeanCopyUtils.copyObject(replyAddDTO, Reply.class);
        reply.setUserId(userId);
        // 3. 判断评论数或者累加文章评论数
        boolean isAnswer = replyAddDTO.getAnswerId() == null;
        if (!isAnswer) {
            // 3.1 是评论 需要更新上级回答的评论数量
            lambdaUpdate()
                    .setSql("reply_times = reply_times + 1")
                    .eq(Reply::getId, replyAddDTO.getAnswerId())
                    .update();
        }
        // 3.2 是回复文章 更新文章的回答数量 TODO
        articleService.lambdaUpdate()
                .set(isAnswer, Article::getLatestReplyId, reply.getAnswerId())
                .setSql(isAnswer, "reply_times = reply_times + 1")
                .eq(Article::getId, replyAddDTO.getArticleId())
                .update();

    }
}




