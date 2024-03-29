package com.teng.mai.model.vo;

import com.teng.mai.model.enums.UserGenderEnum;
import com.teng.mai.model.enums.UserStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/3/22 22:09
 */
@Data
@ApiModel("用户查询脱敏视图")
public class UserVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("用户id")
    private Long id;
    @ApiModelProperty("用户账号")
    private String username;
    @ApiModelProperty("用户角色")
    private String userRole;
    @ApiModelProperty("用户手机号")
    private String userPhone;
    @ApiModelProperty("用户真实姓名")
    private String userRealName;
    @ApiModelProperty("性别：0-男性，1-女性")
    private UserGenderEnum userGender;
    @ApiModelProperty("用户年龄")
    private Integer userAge;
    @ApiModelProperty("用户邮箱")
    private String userEmail;
    @ApiModelProperty("用户头像")
    private String userAvatar;
    @ApiModelProperty("用户生日")
    private LocalDate userBirthday;
    @ApiModelProperty("用户简介")
    private String userProfile;
    @ApiModelProperty("账户状态：0-禁用 1-正常")
    private UserStatusEnum userStatus;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("用户token")
    private String accessToken;
    @ApiModelProperty("是否被关注 false-未关注 true-已关注")
    private Boolean followed = Boolean.FALSE;
}
