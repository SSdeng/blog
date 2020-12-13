package com.zyd.blog.business.enums;

import org.springframework.util.StringUtils;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @website https://www.zhyd.me
 * @version 1.0
 * @date 2018/4/16 16:26
 * @since 1.0
 */

/**
 * 枚举用户的类型
 * @param ROOT:超级管理员
 * @param ADMIN:管理员
 * @param USER:系统会员
 * @param UNKNOW:未知
 * 根据用户的type遍历用户类型数组获取对应用户类型
 */
public enum UserTypeEnum {
    ROOT("超级管理员"),
    ADMIN("管理员"),
    USER("系统会员"),
    UNKNOW("未知");
    private String desc;//描述

    UserTypeEnum(String desc) {
        this.desc = desc;
    }

    /**
     * 根据用户的type遍历用户类型数组获取对应用户类型
     * @param type
     * @return 用户类型
     */
    public static UserTypeEnum getByType(String type) {
        if (StringUtils.isEmpty(type)) {
            return UserTypeEnum.UNKNOW;
        }
        for (UserTypeEnum ut : UserTypeEnum.values()) {
            if (ut.toString().equalsIgnoreCase(type)) {
                return ut;
            }
        }
        return UserTypeEnum.UNKNOW;
    }

    /**
     * 获取用户类型描述
     * @return 描述
     */
    public String getDesc() {
        return desc;
    }
}
