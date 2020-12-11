package com.zyd.blog.business.enums;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @website https://www.zhyd.me
 * @version 1.0
 * @date 2018/4/16 16:26
 * @since 1.0
 */

/**
 * 枚举用户的状态
 * @param NORMAL:正常
 * @param DISABLE:禁用
 * 根据用户的code遍历用户状态数组获取对应用户状态
 */
public enum UserStatusEnum {
    NORMAL(1, "正常"),
    DISABLE(0, "禁用"),;
    private Integer code;
    private String desc;

    UserStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据用户的code遍历用户状态数组获取对应用户状态
     * @param code
     * @return
     */
    public static UserStatusEnum get(Integer code) {
        if (null == code) {
            return NORMAL;
        }
        UserStatusEnum[] enums = UserStatusEnum.values();
        for (UserStatusEnum anEnum : enums) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return NORMAL;
    }

    /**
     * 获取用户状态code
     * @return
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 获取用户状态的描述
     * @return
     */
    public String getDesc() {
        return desc;
    }

}
