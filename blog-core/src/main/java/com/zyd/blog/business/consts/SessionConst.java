package com.zyd.blog.business.consts;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @website https://www.zhyd.me
 * @version 1.0
 * @date 2018/4/16 16:26
 * @since 1.0
 */
public class SessionConst {
	private SessionConst() {
		throw new IllegalStateException("SessionConst class");
	}
    /**
     * User 的 session key;k
     */
    public static final String USER_SESSION_KEY = "user";

    /**
     * kaptcha 的 session key
     */
    public static final String KAPTCHA_SESSION_KEY = "KAPTCHA_SESSION_KEY";
}
