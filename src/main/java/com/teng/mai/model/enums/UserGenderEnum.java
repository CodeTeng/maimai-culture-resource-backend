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
 * @date: 2024/3/22 22:10
 */
@Getter
@AllArgsConstructor
public enum UserGenderEnum implements BaseEnum {
    MAN(0, "男性"),
    WOMAN(1, "女性"),
    ;
    @EnumValue
    @JsonValue
    private final int value;
    private final String desc;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static UserGenderEnum of(Integer value) {
        if (value == null) return null;
        for (UserGenderEnum userGenderEnum : values()) {
            if (userGenderEnum.value == value) {
                return userGenderEnum;
            }
        }
        return null;
    }
}
