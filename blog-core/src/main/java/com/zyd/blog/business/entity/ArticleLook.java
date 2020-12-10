package com.zyd.blog.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zyd.blog.persistence.beans.BizArticleLook;

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
public class ArticleLook {//文章查看记录，包装了持久化中的数据实体bizArticleLook
    private static final long serialVersionUID = 1L;
    private BizArticleLook bizArticleLook;


    /**
     * 构造函数
     */
    public ArticleLook() {
        this.bizArticleLook = new BizArticleLook();
    }

    /**
     * 带参数的构造函数
     *
     * @param bizArticleLook
     */
    public ArticleLook(BizArticleLook bizArticleLook) {
        this.bizArticleLook = bizArticleLook;
    }

    /**
     * @return 返回bizArticleLook
     */
    @JsonIgnore//在json序列化时将pojo中的一些属性忽略掉，标记在属性或者方法上，返回的json数据即不包含该属性。
    public BizArticleLook getBizArticleLook() {
        return this.bizArticleLook;
    }

    /**
     * @return 返回文章查看记录的Id
     */
    public Long getId() {
        return this.bizArticleLook.getId();
    }

    /**
     * 设置文章记录的Id
     *
     * @param id
     */
    public void setId(Long id) {
        this.bizArticleLook.setId(id);
    }

    /**
     * @return 返回被查看文章的Id
     */
    public long getArticleId() {
        return this.bizArticleLook.getArticleId();
    }

    /**
     * 设置被查看文章的Id
     *
     * @param articleId
     */
    public void setArticleId(Long articleId) {
        this.bizArticleLook.setArticleId(articleId);
    }

    /**
     * @return 获取文章查看者的ID
     */
    public Long getUserId() {
        return this.bizArticleLook.getUserId();
    }

    /**
     * 设置文章查看者的Id
     *
     * @param userId
     */
    public void setUserId(long userId) {
        this.bizArticleLook.setUserId(userId);
    }

    /**
     * @return 返会查看者的IP地址
     */
    public String getUserIp() {
        return this.bizArticleLook.getUserIp();
    }

    /**
     * 设置文章查看者的IP地址
     *
     * @param userIp
     */
    public void setUserIp(String userIp) {
        this.bizArticleLook.setUserIp(userIp);
    }

    /**
     * @return 返回查看时间
     */
    public Date getLookTime() {
        return this.bizArticleLook.getLookTime();
    }

    /**
     * 设置查看时间
     *
     * @param lookTime
     */
    public void setLookTime(Date lookTime) {
        this.bizArticleLook.setLookTime(lookTime);
    }

    /**
     * @return 返回文章记录的创建时间
     */
    public Date getCreateTime() {
        return this.bizArticleLook.getCreateTime();
    }

    /**
     * 设置文章查看记录的时间
     *
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.bizArticleLook.setCreateTime(createTime);
    }

    /**
     * @return 返回文件查看记录的更新时间
     */
    public Date getUpdateTime() {
        return this.bizArticleLook.getUpdateTime();
    }

    /**
     * 更新文章查看记录的更新时间
     *
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.bizArticleLook.setUpdateTime(updateTime);
    }

}

