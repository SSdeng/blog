package com.zyd.blog.business.enums;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @website https://www.zhyd.me
 * @version 1.0
 * @date 2018/4/16 16:26
 * @since 1.0
 */

/**
 * 定义链接资源的类型及其描述包括
 * @param AUTOMATIC 自动申请
 * @param ADMIN 管理员添加
 * @param OTHER 其他
 */
public enum LinkSourceEnum {

    AUTOMATIC("自动申请"),
    ADMIN("管理员添加"),
    OTHER("其他");
    private String desc;

    /**
     * 初始化链接资源
     * @param desc
     */
    LinkSourceEnum(String desc) {
        this.desc = desc;
    }

    /**
     * 获取链接资源的描述
     * @return desc
     */
    public String getDesc() {
        return desc;
    }
}
