package com.teng.mai.model.vo;

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
 * @date: 2024/3/23 23:53
 */
@Data
@ApiModel("聊天消息VO")
@AllArgsConstructor
@NoArgsConstructor
public class MessageVO implements Serializable {
    private static final long serialVersionUID = -4722378360550337925L;
    @ApiModelProperty("发送消息人")
    private UserWebSocketVO fromUser;
    @ApiModelProperty("接受消息人")
    private UserWebSocketVO toUser;
    @ApiModelProperty("发送消息")
    private String text;
    private Boolean isMy = false;
    @ApiModelProperty("聊天类型 1-私聊 2-大厅聊天")
    private ChatTypeEnum chatType;
    @ApiModelProperty("聊天时间")
    private String createTime;
}
