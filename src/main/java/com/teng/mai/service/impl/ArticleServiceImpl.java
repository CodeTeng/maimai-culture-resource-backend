package com.teng.mai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.teng.mai.common.ErrorCode;
import com.teng.mai.common.page.PageVO;
import com.teng.mai.exception.BusinessException;
import com.teng.mai.mapper.ArticleFavMapper;
import com.teng.mai.mapper.ArticleMapper;
import com.teng.mai.mapper.UserMapper;
import com.teng.mai.model.dto.ArticleSearchDTO;
import com.teng.mai.model.entity.Article;
import com.teng.mai.model.entity.ArticleFav;
import com.teng.mai.model.entity.ArticleTag;
import com.teng.mai.model.entity.User;
import com.teng.mai.model.vo.ArticleVO;
import com.teng.mai.model.vo.UserVO;
import com.teng.mai.service.ArticleService;
import com.teng.mai.service.ArticleTagService;
import com.teng.mai.service.UserService;
import com.teng.mai.utils.BeanCopyUtils;
import com.teng.mai.utils.CollUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author teng
 * @description 针对表【article(文化资源表)】的数据库操作Service实现
 * @createDate 2024-03-22 23:31:27
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
        implements ArticleService {
    @Resource
    private UserService userService;
    @Resource
    private ArticleFavMapper articleFavMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ArticleTagService articleTagService;

    @Override
    public PageVO<ArticleVO> searchArticle(ArticleSearchDTO articleSearchDTO) {
        Long userId = userService.getCurrentUser().getId();
        // 1. 查看标签下的所有文档id
        Long tagId = articleSearchDTO.getTagId();
        List<ArticleTag> articleTagList = articleTagService.list(new LambdaQueryWrapper<ArticleTag>().eq(tagId != null, ArticleTag::getTagId, tagId));
        if (CollUtils.isEmpty(articleTagList)) {
            return PageVO.empty(0L, 0L);
        }
        List<Long> articleIdList = articleTagList.stream().map(ArticleTag::getArticleId).toList();
        // 2. 根据条件分页搜索
        String keyword = articleSearchDTO.getKeyword();
        Page<Article> articlePage = lambdaQuery()
                .in(Article::getId, articleIdList)
                .like(StringUtils.isNotBlank(keyword), Article::getArticleTitle, keyword)
                .page(articleSearchDTO.toMpPageDefaultSortByCreateTimeDesc());
        List<Article> articleList = articlePage.getRecords();
        if (CollUtils.isEmpty(articleList)) {
            return PageVO.empty(articlePage);
        }
        // 3. 转 VO
        List<Long> userIdList = articleList.stream().map(Article::getUserId).toList();
        List<User> userList = userMapper.selectBatchIds(userIdList);
        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(User::getId, u -> u));
        List<ArticleVO> articleVOList = articleList.stream().map(article -> {
            ArticleVO articleVO = BeanCopyUtils.copyObject(article, ArticleVO.class);
            // 判断是否被自己收藏
            Long count = articleFavMapper.selectCount(new LambdaQueryWrapper<ArticleFav>()
                    .eq(ArticleFav::getUserId, userId).eq(ArticleFav::getArticleId, article.getId()));
            articleVO.setFavStatus(count > 0);
            User user = userMap.get(article.getUserId());
            if (user != null) {
                articleVO.setUsername(user.getUsername());
                articleVO.setUserAvatar(user.getUserAvatar());
                articleVO.setUserGender(user.getUserGender());
            }
            return articleVO;
        }).toList();
        return PageVO.of(articlePage, articleVOList);
    }

    @Override
    public ArticleVO getArticleDetail(Long id) {
        Article article = getById(id);
        if (article == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "该文章不存在");
        }
        Long userId = article.getUserId();
        User user = userService.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "文章作者不存在");
        }
        ArticleVO articleVO = BeanCopyUtils.copyObject(article, ArticleVO.class);
        // 判断是否被自己收藏
        Long count = articleFavMapper.selectCount(new LambdaQueryWrapper<ArticleFav>()
                .eq(ArticleFav::getUserId, userId).eq(ArticleFav::getArticleId, article.getId()));
        articleVO.setFavStatus(count > 0);
        articleVO.setUsername(user.getUsername());
        articleVO.setUserGender(user.getUserGender());
        articleVO.setUserAvatar(user.getUserAvatar());
        return articleVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void favOrCancelFav(Long id, Long type) {
        // 1. 获取当前登录用户
        UserVO currentUser = userService.getCurrentUser();
        Long userId = currentUser.getId();
        // 2. 判断是否已经收藏或没有收藏过
        Long count = articleFavMapper.selectCount(new LambdaQueryWrapper<ArticleFav>()
                .eq(ArticleFav::getUserId, userId)
                .eq(ArticleFav::getArticleId, id));
        if (type.equals(0L)) {
            if (count > 0) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "你已收藏，不可再次收藏");
            }
            // 3. 进行收藏
            ArticleFav articleFav = new ArticleFav();
            articleFav.setUserId(userId);
            articleFav.setArticleId(id);
            articleFavMapper.insert(articleFav);
            // 4. 增加收藏数量
            lambdaUpdate()
                    .setSql("fav_times = fav_times + 1")
                    .eq(Article::getId, id)
                    .update();
        } else if (type.equals(1L)) {
            if (count <= 0) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "你还未收藏，请先收藏");
            }
            // 3. 取消收藏
            articleFavMapper.delete(new LambdaQueryWrapper<ArticleFav>()
                    .eq(ArticleFav::getUserId, userId)
                    .eq(ArticleFav::getArticleId, id));
            // 4. 减少收藏数量
            lambdaUpdate()
                    .setSql("fav_times = fav_times - 1")
                    .eq(Article::getId, id)
                    .update();
        } else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
    }

    @Override
    public PageVO<ArticleVO> getMyFavArticle(ArticleSearchDTO articleSearchDTO) {
        // 1. 查询我收藏的文章的id
        UserVO currentUser = userService.getCurrentUser();
        Long userId = currentUser.getId();
        List<ArticleFav> articleFavList = articleFavMapper.selectList(new LambdaQueryWrapper<ArticleFav>()
                .eq(ArticleFav::getUserId, userId));
        if (CollUtils.isEmpty(articleFavList)) {
            return PageVO.empty(new Page<>());
        }
        List<Long> articleIdList = articleFavList.stream().map(ArticleFav::getArticleId).toList();
        // 2. 根据关键词查询文章
        String keyword = articleSearchDTO.getKeyword();
        Page<Article> articlePage = lambdaQuery()
                .in(Article::getId, articleIdList)
                .like(StringUtils.isNotBlank(keyword), Article::getArticleTitle, keyword)
                .page(articleSearchDTO.toMpPageDefaultSortByCreateTimeDesc());
        List<Article> articleList = articlePage.getRecords();
        if (CollUtils.isEmpty(articleList)) {
            return PageVO.empty(articlePage);
        }
        List<Long> userIdList = articleList.stream().map(Article::getUserId).toList();
        List<User> userList = userMapper.selectBatchIds(userIdList);
        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(User::getId, u -> u));
        List<ArticleVO> articleVOList = articleList.stream().map(article -> {
            ArticleVO articleVO = BeanCopyUtils.copyObject(article, ArticleVO.class);
            articleVO.setFavStatus(true);
            User user = userMap.get(article.getUserId());
            if (user != null) {
                articleVO.setUsername(user.getUsername());
                articleVO.setUserAvatar(user.getUserAvatar());
                articleVO.setUserGender(user.getUserGender());
            }
            return articleVO;
        }).toList();
        return PageVO.of(articlePage, articleVOList);
    }
}




