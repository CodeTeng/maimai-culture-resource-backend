package com.teng.mai.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/3/23 23:54
 */
@Data
@ApiModel("聊天人VO")
@AllArgsConstructor
@NoArgsConstructor
public class UserWebSocketVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("用户id")
    private Long id;
    @ApiModelProperty("用户账号")
    private String username;
    @ApiModelProperty("用户头像")
    private String userAvatar;
}
