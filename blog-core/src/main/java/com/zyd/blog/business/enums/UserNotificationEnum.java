package com.zyd.blog.business.enums;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @website https://www.zhyd.me
 * @version 1.0
 * @date 2018/4/16 16:26
 * @since 1.0
 */

/**
 * 枚举通知用户的消息类型
 *  @param DETAIL：通知显示消息详情
 *  @param TITLE：通知不显示消息详情
 *  根据用户的code获取通知用户消息类型
 */
public enum UserNotificationEnum {
    DETAIL(1, "通知显示消息详情"),
    TITLE(2, "通知不显示消息详情"),;
    private Integer code;
    private String desc;

    /**
     * 初始化通知用户消息类型
     * @param code
     * @param desc
     */
    UserNotificationEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据用户的code获取通知用户消息类型
     * @param code
     * @return 消息类型
     */
    public static UserNotificationEnum get(Integer code) {
        if (null == code) {
            return DETAIL;
        }
        UserNotificationEnum[] enums = UserNotificationEnum.values();
        for (UserNotificationEnum anEnum : enums) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return DETAIL;
    }

    /**
     * 获取通知消息类型的code
     * @return
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 获取通知消息的描述
     * @return 描述
     */
    public String getDesc() {
        return desc;
    }
}
