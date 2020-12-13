package com.zyd.blog.business.service.impl;

import com.zyd.blog.business.entity.Comment;
import com.zyd.blog.business.entity.Link;
import com.zyd.blog.business.entity.MailDetail;
import com.zyd.blog.business.entity.Template;
import com.zyd.blog.business.enums.ConfigKeyEnum;
import com.zyd.blog.business.enums.TemplateKeyEnum;
import com.zyd.blog.business.service.MailService;
import com.zyd.blog.business.service.SysConfigService;
import com.zyd.blog.business.service.SysTemplateService;
import com.zyd.blog.util.FreeMarkerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 邮件发送
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Slf4j
@Service
public class MailServiceImpl implements MailService {

    private static final String CONFIG = "config";

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private SysTemplateService templateService;
    @Autowired
    private SysConfigService configService;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * 普通的发送
     *
     * @param mailDetail 邮件类
     */
    @Override
    @Async
    public void send(MailDetail mailDetail) {
        sendMessage(mailDetail, from);
    }

    /**
     * 发送友情链接邮件通知
     *
     * @param link 友链
     * @param keyEnum 模板键值
     */
    @Override
    @Async
    public void send(Link link, TemplateKeyEnum keyEnum) {
        // 友情链邮箱不为空
        if (!StringUtils.isEmpty(link.getEmail())) {
            // 获取系统配置
            Map<String, Object> config = configService.getConfigs();
            // 获取相应模板信息
            Template template = templateService.getTemplate(keyEnum);
            // 获取模板值
            String temXml = template.getRefValue();
            Map<String, Object> map = new HashMap<>(2);
            // 记录友链
            map.put("link", link);
            // 记录系统配置
            map.put(CONFIG, config);
            // 按模板及map数据生成邮件正文
            String mailContext = FreeMarkerUtil.template2String(temXml, map, true);

            // 创建邮件类对象
            MailDetail mailDetail = new MailDetail("友情链接操作通知", link.getEmail(), link.getName(), mailContext);
            // 发送邮件
            send(mailDetail);
        }
        // 发送到管理员的友链操作通知
        this.sendToAdmin(link);
    }

    /**
     * 发送评论回复通知或评论审核结果通知
     *
     * @param comment 评论
     * @param keyEnum 模板键值
     * @param audit 是否为审核结果通知
     */
    @Override
    @Async
    public void send(Comment comment, TemplateKeyEnum keyEnum, boolean audit) {
        // 评论为空或评论邮箱为空
        if (comment == null || StringUtils.isEmpty(comment.getEmail())) {
            // 发送给管理员
            this.sendToAdmin(comment);
            return;
        }
        // 获取系统配置
        Map<String, Object> config = configService.getConfigs();
        // 获取模板
        Template template = templateService.getTemplate(keyEnum);
        // 获取模板值
        String temXml = template.getRefValue();
        Map<String, Object> map = new HashMap<>(2);
        // 记录评论
        map.put("comment", comment);
        // 记录系统配置
        map.put(CONFIG, config);
        // 按模板及map数据生成邮件正文
        String mailContext = FreeMarkerUtil.template2String(temXml, map, true);
        // 标志通知类型
        String subject = "评论回复通知";
        // audit为真
        if (audit) {
            // 修改通知类型
            subject = "评论审核结果通知";
        }
        // 创建邮件类对象
        MailDetail mailDetail = new MailDetail(subject, comment.getEmail(), comment.getNickname(), mailContext);
        // 发送邮件
        send(mailDetail);
        // 通知为评论回复通知
        if (!audit) {
            // 发送给管理员
            this.sendToAdmin(comment);
        }
    }

    /**
     * 发送到管理员的友链操作通知
     *
     * @param link 友链
     */
    @Override
    @Async
    public void sendToAdmin(Link link) {
        // 获取系统配置
        Map<String, Object> config = configService.getConfigs();
        // 获取模板
        Template template = templateService.getTemplate(TemplateKeyEnum.TM_LINKS_TO_ADMIN);
        // 获取模板值
        String temXml = template.getRefValue();
        Map<String, Object> map = new HashMap<>(1);
        // 记录友链
        map.put("link", link);
        // 按模板及map数据生成邮件正文
        String mailContext = FreeMarkerUtil.template2String(temXml, map, true);
        // 从系统配置中获取站长邮箱
        String adminEmail = (String) config.get(ConfigKeyEnum.AUTHOR_EMAIL.getKey());
        // 邮箱为空
        if (StringUtils.isEmpty(adminEmail)) {
            log.warn("[sendToAdmin]邮件发送失败！未指定系统管理员的邮箱地址");
            return;
        }
        // 规范邮箱格式
        adminEmail = (adminEmail.contains("#") ? adminEmail.replace("#", "@") : adminEmail);
        // 创建邮件对象
        MailDetail mailDetail = new MailDetail("有新的友链消息", adminEmail, (String) config.get(ConfigKeyEnum.AUTHOR_NAME.getKey()), mailContext);
        // 发送邮件
        send(mailDetail);
    }

    /**
     * 发送到管理员的评论通知
     *
     * @param comment 评论
     */
    @Override
    @Async
    public void sendToAdmin(Comment comment) {
        // 获取系统配置
        Map<String, Object> config = configService.getConfigs();
        // 获取模板
        Template template = templateService.getTemplate(TemplateKeyEnum.TM_NEW_COMMENT);
        // 获取模板值
        String temXml = template.getRefValue();
        Map<String, Object> map = new HashMap<>(2);
        // 记录评论
        map.put("comment", comment);
        // 记录系统配置
        map.put(CONFIG, config);
        // 按模板及map数据生成邮件正文
        String mailContext = FreeMarkerUtil.template2String(temXml, map, true);
        // 从系统配置中获取作者邮箱
        String adminEmail = (String) config.get("authorEmail");
        // 邮箱为空
        if (StringUtils.isEmpty(adminEmail)) {
            log.warn("[sendToAdmin]邮件发送失败！未指定系统管理员的邮箱地址");
            return;
        }
        // 规范邮箱格式
        adminEmail = (adminEmail.contains("#") ? adminEmail.replace("#", "@") : adminEmail);
        // 生成邮件类对象
        MailDetail mailDetail = new MailDetail("有新的评论消息", adminEmail, (String) config.get("authorName"), mailContext);
        // 发送邮件
        send(mailDetail);
    }

    /**
     * 发送消息
     *
     * @param detail 邮件类对象
     * @param from 发送者
     */
    private void sendMessage(MailDetail detail, String from) {
        // 记录发送信息
        log.info("Start to send html email for [{}({})]", detail.getToUsername(), detail.getToMailAddress());
        // 邮件接收地址为空
        if (StringUtils.isEmpty(detail.getToMailAddress())) {
            log.warn("邮件接收者为空！");
            return;
        }
        MimeMessage message = null;
        try {
            // 创建消息
            message = javaMailSender.createMimeMessage();
            // 消息发送工具
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            // 创建发送地址
            InternetAddress fromAddress = new InternetAddress(MimeUtility.encodeText("网站管理员") + "<" + from + ">");
            // 记录发送地址
            helper.setFrom(fromAddress);
            // 创建接收地址
            InternetAddress toAddress = new InternetAddress(MimeUtility.encodeText(detail.getToMailAddress()) + "<" + detail.getToMailAddress() + ">");
            // 记录接收地址
            helper.setTo(toAddress);
            // 记录消息类型
            helper.setSubject(detail.getSubject());
            // 记录消息正文
            helper.setText(detail.getContent(), detail.isHtml());
            // 抄送人不为空
            if (detail.getCc() != null && detail.getCc().length > 0) {
                // 记录抄送人
                helper.setCc(detail.getCc());
            }
            // 发送消息
            javaMailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Failed to send E-mail. e [{}]", e.getMessage());
        }
    }
}
