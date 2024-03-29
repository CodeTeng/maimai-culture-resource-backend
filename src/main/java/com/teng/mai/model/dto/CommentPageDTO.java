package com.teng.mai.model.dto;

import com.teng.mai.common.page.PageQuery;
import com.teng.mai.model.enums.CommentTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/3/23 15:48
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("分页获取评论DTO")
@AllArgsConstructor
@NoArgsConstructor
public class CommentPageDTO extends PageQuery {
    @ApiModelProperty("评论类型 1.文章 2.留言")
    private CommentTypeEnum type;
    @ApiModelProperty("评论主题id")
    private Long topicId;
}
