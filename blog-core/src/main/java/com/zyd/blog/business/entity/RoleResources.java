package com.zyd.blog.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zyd.blog.persistence.beans.SysRoleResources;

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
public class RoleResources {//系统role和resources对应关系，包装了持久化中的数据实体SysRoleResources

    private SysRoleResources sysRoleResources;

    /**
     * 构造函数
     */
    public RoleResources() {
        this.sysRoleResources = new SysRoleResources();
    }

    /**
     * 构造函数
     *
     * @param sysRoleResources
     */
    public RoleResources(SysRoleResources sysRoleResources) {
        this.sysRoleResources = sysRoleResources;
    }

    /**
     * @return 返回系统role和resources对应关系
     */
    @JsonIgnore//在json序列化时将pojo中的一些属性忽略掉，标记在属性或者方法上，返回的json数据即不包含该属性。
    public SysRoleResources getSysRoleResources() {
        return this.sysRoleResources;
    }

    /**
     * @return 返回角色ID
     */
    public Long getRoleId() {
        return this.sysRoleResources.getRoleId();
    }

    /**
     * 设置角色ID
     *
     * @param roleId
     */
    public void setRoleId(Long roleId) {
        this.sysRoleResources.setRoleId(roleId);
    }

    /**
     * @return 返回资源的ID
     */
    public Long getResourcesId() {
        return this.sysRoleResources.getResourcesId();
    }

    /**
     * 设置资源的ID
     *
     * @param resourcesId
     */
    public void setResourcesId(Long resourcesId) {
        this.sysRoleResources.setResourcesId(resourcesId);
    }

    /**
     * @return 返回对应关系的创建时间
     */
    public Date getCreateTime() {
        return this.sysRoleResources.getCreateTime();
    }

    /**
     * 设置对应关系的创建时间
     *
     * @param regTime
     */
    public void setCreateTime(Date regTime) {
        this.sysRoleResources.setCreateTime(regTime);
    }

    /**
     * @return 返回对应关系更新时间
     */
    public Date getUpdateTime() {
        return this.sysRoleResources.getUpdateTime();
    }

    /**
     * 设置对应关系的更新时间
     *
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.sysRoleResources.setUpdateTime(updateTime);
    }

}
