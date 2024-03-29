package com.teng.mai.service.impl;

import cn.hutool.http.HtmlUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.teng.mai.common.ErrorCode;
import com.teng.mai.common.page.PageVO;
import com.teng.mai.exception.BusinessException;
import com.teng.mai.mapper.ArticleMapper;
import com.teng.mai.mapper.UserMapper;
import com.teng.mai.model.dto.CommentDTO;
import com.teng.mai.model.dto.CommentPageDTO;
import com.teng.mai.model.dto.CommentSearchDTO;
import com.teng.mai.model.entity.Article;
import com.teng.mai.model.entity.Comment;
import com.teng.mai.model.entity.User;
import com.teng.mai.model.enums.CommentTypeEnum;
import com.teng.mai.model.vo.CommentVO;
import com.teng.mai.model.vo.ReplyVO;
import com.teng.mai.model.vo.UserVO;
import com.teng.mai.service.CommentService;
import com.teng.mai.mapper.CommentMapper;
import com.teng.mai.service.UserService;
import com.teng.mai.utils.BeanCopyUtils;
import com.teng.mai.utils.CollUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author teng
 * @description 针对表【comment(评论表)】的数据库操作Service实现
 * @createDate 2024-03-22 23:31:27
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private UserService userService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private CommentMapper commentMapper;

    private static final List<Integer> types = new ArrayList<>();

    @PostConstruct
    public void init() {
        CommentTypeEnum[] values = CommentTypeEnum.values();
        for (CommentTypeEnum value : values) {
            types.add(value.getValue());
        }
    }

    @Override
    public void saveComments(CommentDTO commentDTO) {
        // 1. 校验参数异常
        checkCommentDTO(commentDTO);
        // 2. 获取当前登录用户
        UserVO currentUser = userService.getCurrentUser();
        Long userId = currentUser.getId();
        commentDTO.setCommentContent(HtmlUtil.filter(commentDTO.getCommentContent()));
        Comment comment = Comment.builder()
                .userId(userId).topicId(commentDTO.getTopicId())
                .commentContent(commentDTO.getCommentContent())
                .replyUserId(commentDTO.getReplyUserId())
                .parentId(commentDTO.getParentId())
                .type(commentDTO.getType())
                .build();
        // 3. 添加评论
        save(comment);
    }

    @Override
    public PageVO<CommentVO> getComments(CommentPageDTO commentPageDTO) {
        // 1. 查看是否有评论
        Page<Comment> commentPage = lambdaQuery()
                .isNull(Comment::getParentId)
                .eq(commentPageDTO.getType() != null, Comment::getType, commentPageDTO.getType())
                .eq(commentPageDTO.getTopicId() != null, Comment::getTopicId, commentPageDTO.getTopicId())
                .page(commentPageDTO.toMpPageDefaultSortByCreateTimeDesc());
        List<Comment> commentList = commentPage.getRecords();
        if (CollUtils.isEmpty(commentList)) {
            return PageVO.empty(commentPage);
        }
        // 2. 查询评论的用户
        List<Long> userIdList = commentList.stream().map(Comment::getUserId).distinct().toList();
        List<User> userList = userMapper.selectBatchIds(userIdList);
        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(User::getId, u -> u));
        // 3. 查询所有的子评论 有bug
        List<Long> commentIdList = commentList.stream().map(Comment::getId).toList();
        List<ReplyVO> replyVOS = commentMapper.listReplies(commentIdList);
        Map<Long, List<ReplyVO>> replyMap = replyVOS.stream().collect(Collectors.groupingBy(ReplyVO::getParentId));
        // 4. 封装 VO
        List<CommentVO> commentVOList = commentList.stream().map(comment -> {
            CommentVO commentVO = BeanCopyUtils.copyObject(comment, CommentVO.class);
            // 设置评论的用户
            User user = userMap.get(comment.getUserId());
            if (user != null) {
                commentVO.setUsername(user.getUsername());
                commentVO.setUserGender(user.getUserGender());
                commentVO.setUserAvatar(user.getUserAvatar());
            }
            // 设置子评论
            commentVO.setReplyVOS(replyMap.get(comment.getId()));
            return commentVO;
        }).toList();
        return PageVO.of(commentPage, commentVOList);
    }

    @Override
    public List<ReplyVO> listRepliesByCommentId(Long commentId) {
        return commentMapper.listReplies(Collections.singletonList(commentId));
    }

    @Override
    public PageVO<CommentVO> getMyComments(CommentSearchDTO commentSearchDTO) {
        return null;
    }

    private void checkCommentDTO(CommentDTO commentDTO) {
        int value = commentDTO.getType().getValue();
        if (!types.contains(value)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        CommentTypeEnum commentTypeEnum = CommentTypeEnum.of(value);
        if (commentTypeEnum == CommentTypeEnum.ARTICLE) {
            // 文章类型评论的主题不能为空
            if (commentDTO.getTopicId() == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            // 查询评论的文章是否存在
            Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                    .select(Article::getId, Article::getUserId).eq(Article::getId, commentDTO.getTopicId()));
            if (article == null) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "文章不存在");
            }
        }
        // 有回复的人 校验参数
        Long parentId = commentDTO.getParentId();
        Long replyUserId = commentDTO.getReplyUserId();
        if (parentId == null) {
            if (replyUserId != null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (parentId != null) {
            // 父级id不为空
            Comment parentComment = getOne(new LambdaQueryWrapper<Comment>()
                    .select(Comment::getId, Comment::getParentId, Comment::getType).eq(Comment::getId, parentId));
            if (parentComment == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "上级评论不存在");
            }
            if (commentDTO.getType() != parentComment.getType()) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            if (replyUserId == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            } else {
                User user = userService.getOne(new LambdaQueryWrapper<User>().select(User::getId).eq(User::getId, replyUserId));
                if (user == null) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "回复用户不存在");
                }
            }
        }
    }
}




