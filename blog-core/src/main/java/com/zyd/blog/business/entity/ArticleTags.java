package com.zyd.blog.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zyd.blog.persistence.beans.BizArticleTags;

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
public class ArticleTags {//文章标签绑定，包装了持久化中的数据实体BizArticleTags
    private static final long serialVersionUID = 1L;
    private BizArticleTags bizArticleTags;

    /**
     * 构造函数
     */
    public ArticleTags() {
        this.bizArticleTags = new BizArticleTags();
    }

    /**
     * 带参数的构造函数
     *
     * @param bizArticleTags
     */
    public ArticleTags(BizArticleTags bizArticleTags) {
        this.bizArticleTags = bizArticleTags;
    }

    /**
     * @return 返回持久化中的数据实体BizArticleTags
     */
    @JsonIgnore//在json序列化时将pojo中的一些属性忽略掉，标记在属性或者方法上，返回的json数据即不包含该属性。
    public BizArticleTags getBizArticleTags() {
        return this.bizArticleTags;
    }

    /**
     * @return 返回这条记录的Id
     */
    public Long getId() {
        return this.bizArticleTags.getId();
    }

    /**
     * 设置这条记录的Id
     *
     * @param id
     */
    public void setId(Long id) {
        this.bizArticleTags.setId(id);
    }

    /**
     * @return 返回这条记录绑定的TagId
     */
    public long getTagId() {
        return this.bizArticleTags.getTagId();
    }

    /**
     * 设置文章标签绑定记录的TagID
     *
     * @param tagId
     */
    public void setTagId(long tagId) {
        this.bizArticleTags.setTagId(tagId);
    }

    /**
     * @return 返回文章标签绑定记录的文章ID
     */
    public long getArticleId() {
        return this.bizArticleTags.getArticleId();
    }

    /**
     * 设置文章标签绑定记录的文章ID
     *
     * @param articleId
     */
    public void setArticleId(long articleId) {
        this.bizArticleTags.setArticleId(articleId);
    }

    /**
     * @return 返回这条记录的创建时间
     */
    public Date getCreateTime() {
        return this.bizArticleTags.getCreateTime();
    }

    /**
     * 设置这条记录的创建时间
     *
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.bizArticleTags.setCreateTime(createTime);
    }

    /**
     * @return 返回这条记录的更新时间
     */
    public Date getUpdateTime() {
        return this.bizArticleTags.getUpdateTime();
    }

    /**
     * 设置这条记录的更新时间
     *
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.bizArticleTags.setUpdateTime(updateTime);
    }

}

