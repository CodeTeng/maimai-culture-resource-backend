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
 * @date: 2024/3/23 19:36
 */
@Data
@ApiModel("用户个人修改密码请求体")
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateMyPwdDTO implements Serializable {
    @ApiModelProperty(value = "用户原始密码", required = true)
    @NotBlank(message = "用户原始密码不能为空")
    @Pattern(regexp = RegexConstants.PASSWORD_PATTERN, message = "密码为6~32位的字母、数字、下划线")
    private String oldPassword;
    @ApiModelProperty(value = "用户新密码", required = true)
    @NotBlank(message = "用户新密码不能为空")
    @Pattern(regexp = RegexConstants.PASSWORD_PATTERN, message = "密码为6~32位的字母、数字、下划线")
    private String userPassword;
    @ApiModelProperty(value = "确认密码", required = true)
    @NotBlank(message = "确认密码不能为空")
    private String checkPassword;
}
