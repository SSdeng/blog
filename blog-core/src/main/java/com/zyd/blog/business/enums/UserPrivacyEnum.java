package com.zyd.blog.business.enums;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @website https://www.zhyd.me
 * @version 1.0
 * @date 2018/4/16 16:26
 * @since 1.0
 */

/**
 * 枚举用户隐私的类型
 * @param PUBLIC:公开
 * @param PRIVATE：不公开
 * 根据用户的code遍历用户隐私类型数组获取相对应的隐私类型
 */
public enum UserPrivacyEnum {
    PUBLIC(1, "公开"),
    PRIVATE(2, "不公开"),;
    private Integer code;
    private String desc;

    /**
     * 初始化用户隐私类型
     * @param code
     * @param desc
     */
    UserPrivacyEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据用户的code遍历用户隐私类型数组获取相对应的隐私类型
     * @param code
     * @return 隐私类型
     */
    public static UserPrivacyEnum get(Integer code) {
        if (null == code) {
            return PUBLIC;
        }
        UserPrivacyEnum[] enums = UserPrivacyEnum.values();
        for (UserPrivacyEnum anEnum : enums) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return PUBLIC;
    }

    /**
     * 获取用户隐私类型的code
     * @return
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 获取用户隐私类型的描述
     * @return 描述
     */
    public String getDesc() {
        return desc;
    }

}
