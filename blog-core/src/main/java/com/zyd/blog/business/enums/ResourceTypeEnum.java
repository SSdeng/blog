package com.zyd.blog.business.enums;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @website https://www.zhyd.me
 * @version 1.0
 * @date 2018/4/16 16:26
 * @since 1.0
 */

/**
 * 枚举资源类型
 * @param menu:菜单
 * @param button:按钮
 */
public enum ResourceTypeEnum {
    menu("菜单"), button("按钮");

    private final String info;//信息

    /**
     * 初始化资源类型
     * @param info
     */
    private ResourceTypeEnum(String info) {
        this.info = info;
    }

    /**
     * 获取资源类型的信息
     * @return
     */
    public String getInfo() {
        return info;
    }
}
