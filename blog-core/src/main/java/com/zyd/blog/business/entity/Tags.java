package com.zyd.blog.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zyd.blog.persistence.beans.BizTags;

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
 */
public class Tags {//业务实体标签，包装了持久化中的数据实体BizTags
    private BizTags bizTags;

    /**
     * 构造函数
     */
    public Tags() {
        this.bizTags = new BizTags();
    }

    /**
     * 构造函数
     *
     * @param bizTags
     */
    public Tags(BizTags bizTags) {
        this.bizTags = bizTags;
    }

    /**
     * @return 返回标签
     */
    @JsonIgnore//在json序列化时将pojo中的一些属性忽略掉，标记在属性或者方法上，返回的json数据即不包含该属性。
    public BizTags getBizTags() {
        return this.bizTags;
    }

    /**
     * @return 返回标签的ID
     */
    public Long getId() {
        return this.bizTags.getId();
    }

    /**
     * 设置标签的ID
     *
     * @param id
     */
    public void setId(Long id) {
        this.bizTags.setId(id);
    }

    /**
     * @return 返回标签的名称
     */
    public String getName() {
        return this.bizTags.getName();
    }

    /**
     * 设置标签的名称
     *
     * @param name
     */
    public void setName(String name) {
        this.bizTags.setName(name);
    }

    /**
     * @return 返回标签说明
     */
    public String getDescription() {
        return this.bizTags.getDescription();
    }

    /**
     * 设置标签说明
     *
     * @param description
     */
    public void setDescription(String description) {
        this.bizTags.setDescription(description);
    }

    /**
     * @return 返回标签的创建时间
     */
    public Date getCreateTime() {
        return this.bizTags.getCreateTime();
    }

    /**
     * 设置标签的创建时间
     *
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.bizTags.setCreateTime(createTime);
    }

    /**
     * @return 返回标签的更新时间
     */
    public Date getUpdateTime() {
        return this.bizTags.getUpdateTime();
    }

    /**
     * 设置标签的更新时间
     *
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.bizTags.setUpdateTime(updateTime);
    }

}

