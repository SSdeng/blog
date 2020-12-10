package com.zyd.blog.business.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zyd.blog.business.consts.DateConst;
import com.zyd.blog.persistence.beans.SysUpdateRecorde;
import org.springframework.format.annotation.DateTimeFormat;

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
public class UpdateRecorde {//业务实体更新记录，包装了持久化中的数据实体SysUpdateRecorde
    private SysUpdateRecorde sysUpdateRecorde;

    /**
     * 构造函数
     */
    public UpdateRecorde() {
        this.sysUpdateRecorde = new SysUpdateRecorde();
    }

    /**
     * 构造函数
     *
     * @param sysUpdateRecorde
     */
    public UpdateRecorde(SysUpdateRecorde sysUpdateRecorde) {
        this.sysUpdateRecorde = sysUpdateRecorde;
    }

    /**
     * @return 返回更新记录
     */
    @JsonIgnore//在json序列化时将pojo中的一些属性忽略掉，标记在属性或者方法上，返回的json数据即不包含该属性。
    public SysUpdateRecorde getSysUpdateRecorde() {
        return this.sysUpdateRecorde;
    }

    /**
     * @return 返回更新记录的ID
     */
    public Long getId() {
        return this.sysUpdateRecorde.getId();
    }

    /**
     * 设置更新记录的ID
     *
     * @param id
     */
    public void setId(Long id) {
        this.sysUpdateRecorde.setId(id);
    }

    /**
     * @return 返回更新记录的版本
     */
    public String getVersion() {
        return this.sysUpdateRecorde.getVersion();
    }

    /**
     * 设置更新记录的版本
     *
     * @param version
     */
    public void setVersion(String version) {
        this.sysUpdateRecorde.setVersion(version);
    }

    /**
     * @return 返回更新的说明
     */
    public String getDescription() {
        return this.sysUpdateRecorde.getDescription();
    }

    /**
     * 设置更新的说明
     *
     * @param description
     */
    public void setDescription(String description) {
        this.sysUpdateRecorde.setDescription(description);
    }

    /**
     * @return 返回更新的时间
     *
     * JsonFormat注解是一个时间格式化注解
     */
    @JsonFormat(timezone = "GMT+8", pattern = DateConst.YYYY_MM_DD_HH_MM_SS_EN)
    public Date getRecordeTime() {
        return this.sysUpdateRecorde.getRecordeTime();
    }

    /**
     * 设置更新的时间
     *
     * @param recordeTime
     */
    @DateTimeFormat(pattern = DateConst.YYYY_MM_DD_HH_MM_SS_EN)
    public void setRecordeTime(Date recordeTime) {
        this.sysUpdateRecorde.setRecordeTime(recordeTime);
    }

    /**
     * @return 返回更新记录创建的时间
     */
    public Date getCreateTime() {
        return this.sysUpdateRecorde.getCreateTime();
    }

    /**
     * 设置更新记录创建的时间
     *
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.sysUpdateRecorde.setCreateTime(createTime);
    }

    /**
     * @return 返回更新记录更新的时间
     */
    public Date getUpdateTime() {
        return this.sysUpdateRecorde.getUpdateTime();
    }

    /**
     * 设置更新记录更新的时间
     *
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.sysUpdateRecorde.setUpdateTime(updateTime);
    }

}

