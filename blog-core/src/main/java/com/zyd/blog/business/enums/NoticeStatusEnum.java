package com.zyd.blog.business.enums;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @website https://www.zhyd.me
 * @version 1.0
 * @date 2018/4/16 16:26
 * @since 1.0
 */

/**
 * 枚举发布通知的状态
 * @param RELEASE:已发布
 * @param NOT_RELEASE:未发布
 */
public enum NoticeStatusEnum {
    RELEASE("已发布"),
    NOT_RELEASE("未发布");
    private String desc;

    /**
     * 初始化发布通知状态
     * @param desc
     */
    NoticeStatusEnum(String desc) {
        this.desc = desc;
    }

    /**
     * 获取发布通知状态的描述
     * @return desc
     */
    public String getDesc() {
        return desc;
    }
}
