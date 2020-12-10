package com.zyd.blog.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zyd.blog.business.enums.LogLevelEnum;
import com.zyd.blog.business.enums.LogTypeEnum;
import com.zyd.blog.persistence.beans.SysLog;

import java.util.Date;

/**
 * @author yadong.zhang email:yadong.zhang0415(a)gmail.com
 * @version 1.0
 * @date 2018/01/09 17:40
 * @since 1.0
 *
 * 所有的实体类都包装了beans中对应的持久化中的数据实体，
 * beans中的数据实体大多都继承了AbstractDO，抽象数据对象中都有自己的id，创建时间，更新时间
 */
public class Log {//业务实体系统日志，包装了持久化中的数据实体SysLog
    private static final long serialVersionUID = 1L;
    private SysLog sysLog;

    /* generateConstructor */
    public Log() {
        this.sysLog = new SysLog();
    }

    /**
     * 带参数的构造函数
     *
     * @param sysLog
     */
    public Log(SysLog sysLog) {
        this.sysLog = sysLog;
    }

    /**
     * @return 返回系统日志
     */
    @JsonIgnore//在json序列化时将pojo中的一些属性忽略掉，标记在属性或者方法上，返回的json数据即不包含该属性。
    public SysLog getSysLog() {
        return this.sysLog;
    }

    /**
     * @return 返回这条记录的Id
     */
    public Long getId() {
        return this.sysLog.getId();
    }

    /**
     * 设置这条记录的Id
     *
     * @param id
     */
    public void setId(Long id) {
        this.sysLog.setId(id);
    }

    /**
     * @return 返回用户ID
     */
    public Long getUserId() {
        return this.sysLog.getUserId();
    }

    /**
     * 设置用户ID
     *
     * @param userId
     */
    public void setUserId(Long userId) {
        this.sysLog.setUserId(userId);
    }

    /**
     * @return 返回日志的级别
     */
    public String getLogLevel() {
        return this.sysLog.getLogLevel();
    }

    /**
     * 设置系统日志的级别
     *
     * @param logLevel
     */
    public void setLogLevel(LogLevelEnum logLevel) {
        if (null == logLevel) {
            return;
        }
        this.sysLog.setLogLevel(logLevel.toString());
    }

    /**
     * 设置系统日志的级别
     *
     * @param logLevel
     */
    public void setLogLevel(String logLevel) {
        this.sysLog.setLogLevel(logLevel);
    }

    /**
     * @return 返回提交报告的IP地址
     */
    public String getIp() {
        return this.sysLog.getIp();
    }

    /**
     * 设置提交报告的IP地址
     *
     * @param ip
     */
    public void setIp(String ip) {
        this.sysLog.setIp(ip);
    }

    /**
     * @return 返回日志的内容
     */
    public String getContent() {
        return this.sysLog.getContent();
    }

    /**
     * 设置日志的内容
     *
     * @param content
     */
    public void setContent(String content) {
        this.sysLog.setContent(content);
    }


    /**
     * @return 返回日志的参数
     */
    public String getParams() {
        return this.sysLog.getParams();
    }

    /**
     * 设置日志的参数
     *
     * @param params
     */
    public void setParams(String params) {
        this.sysLog.setParams(params);
    }

    /**
     * @return 返回日志的类型
     */
    public String getType() {
        return this.sysLog.getType();
    }

    /**
     * 设置日志的类型
     *
     * @param type
     */
    public void setType(LogTypeEnum type) {
        if (null == type) {
            return;
        }
        this.sysLog.setType(type.toString());
    }

    /**
     * 设置日志的类型
     *
     * @param type
     */
    public void setType(String type) {
        this.sysLog.setType(type);
    }

    /**
     * @return 返回爬虫程序发起请求所需的User-Agent头信息(UA)
     */
    public String getUa() {
        return this.sysLog.getUa();
    }

    /**
     * 设置爬虫程序发起请求所需的User-Agent头信息(UA)
     *
     * @param ua
     */
    public void setUa(String ua) {
        this.sysLog.setUa(ua);
    }

    /**
     * @return 返回用户的操作系统信息
     */
    public String getOs() {
        return this.sysLog.getOs();
    }

    /**
     * 设置用户操作系统信息
     *
     * @param os
     */
    public void setOs(String os) {
        this.sysLog.setOs(os);
    }

    /**
     * @return 返回浏览器信息
     */
    public String getBrowser() {
        return this.sysLog.getBrowser();
    }

    /**
     * 设置用户使用的浏览器信息
     *
     * @param browser
     */
    public void setBrowser(String browser) {
        this.sysLog.setBrowser(browser);
    }

    /**
     * @return 返回请求的链接
     */
    public String getRequestUrl() {
        return this.sysLog.getRequestUrl();
    }

    /**
     * 设置请求的链接
     *
     * @param requestUrl
     */
    public void setRequestUrl(String requestUrl) {
        this.sysLog.setRequestUrl(requestUrl);
    }

    /**
     * @return 返回HTTP Referer是header的一部分，当浏览器向web服务器发送请求的时候，一般会带上Referer，告诉服务器该网页是从哪个页面链接过来的
     */
    public String getReferer() {
        return this.sysLog.getReferer();
    }

    /**
     * 设置HTTP Referer
     *
     * @param referer
     */
    public void setReferer(String referer) {
        this.sysLog.setReferer(referer);
    }

    /**
     * @return 返回爬虫类型
     */
    public String getSpiderType() {
        return this.sysLog.getSpiderType();
    }

    /**
     * 设置爬虫类型
     *
     * @param spiderType
     */
    public void setSpiderType(String spiderType) {
        this.sysLog.setSpiderType(spiderType);
    }

    /**
     * @return 返回这条记录的创建时间
     */
    public Date getCreateTime() {
        return this.sysLog.getCreateTime();
    }

    /**
     * 设置这条记录的创建时间
     *
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.sysLog.setCreateTime(createTime);
    }

    /**
     * @return 返回这条记录的更新时间
     */
    public Date getUpdateTime() {
        return this.sysLog.getUpdateTime();
    }

    /**
     * 设置这条记录的更新时间
     *
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.sysLog.setUpdateTime(updateTime);
    }

}

