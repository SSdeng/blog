package com.zyd.blog.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zyd.blog.persistence.beans.SysUserRole;

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
public class UserRole {//业务实体用户与用户角色的关联关系，，包装了持久化中的数据实体SysUserRole

    private SysUserRole sysUserRole;

    /**
     * 构造函数
     */
    public UserRole() {
        this.sysUserRole = new SysUserRole();
    }

    /**
     * 构造函数
     *
     * @param sysUserRole
     */
    public UserRole(SysUserRole sysUserRole) {
        this.sysUserRole = sysUserRole;
    }

    /**
     * @return 返回用户角色
     */
    @JsonIgnore//在json序列化时将pojo中的一些属性忽略掉，标记在属性或者方法上，返回的json数据即不包含该属性。
    public SysUserRole getSysUserRole() {
        return this.sysUserRole;
    }

    /**
     * @return 返回用户ID
     */
    public Long getUserId() {
        return this.sysUserRole.getUserId();
    }

    /**
     * 设置用户ID
     *
     * @param userId
     */
    public void setUserId(Long userId) {
        this.sysUserRole.setUserId(userId);
    }

    /**
     * @return 返回角色ID
     */
    public Long getRoleId() {
        return this.sysUserRole.getRoleId();
    }

    /**
     * 设置角色ID
     *
     * @param roleId
     */
    public void setRoleId(Long roleId) {
        this.sysUserRole.setRoleId(roleId);
    }

    /**
     * @return 关系的创建时间
     */
    public Date getCreateTime() {
        return this.sysUserRole.getCreateTime();
    }

    /**
     * 设置关系的创建时间
     *
     * @param regTime
     */
    public void setCreateTime(Date regTime) {
        this.sysUserRole.setCreateTime(regTime);
    }

    /**
     * @return 返回关系的更新时间
     */
    public Date getUpdateTime() {
        return this.sysUserRole.getUpdateTime();
    }

    /**
     * 设置关系的更新时间
     *
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.sysUserRole.setUpdateTime(updateTime);
    }
}
