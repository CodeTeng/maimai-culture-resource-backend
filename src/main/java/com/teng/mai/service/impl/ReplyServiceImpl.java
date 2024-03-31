package com.teng.mai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.teng.mai.common.ErrorCode;
import com.teng.mai.common.page.PageVO;
import com.teng.mai.exception.BusinessException;
import com.teng.mai.mapper.UserMapper;
import com.teng.mai.model.dto.MyReplySearchDTO;
import com.teng.mai.model.dto.ReplyAddDTO;
import com.teng.mai.model.dto.ReplySearchDTO;
import com.teng.mai.model.entity.Article;
import com.teng.mai.model.entity.Reply;
import com.teng.mai.model.entity.User;
import com.teng.mai.model.vo.ReplyVO;
import com.teng.mai.service.ArticleService;
import com.teng.mai.service.ReplyService;
import com.teng.mai.mapper.ReplyMapper;
import com.teng.mai.service.UserService;
import com.teng.mai.utils.BeanCopyUtils;
import com.teng.mai.utils.CollUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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
    @Resource
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addReply(ReplyAddDTO replyAddDTO) {
        // 1. 获取当前登录用户
        Long userId = userService.getCurrentUser().getId();
        // 2. 新增评论
        Reply reply = BeanCopyUtils.copyObject(replyAddDTO, Reply.class);
        reply.setUserId(userId);
        save(reply);
        // 3. 判断评论数或者累加文章评论数
        boolean isAnswer = replyAddDTO.getAnswerId() == null;
        if (!isAnswer) {
            // 3.1 是评论 需要更新上级回答的评论数量
            lambdaUpdate()
                    .setSql("reply_times = reply_times + 1")
                    .eq(Reply::getId, replyAddDTO.getAnswerId())
                    .update();
        }
        // 3.2 是回复文章 更新文章的回答数量
        articleService.lambdaUpdate()
                .set(isAnswer, Article::getLatestAnswerId, reply.getId())
                .setSql(isAnswer, "reply_times = reply_times + 1")
                .setSql("reply_times = reply_times")
                .eq(Article::getId, replyAddDTO.getArticleId())
                .update();
    }

    @Override
    public PageVO<ReplyVO> queryReplyPage(ReplySearchDTO replySearchDTO) {
        Long articleId = replySearchDTO.getArticleId();
        Long answerId = replySearchDTO.getAnswerId();
        boolean isQueryAnswer = articleId != null;
        // 1. 根据文章id或者回答id查询列表
        Page<Reply> page = lambdaQuery()
                .eq(isQueryAnswer, Reply::getArticleId, articleId)
                .eq(Reply::getAnswerId, isQueryAnswer ? 0L : answerId)
                // 根据创建时间排序
                .page(replySearchDTO.toMpPageDefaultSortByCreateTimeDesc());
        List<Reply> replyList = page.getRecords();
        if (CollUtils.isEmpty(replyList)) {
            return PageVO.empty(page);
        }
        // 2. 数据整理 需要查询：回复文章的人信息、回复目标信息、当前用户是否已经点赞
        List<ReplyVO> replyVOList = replyList2ReplyVOList(replyList);
        return PageVO.of(page, replyVOList);
    }

    @Override
    public PageVO<ReplyVO> queryMyReplyPage(MyReplySearchDTO myReplySearchDTO) {
        // 1. 查询我的评论
        Long userId = userService.getCurrentUser().getId();
        String keyword = myReplySearchDTO.getKeyword();
        Page<Reply> replyPage = lambdaQuery()
                .eq(Reply::getUserId, userId)
                .like(StringUtils.isNotBlank(keyword), Reply::getContent, keyword)
                .page(myReplySearchDTO.toMpPageDefaultSortByCreateTimeDesc());
        List<Reply> replyList = replyPage.getRecords();
        if (CollUtils.isEmpty(replyList)) {
            return PageVO.empty(replyPage);
        }
        // 2. 转 VO
        List<ReplyVO> replyVOList = replyList2ReplyVOList(replyList);
        return PageVO.of(replyPage, replyVOList);
    }

    private List<ReplyVO> replyList2ReplyVOList(List<Reply> replyList) {
        Set<Long> userIds = replyList.stream().map(Reply::getUserId).collect(Collectors.toSet());
        Set<Long> answerIds = replyList.stream().map(Reply::getAnswerId).collect(Collectors.toSet());
        Set<Long> targetReplyIds = replyList.stream()
                .filter(r -> r.getTargetReplyId() != null && !r.getTargetReplyId().equals(0L))
                .map(Reply::getTargetReplyId).collect(Collectors.toSet());
        // 2.1 查询目标回复人信息
        if (CollUtils.isNotEmpty(targetReplyIds)) {
            List<Reply> targetReplyList = listByIds(targetReplyIds);
            Set<Long> targetUserIds = targetReplyList.stream().map(Reply::getUserId).collect(Collectors.toSet());
            userIds.addAll(targetUserIds);
        }
        // 2.2 查询用户
        Map<Long, User> userMap = new HashMap<>(userIds.size());
        if (CollUtils.isNotEmpty(userIds)) {
            List<User> userList = userMapper.selectBatchIds(userIds);
            userMap = userList.stream().collect(Collectors.toMap(User::getId, u -> u));
        }
        // 3. 转 VO
        List<ReplyVO> replyVOList = new ArrayList<>(replyList.size());
        for (Reply reply : replyList) {
            ReplyVO replyVO = BeanCopyUtils.copyObject(reply, ReplyVO.class);
            replyVO.setCommentContent(reply.getContent());
            // 回复人信息
            User user = userMap.get(reply.getUserId());
            if (user != null) {
                replyVO.setUsername(user.getUsername());
                replyVO.setUserGender(user.getUserGender());
                replyVO.setUserAvatar(user.getUserAvatar());
            }
            // 如果存在评论的目标，需要设置目标用户的信息
            if (reply.getTargetReplyId() != null) {
                User replyUser = userMap.get(reply.getTargetUserId());
                if (replyUser != null) {
                    replyVO.setTargetUserId(replyUser.getId());
                    replyVO.setTargetUsername(replyUser.getUsername());
                }
            }
            replyVOList.add(replyVO);
        }
        return replyVOList;
    }

    @Override
    @Transactional
    public void deleteMyReply(Long id) {
        // 1. 查询评论是否存在
        Reply reply = getById(id);
        if (reply == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该评论已不存在");
        }
        // 2. 判断是否是本人发布的评论
        Long userId = userService.getCurrentUser().getId();
        if (!userId.equals(reply.getUserId())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "不是本人发布，不能删除该评论");
        }
        // 3. 删除评论
        removeById(id);
        // 4. 删除下面的子评论
        remove(new LambdaQueryWrapper<Reply>().eq(Reply::getAnswerId, id));
        // 5. 文章的评论数量减一
        articleService.lambdaUpdate()
                .setSql("reply_times = reply_times - 1")
                .eq(Article::getId, reply.getArticleId()).update();
    }
}




