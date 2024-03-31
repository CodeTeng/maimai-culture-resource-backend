package com.teng.mai.mapper;

import com.teng.mai.model.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author teng
* @description 针对表【article(文化资源表)】的数据库操作Mapper
* @createDate 2024-03-28 15:29:34
* @Entity com.teng.mai.model.entity.Article
*/
public interface ArticleMapper extends BaseMapper<Article> {

    List<Long> getMyFavArticle(@Param("userId") Long userId, @Param("tagId") Long tagId);
}




