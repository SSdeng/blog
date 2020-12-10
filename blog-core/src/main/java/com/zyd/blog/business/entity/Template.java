package com.zyd.blog.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zyd.blog.persistence.beans.SysTemplate;

import java.util.Date;

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
 */
public class Template {//业务实体模版，包装了持久化中的数据实体SysTemplate
    private SysTemplate sysTemplate;

    /**
     * 构造函数
     */
    public Template() {
        this.sysTemplate = new SysTemplate();
    }

    /**
     * 构造函数
     *
     * @param sysTemplate
     */
    public Template(SysTemplate sysTemplate) {
        this.sysTemplate = sysTemplate;
    }

    /**
     * @return 返回模版
     */
    @JsonIgnore//在json序列化时将pojo中的一些属性忽略掉，标记在属性或者方法上，返回的json数据即不包含该属性。
    public SysTemplate getSysTemplate() {
        return this.sysTemplate;
    }

    /**
     * @return 返回模版ID
     */
    public Long getId() {
        return this.sysTemplate.getId();
    }

    /**
     * 设置模版ID
     *
     * @param id
     */
    public void setId(Long id) {
        this.sysTemplate.setId(id);
    }

    /**
     * @return 返回模版Key
     */
    public String getRefKey() {
        return this.sysTemplate.getRefKey();
    }

    /**
     * 设置模版Key
     *
     * @param refKey
     */
    public void setRefKey(String refKey) {
        this.sysTemplate.setRefKey(refKey);
    }

    /**
     * @return 返回对应Key的值
     */
    public String getRefValue() {
        return this.sysTemplate.getRefValue();
    }

    /**
     * 设置对应key的值
     *
     * @param refValue
     */
    public void setRefValue(String refValue) {
        this.sysTemplate.setRefValue(refValue);
    }

    /**
     * @return 返回模版的创建时间
     */
    public Date getCreateTime() {
        return this.sysTemplate.getCreateTime();
    }

    /**
     * 设置模版的创建时间
     *
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.sysTemplate.setCreateTime(createTime);
    }

    /**
     * @return 返回模版的更新时间
     */
    public Date getUpdateTime() {
        return this.sysTemplate.getUpdateTime();
    }

    /**
     * 设置模版的更新时间
     *
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.sysTemplate.setUpdateTime(updateTime);
    }

}

