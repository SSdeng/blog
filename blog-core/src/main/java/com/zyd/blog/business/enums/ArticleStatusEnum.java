package com.zyd.blog.business.enums;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @website https://www.zhyd.me
 * @version 1.0
 * @date 2018/4/16 16:26
 * @since 1.0
 */
public enum ArticleStatusEnum {
    /**
     * 文章发布状态
     * @param PUBLISHED 发布为1
     * @param UNPUBLISHED草稿为0
     * 描述
     */
    PUBLISHED(1, "发布"),
    UNPUBLISHED(0, "草稿");
    private int code;
    private String desc;

    ArticleStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ArticleStatusEnum get(Integer code) {
        //如果code为空则定义状态为草稿
        if (code == null) {
            return UNPUBLISHED;
        }
        /**
         * 遍历所有发布状态，如果其中有对应的code，则返回发布状态数组，否则返回草稿状态
         */
        ArticleStatusEnum[] statusEnums = ArticleStatusEnum.values();
        for (ArticleStatusEnum statusEnum : statusEnums) {
            if (statusEnum.getCode() == code) {
                return statusEnum;
            }
        }
        return UNPUBLISHED;
    }

    /**
     * 获取文章发布状态
     * @return code
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取文章描述
     * @return desc
     */
    public String getDesc() {
        return desc;
    }
}
