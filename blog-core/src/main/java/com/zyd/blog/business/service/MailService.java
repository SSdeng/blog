package com.zyd.blog.business.service;

import com.zyd.blog.business.entity.Comment;
import com.zyd.blog.business.entity.Link;
import com.zyd.blog.business.entity.MailDetail;
import com.zyd.blog.business.enums.TemplateKeyEnum;

/**
 * 邮件发送
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
public interface MailService {

    /**
     * 普通的发送
     *
     * @param mailDetail 邮件类
     */
    void send(MailDetail mailDetail);

    /**
     * 发送友情链接邮件通知
     *
     * @param link 友链
     * @param keyEnum 模板键值
     */
    void send(Link link, TemplateKeyEnum keyEnum);

    /**
     * 发送评论回复通知或评论审核结果通知
     *
     * @param comment 评论
     * @param keyEnum 模板键值
     * @param audit 是否为审核结果通知
     */
    void send(Comment comment, TemplateKeyEnum keyEnum, boolean audit);

    /**
     * 发送到管理员的友链操作通知
     *
     * @param link 友链
     */
    void sendToAdmin(Link link);

    /**
     * 发送到管理员的评论通知
     *
     * @param comment 评论
     */
    void sendToAdmin(Comment comment);
}
