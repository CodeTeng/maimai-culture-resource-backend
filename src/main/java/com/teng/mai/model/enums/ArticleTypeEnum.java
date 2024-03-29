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
 * @date: 2024/3/28 15:33
 */
@Getter
@AllArgsConstructor
public enum ArticleTypeEnum implements BaseEnum {
    DISABLED(1, "文章"),
    NORMAL(2, "视频"),
    ;
    @EnumValue
    @JsonValue
    private final int value;
    private final String desc;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ArticleTypeEnum of(Integer value) {
        if (value == null) return null;
        for (ArticleTypeEnum articleTypeEnum : values()) {
            if (articleTypeEnum.value == value) {
                return articleTypeEnum;
            }
        }
        return null;
    }
}
