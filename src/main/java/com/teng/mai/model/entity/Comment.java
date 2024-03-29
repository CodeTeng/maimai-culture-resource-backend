package com.teng.mai.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.teng.mai.model.enums.CommentTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评论表
 *
 * @TableName comment
 */
@TableName(value = "comment")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 评论用户Id
     */
    private Long userId;

    /**
     * 评论主题id
     */
    private Long topicId;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 回复用户id
     */
    private Long replyUserId;

    /**
     * 父评论id
     */
    private Long parentId;

    /**
     * 评论类型 1.文章 2.留言 ... 可扩展
     */
    private CommentTypeEnum type;

    /**
     * 评论时间
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