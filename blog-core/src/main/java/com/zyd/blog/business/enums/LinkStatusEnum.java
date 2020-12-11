package com.zyd.blog.business.enums;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @website https://www.zhyd.me
 * @version 1.0
 * @date 2018/4/16 16:26
 * @since 1.0
 */

/**
 * 定义链接资源的状态
 * @param EWABLE：可用
 * @param DISABLE：禁用
 */
public enum LinkStatusEnum {
    ENABLE("可用"),
    DISABLE("禁用");
    private String desc;

    LinkStatusEnum(String desc) {
        this.desc = desc;
    }

    /**
     * 获取状态类型描述
     * @return desc
     */
    public String getDesc() {
        return desc;
    }
}
