package com.teng.mai.constant;

import cn.hutool.core.lang.RegexPool;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/3/22 21:49
 */
public interface RegexConstants extends RegexPool {
    /**
     * 手机号正则
     */
    String PHONE_PATTERN = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$";
    /**
     * 邮箱正则
     */
    String EMAIL_PATTERN = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    /**
     * 密码正则。6~32位的字母、数字、下划线
     */
    String PASSWORD_PATTERN = "^\\w{6,32}$";
    /**
     * 用户名正则。4~32位的字母、数字、下划线
     */
    String USERNAME_PATTERN = "^\\w{4,32}$";
    /**
     * 验证码正则, 6位数字或字母
     */
    String VERIFY_CODE_PATTERN = "^[a-zA-Z\\d]{6}$";
}
