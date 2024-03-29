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
 * @date: 2024/3/25 19:30
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("搜索好友DTO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendSearchDTO extends PageQuery {
    @ApiModelProperty("搜索关键词")
    private String keyword;
}
