package com.zyd.blog.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zyd.blog.persistence.beans.BizArticleLove;

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
public class ArticleLove {//喜欢文章记录，包装了持久化中的数据实体BizArticleLove
    private static final long serialVersionUID = 1L;
    private BizArticleLove bizArticleLove;

    /**
     * 构造函数
     */
    public ArticleLove() {
        this.bizArticleLove = new BizArticleLove();
    }

    /**
     * 带参数的构造函数
     *
     * @param bizArticleLove
     */
    public ArticleLove(BizArticleLove bizArticleLove) {
        this.bizArticleLove = bizArticleLove;
    }

    /**
     * @return 返回持久化中的数据实体BizArticleLove
     */
    @JsonIgnore//在json序列化时将pojo中的一些属性忽略掉，标记在属性或者方法上，返回的json数据即不包含该属性。
    public BizArticleLove getBizArticleLove() {
        return this.bizArticleLove;
    }

    /**
     * @return 返回这条记录的Id
     */
    public Long getId() {
        return this.bizArticleLove.getId();
    }

    /**
     * 设置这条记录的ID
     *
     * @param id
     */
    public void setId(Long id) {
        this.bizArticleLove.setId(id);
    }

    /**
     * @return 返回被喜欢文章的ID
     */
    public long getArticleId() {
        return this.bizArticleLove.getArticleId();
    }

    /**
     * 设置被喜欢文章的ID
     *
     * @param articleId
     */
    public void setArticleId(long articleId) {
        this.bizArticleLove.setArticleId(articleId);
    }

    /**
     * @return 获取用户的ID
     */
    public long getUserId() {
        return this.bizArticleLove.getUserId();
    }

    /**
     * 设置用户的ID
     *
     * @param userId
     */
    public void setUserId(long userId) {
        this.bizArticleLove.setUserId(userId);
    }

    /**
     * @return 返回用户的IP地址
     */
    public String getUserIp() {
        return this.bizArticleLove.getUserIp();
    }

    /**
     * 设置用户的IP地址
     *
     * @param userIp
     */
    public void setUserIp(String userIp) {
        this.bizArticleLove.setUserIp(userIp);
    }

    /**
     * @return 返回喜欢的时间
     */
    public Date getLoveTime() {
        return this.bizArticleLove.getLoveTime();
    }

    /**
     * 设置喜欢的时间
     *
     * @param loveTime
     */
    public void setLoveTime(Date loveTime) {
        this.bizArticleLove.setLoveTime(loveTime);
    }

    /**
     * @return 返回这条记录创建的时间
     */
    public Date getCreateTime() {
        return this.bizArticleLove.getCreateTime();
    }

    /**
     * 设置这条记录创建的时间
     *
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.bizArticleLove.setCreateTime(createTime);
    }

    /**
     * @return 返回这条消息更新的时间
     */
    public Date getUpdateTime() {
        return this.bizArticleLove.getUpdateTime();
    }

    /**
     * 设置这条消息更新的时间
     *
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.bizArticleLove.setUpdateTime(updateTime);
    }

}

