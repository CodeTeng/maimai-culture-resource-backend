package com.teng.mai.common.validate.annotation;

import com.teng.mai.common.validate.EnumValidator;
import com.teng.mai.common.validate.EnumValueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @description: 用于状态的枚举校验
 * @author: ~Teng~
 * @date: 2024/2/16 22:11
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
@Constraint(validatedBy = {EnumValidator.class, EnumValueValidator.class})
public @interface EnumValid {
    String message() default "请输入正确的类型";

    int[] enumeration() default {};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
