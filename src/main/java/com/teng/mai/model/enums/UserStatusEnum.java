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
 * @date: 2024/3/22 22:11
 */
@Getter
@AllArgsConstructor
public enum UserStatusEnum implements BaseEnum {
    DISABLED(0, "禁用"),
    NORMAL(1, "正常"),
    ;
    @EnumValue
    @JsonValue
    private final int value;
    private final String desc;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static UserStatusEnum of(Integer value) {
        if (value == null) return null;
        for (UserStatusEnum userStatusEnum : values()) {
            if (userStatusEnum.value == value) {
                return userStatusEnum;
            }
        }
        return null;
    }
}
