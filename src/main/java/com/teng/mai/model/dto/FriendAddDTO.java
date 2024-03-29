package com.teng.mai.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/3/23 22:16
 */
@Data
@ApiModel("好友申请DTO")
@AllArgsConstructor
@NoArgsConstructor
public class FriendAddDTO implements Serializable {
    private static final long serialVersionUID = 1472823745422792988L;
    @ApiModelProperty(value = "接收申请的用户id")
    private Long receiveId;
    @NotBlank(message = "用户账号不能为空")
    @ApiModelProperty(value = "接收申请用户账号", required = true)
    private String username;
    @ApiModelProperty("好友申请备注信息")
    private String remark;
}
