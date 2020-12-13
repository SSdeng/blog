package com.zyd.blog.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zyd.blog.persistence.beans.SysRole;

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
public class Role {//用户角色，包装了持久化中的数据实体SysRole
    private SysRole sysRole;

    /**
     * 构造函数
     */
    public Role() {
        this.sysRole = new SysRole();
    }

    /**
     * 构造函数
     *
     * @param sysRole
     */
    public Role(SysRole sysRole) {
        this.sysRole = sysRole;
    }

    /**
     * @return 返回用户角色
     */
    @JsonIgnore//在json序列化时将pojo中的一些属性忽略掉，标记在属性或者方法上，返回的json数据即不包含该属性。
    public SysRole getSysRole() {
        return this.sysRole;
    }

    /**
     * @return 返回角色ID
     */
    public Long getId() {
        return this.sysRole.getId();
    }

    /**
     * 设置角色ID
     *
     * @param id
     */
    public void setId(Long id) {
        this.sysRole.setId(id);
    }

    /**
     * @return 返回角色名称
     */
    public String getName() {
        return this.sysRole.getName();
    }

    /**
     * 设置角色名称
     *
     * @param name
     */
    public void setName(String name) {
        this.sysRole.setName(name);
    }


    /**
     * @return 返回角色描述
     */
    public String getDescription() {
        return this.sysRole.getDescription();
    }

    /**
     * 设置角色描述
     *
     * @param description
     */
    public void setDescription(String description) {
        this.sysRole.setDescription(description);
    }

    /**
     * @return 返回角色是否有效
     */
    public boolean isAvailable() {
        Boolean value = this.sysRole.getAvailable();
        if(value != null){
            return value;
        }else{
            return false;
        }
    }

    /**
     * 设置角色的有效性
     *
     * @param available
     */
    public void setAvailable(boolean available) {
        this.sysRole.setAvailable(available);
    }

    /**
     * @return 返回角色的创建时间
     */
    public Date getCreateTime() {
        return this.sysRole.getCreateTime();
    }

    /**
     * 设置角色的创建时间
     *
     * @param regTime
     */
    public void setCreateTime(Date regTime) {
        this.sysRole.setCreateTime(regTime);
    }

    /**
     * @return 返回角色的更新时间
     */
    public Date getUpdateTime() {
        return this.sysRole.getUpdateTime();
    }

    /**
     * 设置角色的更新时间
     *
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.sysRole.setUpdateTime(updateTime);
    }

}

