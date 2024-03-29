package com.teng.mai.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.teng.mai.model.enums.FriendsStatusEnum;
import lombok.Data;

/**
 * 好友申请管理表
 * @TableName friends
 */
@TableName(value ="friends")
@Data
public class Friends implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 发送申请的用户id
     */
    private Long fromId;

    /**
     * 接收申请的用户id 
     */
    private Long receiveId;

    /**
     * 是否已读(0-未读 1-已读)
     */
    private Integer isRead;

    /**
     * 申请状态 默认0 （0-未通过 1-已同意 2-已过期 3-已撤销）
     */
    private FriendsStatusEnum status;

    /**
     * 好友申请备注信息
     */
    private String remark;

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