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
@ApiModel("互动回答信息")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplyVO implements Serializable {
    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("评论内容")
    private String commentContent;
    @ApiModelProperty("评论数量")
    private Integer replyTimes;
    @ApiModelProperty("评论时间")
    private LocalDateTime createTime;
    @ApiModelProperty("评论用户id")
    private Long userId;
    @ApiModelProperty("评论用户账号")
    private String username;
    @ApiModelProperty("评论用户性别：0-男性，1-女性")
    private UserGenderEnum userGender;
    @ApiModelProperty("评论用户头像")
    private String userAvatar;
    @ApiModelProperty("被评论用户id")
    private Long targetUserId;
    @ApiModelProperty("被评论用户账号")
    private String targetUsername;

    @ApiModelProperty("文章id")
    private Long articleId;
}
