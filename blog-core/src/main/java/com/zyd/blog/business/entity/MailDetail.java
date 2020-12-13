package com.zyd.blog.business.entity;

import lombok.Data;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @website https://www.zhyd.me
 * @version 1.0
 * @date 2018/4/16 16:26
 * @since 1.0
 *
 * 所有的实体类都包装了beans中对应的持久化中的数据实体，
 * beans中的数据实体大多都继承了AbstractDO，抽象数据对象中都有自己的id，创建时间，更新时间
 *
 * Data注解
 * 1、可以为类提供读写功能，从而不用写get、set方法。
 * 2、他还会为类提供 equals()、hashCode()、toString() 方法。
 */
@Data
public class MailDetail {//邮件的细节

    /**
     * 邮件标题
     */
    private String subject;
    /**
     * 收件人地址
     */
    private String toMailAddress;
    /**
     * 收件人姓名
     */
    private String toUsername;
    /**
     * 抄送人
     */
    private String[] cc;
    /**
     * 邮件内容
     */
    private String content;
    /**
     * 附件列表
     */
    private List<String> filePaths;
    private boolean html = true;

    /**
     * 构造函数
     *
     * @param title
     * @param toMailAddress
     * @param toUsername
     * @param content
     */
    public MailDetail(String title, String toMailAddress, String toUsername, String content) {
        this.subject = title;
        this.toMailAddress = toMailAddress;
        this.toUsername = toUsername;
        this.content = content;
        this.filePaths = null;
    }

    /**
     * @return 头像？
     */
    public boolean isExitFile() {
        return getFilePaths() != null && getFilePaths().size() > 0;
    }

    /**
     * @return 返回路径
     */
    public String[] getFilePathArr() {
        if (!CollectionUtils.isEmpty(getFilePaths())) {
            return filePaths.toArray(new String[filePaths.size()]);
        }
        return new String[]{"NULL"};
    }

    public String[] getCc() {
        return cc;
    }

    /**
     * 设置抄送人
     *
     * @param cc
     * @return MailDetail
     */
    public MailDetail setCc(String[] cc) {
        this.cc = cc;
        return this;
    }

    /**
     * 设置抄送人
     *
     * @param cc
     * @return MailDetail
     */
    public MailDetail setCc(String cc) {
        if (!StringUtils.isEmpty(cc)) {
            this.cc = new String[]{cc};
        }
        return this;
    }

    /**
     * 设置抄送人
     *
     * @param cc
     * @return MailDetail
     */
    public MailDetail setCc(List<String> cc) {
        if (!CollectionUtils.isEmpty(cc)) {
            this.cc = cc.toArray(new String[cc.size()]);
        }
        return this;
    }
}
