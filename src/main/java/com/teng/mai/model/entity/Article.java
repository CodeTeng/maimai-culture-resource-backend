package com.teng.mai.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.teng.mai.model.enums.ArticleTypeEnum;
import lombok.Data;

/**
 * 文化资源表
 * @TableName article
 */
@TableName(value ="article")
@Data
public class Article implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 作者id
     */
    private Long userId;

    /**
     * 内容
     */
    private String articleContent;

    /**
     * 封面图
     */
    private String articleCover;

    /**
     * 标题
     */
    private String articleTitle;

    /**
     * 文章摘要，如果该字段为空，默认取文章的前500个字符作为摘要
     */
    private String articleAbstract;

    /**
     * 最新的评论id
     */
    private Long latestReplyId;

    /**
     * 评论数量
     */
    private Integer replyTimes;

    /**
     * 收藏数量
     */
    private Integer favTimes;

    /**
     * 类型 1-文章 2-视频
     */
    private ArticleTypeEnum type;

    /**
     * 视频地址
     */
    private String url;

    /**
     * 发表时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}