package com.teng.mai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.teng.mai.model.entity.ArticleTag;
import com.teng.mai.service.ArticleTagService;
import com.teng.mai.mapper.ArticleTagMapper;
import org.springframework.stereotype.Service;

/**
* @author teng
* @description 针对表【article_tag(文章标签中间表)】的数据库操作Service实现
* @createDate 2024-03-28 15:41:07
*/
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag>
    implements ArticleTagService{

}




