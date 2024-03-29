package com.teng.mai.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.teng.mai.common.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/3/23 22:04
 */
@Getter
@AllArgsConstructor
public enum FriendsStatusEnum implements BaseEnum {
    NOT_PASSED(0, "未通过"),
    PASSED(1, "已同意"),
    EXPIRED(2, "已过期"),
    RESCINDED(3, "已撤销"),
    ;
    @EnumValue
    @JsonValue
    private final int value;
    private final String desc;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static FriendsStatusEnum of(Integer value) {
        if (value == null) return null;
        for (FriendsStatusEnum friendsStatusEnum : values()) {
            if (friendsStatusEnum.value == value) {
                return friendsStatusEnum;
            }
        }
        return null;
    }
}
