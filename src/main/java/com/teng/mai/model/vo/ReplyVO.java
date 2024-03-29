package com.teng.mai.model.vo;

import com.teng.mai.model.enums.UserGenderEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/3/23 15:30
 */
@Data
@ApiModel("子评论VO")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplyVO implements Serializable {
    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("父评论id")
    private Long parentId;
    @ApiModelProperty("评论用户id")
    private Long userId;
    @ApiModelProperty("评论用户账号")
    private String username;
    @ApiModelProperty("评论用户性别：0-男性，1-女性")
    private UserGenderEnum userGender;
    @ApiModelProperty("评论用户头像")
    private String userAvatar;
    @ApiModelProperty("评论内容")
    private String commentContent;
    @ApiModelProperty("评论时间")
    private LocalDateTime createTime;
    @ApiModelProperty("被评论用户id")
    private Long replyUserId;
    @ApiModelProperty("被评论用户账号")
    private String replyUsername;
}
