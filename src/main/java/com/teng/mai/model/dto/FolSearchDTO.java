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
 * @date: 2024/3/23 19:06
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("关注查询DTO")
@AllArgsConstructor
@NoArgsConstructor
public class FolSearchDTO extends PageQuery {
    @ApiModelProperty("查询关键词")
    private String keyword;
}
