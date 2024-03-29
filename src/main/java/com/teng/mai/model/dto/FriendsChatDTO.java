package com.teng.mai.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/3/24 0:01
 */
@Data
@ApiModel("好友聊天DTO")
@AllArgsConstructor
@NoArgsConstructor
public class FriendsChatDTO implements Serializable {
    @NotNull(message = "好友id不能为空")
    @ApiModelProperty(value = "好友id", required = true)
    private Long toId;
}
