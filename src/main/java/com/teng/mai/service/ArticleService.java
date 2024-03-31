package com.teng.mai.service;

import com.teng.mai.common.page.PageVO;
import com.teng.mai.model.dto.ArticleSearchDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.teng.mai.model.entity.Article;
import com.teng.mai.model.vo.ArticleVO;

import java.util.List;

/**
 * @author teng
 * @description 针对表【article(文化资源表)】的数据库操作Service
 * @createDate 2024-03-22 23:31:27
 */
public interface ArticleService extends IService<Article> {

    /**
     * 分页搜索文章
     */
    List<ArticleVO> searchArticle(ArticleSearchDTO articleSearchDTO);

    /**
     * 浏览文章
     */
    ArticleVO getArticleDetail(Long id);

    /**
     * 操作或取消收藏文章
     */
    void favOrCancelFav(Long id, Long type);

    /**
     * 查看我收藏的文章
     */
    List<ArticleVO> getMyFavArticle(ArticleSearchDTO articleSearchDTO);
}
