package com.teng.mai.model.dto;

import com.teng.mai.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/3/25 22:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("评论搜索DTO")
@AllArgsConstructor
@NoArgsConstructor
public class CommentSearchDTO extends PageQuery {
    @ApiModelProperty("评论的内容")
    private String commentContent;
}
