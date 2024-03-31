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
 * @date: 2024/3/31 16:06
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("我的评论搜索")
@AllArgsConstructor
@NoArgsConstructor
public class MyReplySearchDTO extends PageQuery {
    @ApiModelProperty("搜索关键词")
    private String keyword;
}
