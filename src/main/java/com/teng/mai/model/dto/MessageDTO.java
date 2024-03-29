package com.teng.mai.model.dto;

import com.teng.mai.model.enums.ChatTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/3/24 11:49
 */
@Data
@ApiModel("收到消息DTO")
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO implements Serializable {
    private static final long serialVersionUID = 1324635911327892058L;
    @ApiModelProperty("发送给谁")
    private Long toId;
    @ApiModelProperty("发送消息")
    private String text;
    @ApiModelProperty("聊天类型 1-私聊 2-大厅聊天")
    private ChatTypeEnum chatType;
}
