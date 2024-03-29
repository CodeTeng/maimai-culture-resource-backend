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
 * @date: 2024/3/23 14:47
 */
@Getter
@AllArgsConstructor
public enum CommentTypeEnum implements BaseEnum {
    ARTICLE(1, "文章"),
    MESSAGE(2, "留言"),
    ;
    @EnumValue
    @JsonValue
    private final int value;
    private final String desc;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static CommentTypeEnum of(Integer value) {
        if (value == null) return null;
        for (CommentTypeEnum commentTypeEnum : values()) {
            if (commentTypeEnum.value == value) {
                return commentTypeEnum;
            }
        }
        return null;
    }
}
