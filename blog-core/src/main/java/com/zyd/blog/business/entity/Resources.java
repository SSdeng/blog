package com.zyd.blog.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zyd.blog.business.enums.ResourceTypeEnum;
import com.zyd.blog.persistence.beans.SysResources;

import java.util.Date;
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
 */
public class Resources {//业务实体资源，包装了持久化中的数据实体SysResources
    private SysResources sysResources;

    /**
     * 构造函数
     */
    public Resources() {
        this.sysResources = new SysResources();
    }

    /**
     * 构造函数
     *
     * @param sysResources
     */
    public Resources(SysResources sysResources) {
        this.sysResources = sysResources;
    }

    /**
     * @return 返回资源
     */
    @JsonIgnore//在json序列化时将pojo中的一些属性忽略掉，标记在属性或者方法上，返回的json数据即不包含该属性。
    public SysResources getSysResources() {
        return this.sysResources;
    }

    /**
     * @return 返回业务实体的Id
     */
    public Long getId() {
        return this.sysResources.getId();
    }

    /**
     * 设置这个业务实体的Id
     *
     * @param id
     */
    public void setId(Long id) {
        this.sysResources.setId(id);
    }

    /**
     * @return 返回资源的名称
     */
    public String getName() {
        return this.sysResources.getName();
    }

    /**
     * 设置资源的名称
     *
     * @param name
     */
    public void setName(String name) {
        this.sysResources.setName(name);
    }

    /**
     * @return 返回资源的类型
     */
    public ResourceTypeEnum getType() {
        return this.sysResources.getType() != null ? ResourceTypeEnum.valueOf(this.sysResources.getType()) : null;
    }

    /**
     * 设置资源的类型
     *
     * @param type
     */
    public void setType(ResourceTypeEnum type) {
        this.sysResources.setType(type.toString());
    }

    /**
     * @return 返回资源的url
     */
    public String getUrl() {
        return this.sysResources.getUrl();
    }

    /**
     * 设置资源的Url
     *
     * @param url
     */
    public void setUrl(String url) {
        this.sysResources.setUrl(url);
    }

    /**
     * @return 返回访问资源的许可
     */
    public String getPermission() {
        return this.sysResources.getPermission();
    }

    /**
     * 设置访问资源的许可
     *
     * @param permission
     */
    public void setPermission(String permission) {
        this.sysResources.setPermission(permission);
    }

    /**
     * @return 返回上一级资源的Id
     */
    public Long getParentId() {
        return this.sysResources.getParentId();
    }

    /**
     * 设置上一级资源的Id
     *
     * @param parentId
     */
    public void setParentId(Long parentId) {
        this.sysResources.setParentId(parentId);
    }

    /**
     * @return 返回排序结果
     */
    public Integer getSort() {
        return this.sysResources.getSort();
    }

    /**
     * 设置排序
     *
     * @param sort
     */
    public void setSort(Integer sort) {
        this.sysResources.setSort(sort);
    }

    /**
     * @return 返回资源的可用性
     */
    public boolean isAvailable() {
        Boolean value = this.sysResources.getAvailable();
        return value != null ? value : false;
    }

    /**
     * 设置资源的可用性
     *
     * @param available
     */
    public void setAvailable(boolean available) {
        this.sysResources.setAvailable(available);
    }

    /**
     * @return 返回资源的扩展
     */
    public Boolean getExternal() {
        Boolean value = this.sysResources.getExternal();
        return null == value ? false : value;
    }

    /**
     * 设置资源的扩展性
     *
     * @param external
     */
    public void setExternal(Boolean external) {
        this.sysResources.setExternal(external);
    }

    /**
     * @return 返回资源的图标
     */
    public String getIcon() {
        return this.sysResources.getIcon();
    }

    /**
     * 设置资源的图标
     *
     * @param icon
     */
    public void setIcon(String icon) {
        this.sysResources.setIcon(icon);
    }

    /**
     * @return 返回资源创建的时间
     */
    public Date getCreateTime() {
        return this.sysResources.getCreateTime();
    }

    /**
     * 设置资源创建的时间
     *
     * @param regTime
     */
    public void setCreateTime(Date regTime) {
        this.sysResources.setCreateTime(regTime);
    }

    /**
     * @return 返回资源更新的时间
     */
    public Date getUpdateTime() {
        return this.sysResources.getUpdateTime();
    }

    /**
     * 设置资源更新的时间
     *
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.sysResources.setUpdateTime(updateTime);
    }

    /**
     * @return 返回上一级资源
     */
    public SysResources getParent() {
        return this.sysResources.getParent();
    }

    /**
     * 设置上一级资源
     *
     * @param parent
     */
    public void setParent(SysResources parent) {
        this.sysResources.setParent(parent);
    }

    /**
     * @return 获取资源列表
     */
    public List<SysResources> getNodes() {
        return this.sysResources.getNodes();
    }

    /**
     * 设置资源列表
     *
     * @param nodes
     */
    public void setNodes(List<SysResources> nodes) {
        this.sysResources.setNodes(nodes);
    }
}

