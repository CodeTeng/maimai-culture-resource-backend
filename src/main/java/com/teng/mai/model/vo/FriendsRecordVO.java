package com.teng.mai.model.vo;

import com.teng.mai.model.enums.FriendsStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/3/23 23:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendsRecordVO implements Serializable {
    private static final long serialVersionUID = 1928465648232335L;
    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("申请状态 默认0 （0-未通过 1-已同意 2-已过期 3-已撤销）")
    private FriendsStatusEnum status;
    @ApiModelProperty("好友申请备注信息")
    private String remark;
    @ApiModelProperty("申请用户")
    private UserVO applyUser;
}
