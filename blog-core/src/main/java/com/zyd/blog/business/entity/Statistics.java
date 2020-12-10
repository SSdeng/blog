package com.zyd.blog.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zyd.blog.persistence.beans.BizStatistics;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 *
 *
 * 所有的实体类都包装了beans中对应的持久化中的数据实体，
 * beans中的数据实体大多都继承了AbstractDO，抽象数据对象中都有自己的id，创建时间，更新时间
 */
public class Statistics {//统计，包装了持久化中的数据实体BizStatistics

    private BizStatistics bizStatistics;

    /**
     * 构造函数
     *
     * @param bizStatistics
     */
    public Statistics(BizStatistics bizStatistics) {
        this.bizStatistics = bizStatistics;
    }

    /**
     * 构造函数
     */
    public Statistics() {
    }

    /**
     * @return 返回统计
     */
    @JsonIgnore//在json序列化时将pojo中的一些属性忽略掉，标记在属性或者方法上，返回的json数据即不包含该属性。
    public BizStatistics getBizStatistics() {
        return bizStatistics;
    }

    /**
     * @return 返回统计的名称
     */
    public String getName() {
        return this.bizStatistics.getName();
    }

    /**
     * 设计统计的名称
     *
     * @param name
     */
    public void setName(String name) {
        this.bizStatistics.setName(name);
    }

    /**
     * @return 返回统计的数据
     */
    public Integer getValue() {
        return this.bizStatistics.getValue();
    }

    /**
     * 设置统计的数据
     *
     * @param value
     */
    public void setValue(Integer value) {
        this.bizStatistics.setValue(value);
    }
}
