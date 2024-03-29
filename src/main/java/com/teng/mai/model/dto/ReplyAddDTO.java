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
 * @date: 2024/3/26 11:08
 */
@Data
@ApiModel("添加评论DTO")
@AllArgsConstructor
@NoArgsConstructor
public class ReplyAddDTO implements Serializable {
    @ApiModelProperty("回答内容")
    @NotNull(message = "回答内容不能为空")
    private String content;
    @ApiModelProperty("文章id")
    @NotNull(message = "文章id不能为空")
    private Long articleId;
    @ApiModelProperty("回复的上级回复id，没有可不填")
    private Long answerId;
    @ApiModelProperty("回复的目标回复id，没有可不填")
    private Long targetReplyId;
    @ApiModelProperty("回复的目标用户id，没有可不填")
    private Long targetUserId;
}
