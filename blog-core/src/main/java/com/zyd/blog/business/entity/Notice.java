package com.zyd.blog.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zyd.blog.business.enums.NoticeStatusEnum;
import com.zyd.blog.persistence.beans.SysNotice;

import java.util.Date;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @website https://www.zhyd.me
 * @version 1.0
 * @date 2018/4/16 16:26
 * @since 1.0
 *
 *
 * 所有的实体类都包装了beans中对应的持久化中的数据实体，
 * beans中的数据实体大多都继承了AbstractDO，抽象数据对象中都有自己的id，创建时间，更新时间
 */
public class Notice {//业务实体公告通知，包装了持久化中的数据实体SysNotice
    private SysNotice sysNotice;

    /**
     * 构造函数
     */
    public Notice() {
        this.sysNotice = new SysNotice();
    }

    /**
     * 构造函数
     *
     * @param sysNotice
     */
    public Notice(SysNotice sysNotice) {
        this.sysNotice = sysNotice;
    }

    /**
     * @return 返回公告通知
     */
    @JsonIgnore//在json序列化时将pojo中的一些属性忽略掉，标记在属性或者方法上，返回的json数据即不包含该属性。
    public SysNotice getSysNotice() {
        return this.sysNotice;
    }

    /**
     * @return 返回这条记录的Id
     */
    public Long getId() {
        return this.sysNotice.getId();
    }

    /**
     * 设置这条记录的Id
     *
     * @param id
     */
    public void setId(Long id) {
        this.sysNotice.setId(id);
    }

    /**
     * @return 返回用户的Id
     */
    public long getUserId() {
        return this.sysNotice.getUserId();
    }

    /**
     * 设置用户的Id
     *
     * @param userId
     */
    public void setUserId(long userId) {
        this.sysNotice.setUserId(userId);
    }

    /**
     * @return 返回公告通知的状态
     */
    public String getStatus() {
        return this.sysNotice.getStatus();
    }

    /**
     * @return 返回公告通知状态的枚举类型
     */
    public NoticeStatusEnum getStatusEnum() {
        return NoticeStatusEnum.valueOf(this.sysNotice.getStatus());
    }

    /**
     * 设置公知通告的状态
     *
     * @param status
     */
    public void setStatus(String status) {
        this.sysNotice.setStatus(status);
    }

    /**
     * @return 返回公告通知的标题
     */
    public String getTitle() {
        return this.sysNotice.getTitle();
    }

    /**
     * 设置公告通知的标题
     *
     * @param title
     */
    public void setTitle(String title) {
        this.sysNotice.setTitle(title);
    }

    /**
     * @return 返回公告通知的内容
     */
    public String getContent() {
        return this.sysNotice.getContent();
    }

    /**
     *设置公告通知的内容
     *
     * @param content
     */
    public void setContent(String content) {
        this.sysNotice.setContent(content);
    }

    /**
     * @return 返回业务实体创建的时间
     */
    public Date getCreateTime() {
        return this.sysNotice.getCreateTime();
    }

    /**
     * 设置业务实体创建的时间
     *
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.sysNotice.setCreateTime(createTime);
    }

    /**
     * @return 返回业务实体更新的时间
     */
    public Date getUpdateTime() {
        return this.sysNotice.getUpdateTime();
    }

    /**
     * 设置业务实体更新的时间
     *
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.sysNotice.setUpdateTime(updateTime);
    }

}

