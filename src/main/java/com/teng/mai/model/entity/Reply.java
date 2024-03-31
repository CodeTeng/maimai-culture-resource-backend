package com.teng.mai.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 回复评论表
 *
 * @TableName reply
 */
@TableName(value = "reply")
@Data
public class Reply implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 回复的上级评论id
     */
    private Long answerId;

    /**
     * 回答者id
     */
    private Long userId;

    /**
     * 回答内容
     */
    private String content;

    /**
     * 回复的目标用户id
     */
    private Long targetUserId;

    /**
     * 回复的目标回复id
     */
    private Long targetReplyId;

    /**
     * 评论数量
     */
    private Integer replyTimes;

    /**
     * 是否被隐藏，默认false
     */
    private Boolean hidden;

    /**
     * 创建时间
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