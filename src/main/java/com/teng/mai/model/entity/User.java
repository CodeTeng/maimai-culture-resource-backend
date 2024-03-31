package com.teng.mai.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.teng.mai.model.enums.UserGenderEnum;
import com.teng.mai.model.enums.UserStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户角色 user/admin/ban
     */
    private String userRole;

    /**
     * 用户手机号
     */
    private String userPhone;

    /**
     * 用户真实姓名
     */
    private String userRealName;

    /**
     * 性别：0-男性，1-女性
     */
    private UserGenderEnum userGender;

    /**
     * 用户年龄
     */
    private Integer userAge;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户生日
     */
    private LocalDate userBirthday;

    /**
     * 用户简介
     */
    private String userProfile;

    @ApiModelProperty("用户选择的标签json列表")
    private String tags;

    /**
     * 账号状态：0-禁用 1-正常
     */
    private UserStatusEnum userStatus;

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