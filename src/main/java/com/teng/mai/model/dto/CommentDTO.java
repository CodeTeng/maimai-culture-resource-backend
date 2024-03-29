package com.teng.mai.model.dto;

import com.teng.mai.model.enums.CommentTypeEnum;
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
 * @date: 2024/3/23 14:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("添加评论DTO")
public class CommentDTO implements Serializable {
    @NotBlank(message = "评论内容不能为空")
    @ApiModelProperty(name = "commentContent", value = "评论内容", required = true, dataType = "String")
    private String commentContent;
    @ApiModelProperty(name = "topicId", value = "主题id", dataType = "Long")
    private Long topicId;
    @NotNull(message = "评论类型不能为空")
    @ApiModelProperty(name = "type", value = "评论类型 1.文章 2.留言 ... 可扩展", dataType = "Integer", required = true)
    private CommentTypeEnum type;
    @ApiModelProperty(name = "replyUserId", value = "回复用户id", dataType = "Long")
    private Long replyUserId;
    @ApiModelProperty(name = "parentId", value = "父级评论id", dataType = "Long")
    private Long parentId;
}
