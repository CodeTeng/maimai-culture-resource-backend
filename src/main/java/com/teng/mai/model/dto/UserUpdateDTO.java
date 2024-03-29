package com.teng.mai.model.dto;

import com.teng.mai.common.validate.annotation.EnumValid;
import com.teng.mai.constant.RegexConstants;
import com.teng.mai.model.enums.UserGenderEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/3/23 19:29
 */
@Data
@ApiModel("修改个人信息DTO")
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = RegexConstants.USERNAME_PATTERN, message = "用户名为4~32位的字母、数字、下划线")
    private String username;
    @ApiModelProperty("用户手机号")
    @Pattern(regexp = RegexConstants.PHONE_PATTERN, message = "请填写正确的手机号")
    private String userPhone;
    @ApiModelProperty("用户真实姓名")
    private String userRealName;
    @ApiModelProperty(value = "性别：0-男性，1-女性", required = true)
    @EnumValid(enumeration = {0, 1}, message = "用户性别不符合")
    private UserGenderEnum userGender;
    @ApiModelProperty("用户年龄")
    @Min(value = 0L, message = "请填写正确的年龄")
    @Max(value = 120L, message = "请填写正确的年龄")
    private Integer userAge;
    @ApiModelProperty("用户邮箱")
    @Pattern(regexp = RegexConstants.EMAIL_PATTERN, message = "请填写正确的邮箱格式")
    private String userEmail;
    @ApiModelProperty("用户头像")
    private String userAvatar;
    @ApiModelProperty("用户生日")
    private LocalDate userBirthday;
    @ApiModelProperty("用户简介")
    private String userProfile;
}
