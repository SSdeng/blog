package com.zyd.blog.file.alioss.entity;

import lombok.Data;

import java.util.List;

/**
 * 防盗链Referer白名单实体
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2017/7/12 10:29
 * @since 1.8
 */
@Data
public class RefererEntity extends AbstractEntity {
    /**
     * referer列表
     * Referer是HTTP请求header的一部分，当浏览器（或者模拟浏览器行为）<br>
     * 向web服务器发送请求的时候，头信息里有包含 Referer 。
     */
    List<String> refererList;

    public RefererEntity(String bucketName) {
        super(bucketName);
    }

    public void setRefererList(List<String> refererList) {
        this.refererList = refererList;
    }
}
