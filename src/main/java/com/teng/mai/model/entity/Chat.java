package com.teng.mai.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.teng.mai.model.enums.ChatTypeEnum;
import lombok.Data;

/**
 * 聊天消息表
 * @TableName chat
 */
@TableName(value ="chat")
@Data
public class Chat implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 发送消息人
     */
    private Long fromId;

    /**
     * 接收消息人
     */
    private Long toId;

    /**
     * 聊天记录
     */
    private String text;

    /**
     * 聊天类型 1-私聊 2-大厅聊天
     */
    private ChatTypeEnum chatType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}