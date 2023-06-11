package com.jishuqin.constant;

public class JwtConstant {//该类的常量值要根据具体的项目进行设置
    /* 请求头相关 */
    public static final String SECRET_KEY = "jishuqin"; // Secret密钥
    public static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24; // token过期时间(毫秒) 1天
}
