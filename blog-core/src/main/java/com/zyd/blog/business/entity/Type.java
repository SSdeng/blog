package com.zyd.blog.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zyd.blog.persistence.beans.BizType;

import java.util.Date;
import java.util.List;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 *
 * 所有的实体类都包装了beans中对应的持久化中的数据实体，
 * beans中的数据实体大多都继承了AbstractDO，抽象数据对象中都有自己的id，创建时间，更新时间
 *
 */
public class Type {//业务实体分类，包装了持久化中的数据实体BizType
    private BizType bizType;

    /**
     * 构造函数
     */
    public Type() {
        this.bizType = new BizType();
    }

    /**
     * 构造函数
     *
     * @param bizType
     */
    public Type(BizType bizType) {
        this.bizType = bizType;
    }

    /**
     * @return 返回分类
     */
    @JsonIgnore//在json序列化时将pojo中的一些属性忽略掉，标记在属性或者方法上，返回的json数据即不包含该属性。
    public BizType getBizType() {
        return this.bizType;
    }

    /**
     * @return 返回分类的ID
     */
    public Long getId() {
        return this.bizType.getId();
    }

    /**
     * 设置分类的ID
     *
     * @param id
     */
    public void setId(Long id) {
        this.bizType.setId(id);
    }

    /**
     * @return 返回上一级分类的ID？
     */
    public Long getPid() {
        return this.bizType.getPid();
    }

    /**
     * 设置上一级分类的ID？
     *
     * @param pid
     */
    public void setPid(Long pid) {
        this.bizType.setPid(pid);
    }

    public Long getParentId() {
        return this.bizType.getPid();
    }

    /**
     * @return 返回分类的名称
     */
    public String getName() {
        return this.bizType.getName();
    }

    /**
     * 设置分类的名称
     *
     * @param name
     */
    public void setName(String name) {
        this.bizType.setName(name);
    }

    /**
     * @return 返回分类的说明
     */
    public String getDescription() {
        return this.bizType.getDescription();
    }

    /**
     * 设置分类的说明
     *
     * @param description
     */
    public void setDescription(String description) {
        this.bizType.setDescription(description);
    }

    /**
     * @return 返回排序结果
     */
    public Integer getSort() {
        return this.bizType.getSort();
    }

    /**
     * 设置排序
     *
     * @param sort
     */
    public void setSort(Integer sort) {
        this.bizType.setSort(sort);
    }

    /**
     * @return 返回分类是否可使用
     */
    public boolean isAvailable() {
        Boolean value = this.bizType.getAvailable();
        return value != null ? value : false;
    }

    /**
     * 设置分类的可用性
     *
     * @param available
     *
     */
    public void setAvailable(boolean available) {
        this.bizType.setAvailable(available);
    }

    /**
     * @return 返回图标
     */
    public String getIcon() {
        return this.bizType.getIcon();
    }

    /**
     * 设置图标
     *
     * @param icon
     */
    public void setIcon(String icon) {
        this.bizType.setIcon(icon);
    }

    /**
     * @return 返回分类的创建时间
     */
    public Date getCreateTime() {
        return this.bizType.getCreateTime();
    }

    /**
     * 设置分类的创建时间
     *
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.bizType.setCreateTime(createTime);
    }

    /**
     * @return 返回分类的更新时间
     */
    public Date getUpdateTime() {
        return this.bizType.getUpdateTime();
    }

    /**
     * 设置分类的更新时间
     *
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.bizType.setUpdateTime(updateTime);
    }

    /**
     * @return 返回上一级分类？
     */
    public BizType getParent() {
        return this.bizType.getParent();
    }

    /**
     * 设置上一级分类
     *
     * @param parent
     */
    public void setParent(BizType parent) {
        this.bizType.setParent(parent);
    }

    /**
     * @return 返回分类的列表
     */
    public List<BizType> getNodes() {
        return this.bizType.getNodes();
    }

    /**
     * 设置分类的列表
     *
     * @param nodes
     */
    public void setNodes(List<BizType> nodes) {
        this.bizType.setNodes(nodes);
    }

}

