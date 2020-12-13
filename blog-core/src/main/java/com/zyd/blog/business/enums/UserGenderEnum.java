package com.zyd.blog.business.enums;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @website https://www.zhyd.me
 * @version 1.0
 * @date 2018/4/16 16:26
 * @since 1.0
 */

/**
 * 枚举使用者性别包括MALE或FEMALE
 * 根据使用者code获取使用者性别
 * @param MALE 男
 * @param FEMALE 女
 */
public enum UserGenderEnum {
    MALE(1, "男"), FEMALE(0, "女"), UNKNOW(-1, "");
    private int code;
    private String desc;//描述

    /**
     * 初始化使用者性别
     * @param code
     * @param desc
     */
    UserGenderEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据使用者code获取使用者性别
     * @param code
     * @return 用户性别
     */
    public static UserGenderEnum getUserSex(Integer code) {
        if (code == null) {
            return UNKNOW;
        }
        for (UserGenderEnum userSexEnum : UserGenderEnum.values()) {
            if (userSexEnum.getCode() == code) {
                return userSexEnum;
            }
        }
        return UNKNOW;
    }

    /**
     * 根据使用者code获取使用者性别类型
     * @param code
     * @return 性别类型
     */
    public static UserGenderEnum getUserSex(String code) {
        if (code == null) {
            return UNKNOW;
        }
        if("m".equals(code)){
            return MALE;
        }
        if("f".equals(code)){
            return FEMALE;
        }
        return UNKNOW;
    }

    /**
     * 获取使用者code
     * @return
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取使用者性别描述
     * @return 描述
     */
    public String getDesc() {
        return desc;
    }
}
