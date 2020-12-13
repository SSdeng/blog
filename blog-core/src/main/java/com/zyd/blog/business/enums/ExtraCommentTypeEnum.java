package com.zyd.blog.business.enums;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2019/2/12 10:20
 * @since 1.8
 */

/**
 * 额外的评论类型枚举
 * @param GUESTBOOK 留言板
 * @param LINKS 友情链接
 * @param ABOUT 关于
 * @param ARTICLE 文章
 * 及其属性sid、url、title
 */
public enum ExtraCommentTypeEnum {

    GUESTBOOK(-1L, "/guestbook", "留言板 "),
    LINKS(-2L, "/links", "友情链接 "),
    ABOUT(-3L, "/about", "关于"),
    ARTICLE(null, "/article/", ""),
    ;

    private Long sid;
    private String url;
    private String title;//文章

    /**
     * 初始化额外的评论类型
     * @param sid
     * @param url
     * @param title
     */
    ExtraCommentTypeEnum(Long sid, String url, String title) {
        this.sid = sid;
        this.url = url;
        this.title = title;
    }

    /**
     * 从额外的枚举类型中寻找对应的sid
     * @param sid
     * @return ARTICLE
     */
    public static ExtraCommentTypeEnum getBySid(Long sid) {
        if (sid == null) {
            return null;
        }
        for (ExtraCommentTypeEnum value : ExtraCommentTypeEnum.values()) {
            if (sid.equals(value.getSid())) {
                return value;
            }
        }
        return ARTICLE;
    }

    /**
     * 获取评论sid
     * @return sid
     */
    public Long getSid() {
        return sid;
    }

    /**
     * 获取评论的url
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 获取评论的title
     * @return title
     */
    public String getTitle() {
        return title;
    }

}
