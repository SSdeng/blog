package com.zyd.blog.business.enums;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @date 2018/7/15 22:00
 * @since 1.0
 */
public enum CachePrefixEnum {
    /**
     * 定义缓存前缀
     */
    BIZ("biz_cache_"),
    VIEW("view_cache_"),
    DDOS("ddos_cache_"),
    WX("wx_api_cache_"),
    SPIDER("spider_cache_"),
    ;
    private String prefix;//前缀

    CachePrefixEnum(String prefix) {
        this.prefix = prefix;
    }

    /**
     * 获取缓存前缀
     * @return 前缀
     */
    public String getPrefix() {
        return prefix;
    }
}
