package com.teng.mai.model.dto;

import com.teng.mai.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/3/30 17:14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "评论回答分页查询条件")
public class ReplySearchDTO extends PageQuery {
    @ApiModelProperty(value = "文章id，不为空则代表根据文章查询回复")
    private Long articleId;
    @ApiModelProperty(value = "回答id，不为空则代表根据回复查询评论")
    private Long answerId;
}
