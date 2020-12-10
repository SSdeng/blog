package com.zyd.blog.business.service;

import me.zhyd.hunter.config.HunterConfig;

import java.io.PrintWriter;

/**
 * 搬运工服务接口类
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/8/21 15:35
 * @since 1.8
 */
public interface RemoverService {

    /**
     * 运行文章搬运工
     * @param typeId 文章分类
     * @param config 博客猎手配置信息
     * @param writer 输出流
     */
    void run(Long typeId, HunterConfig config, PrintWriter writer);

    /**
     * 停止文章搬运工
     */
    void stop();

    /**
     * 抓取文章
     * @param typeId 文章分类
     * @param url url
     * @param convertImg 是否转存图片
     * @param writer 输出流
     */
    void crawlSingle(Long typeId, String[] url, boolean convertImg, PrintWriter writer);
}
