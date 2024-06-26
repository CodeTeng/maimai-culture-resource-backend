package com.teng.mai.model.dto;

import com.teng.mai.constant.RegexConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/3/22 22:04
 */
@Data
@ApiModel("用户注册请求体")
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO implements Serializable {
    private static final long serialVersionUID = 3191241716373120793L;
    @ApiModelProperty(value = "用户账号", required = true)
    @NotBlank(message = "用户账号不能为空")
    @Pattern(regexp = RegexConstants.USERNAME_PATTERN, message = "用户账号为4~32位的字母、数字、下划线")
    private String username;
    @ApiModelProperty(value = "用户密码", required = true)
    @NotBlank(message = "用户密码不能为空")
    @Pattern(regexp = RegexConstants.PASSWORD_PATTERN, message = "密码为6~32位的字母、数字、下划线")
    private String userPassword;
    @ApiModelProperty(value = "确认密码", required = true)
    @NotBlank(message = "确认密码不能为空")
    private String checkPassword;
}
