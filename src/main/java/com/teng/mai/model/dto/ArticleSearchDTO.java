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
 * @date: 2024/3/22 23:38
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("文章搜索DTO")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticleSearchDTO extends PageQuery {
    @ApiModelProperty("搜索关键词")
    private String keyword;
    @ApiModelProperty("标签id")
    private Long tagId;
}
