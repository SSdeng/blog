package com.zyd.blog.business.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zyd.blog.business.enums.ArticleStatusEnum;
import com.zyd.blog.persistence.beans.BizArticle;
import com.zyd.blog.persistence.beans.BizTags;
import com.zyd.blog.persistence.beans.BizType;

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
 *
 */
public class Article {//业务实体：文章，包装了持久化中的数据实体bizArticle
    private BizArticle bizArticle;//持久化中的数据实体bizArticle(文章)

    /**
     * 构造方法
     */
    public Article() {
        this.bizArticle = new BizArticle();
    }

    /**
     * 带bizArticle的构造方法
     *
     * @param bizArticle
     */
    public Article(BizArticle bizArticle) {
        this.bizArticle = bizArticle;
    }

    /**
     * @return 文章数据实体bizArticle
     */
    @JsonIgnore//在json序列化时将pojo中的一些属性忽略掉，标记在属性或者方法上，返回的json数据即不包含该属性。
    public BizArticle getBizArticle() {
        return this.bizArticle;
    }

    /**
     * @return 返回文章数据实体bizArticle的ID
     */
    public Long getId() {
        return this.bizArticle.getId();
    }

    /**
     * 设置文章数据实体bizArticle的ID
     *
     * @param id
     */
    public void setId(Long id) {
        this.bizArticle.setId(id);
    }

    /**
     * @return 文章数据实体bizArticle的标题
     */
    public String getTitle() {
        return this.bizArticle.getTitle();
    }

    /**
     * @param title 设置文章数据实体bizArticle的标题
     */
    public void setTitle(String title) {
        this.bizArticle.setTitle(title);
    }

    /**
     * @return 返回文章发布者的ID
     */
    public long getUserId() {
        return this.bizArticle.getUserId();
    }

    /**
     * 设置文件发布者的ID
     *
     * @param userId
     */
    public void setUserId(long userId) {
        this.bizArticle.setUserId(userId);
    }

    /**
     * 返回文章的配图
     *
     * @return
     */
    public String getCoverImage() {
        return this.bizArticle.getCoverImage();
    }

    /**
     * 设置文件配图
     *
     * @param coverImage
     */
    public void setCoverImage(String coverImage) {
        this.bizArticle.setCoverImage(coverImage);
    }

    /**
     * @return 文章的二维码路径
     */
    public String getQrcodePath() {
        return this.bizArticle.getQrcodePath();
    }

    /**
     * 设置文章的二维码了路径
     *
     * @param qrcodePath
     */
    public void setQrcodePath(String qrcodePath) {
        this.bizArticle.setQrcodePath(qrcodePath);
    }

    /**
     * @return 返回编辑方式是不是Markdown
     */
    public boolean getIsMarkdown() {
        Boolean value = this.bizArticle.getIsMarkdown();
        return null == value ? false : value;
    }

    /**
     * 设置文章编辑方式
     *
     * @param isMarkdown
     */
    public void setIsMarkdown(boolean isMarkdown) {
        this.bizArticle.setIsMarkdown(isMarkdown);
    }

    /**
     * 获取文章内容
     *
     * @return
     */
    public String getContent() {
        return this.bizArticle.getContent();
    }

    /**
     * 设置文章内容
     *
     * @param content
     */
    public void setContent(String content) {
        this.bizArticle.setContent(content);
    }

    /**
     * @return 暂时还不知道
     */
    public String getContentMd() {
        return this.bizArticle.getContentMd();
    }

    public void setContentMd(String contentMd) {
        this.bizArticle.setContentMd(contentMd);
    }

    /**
     *
     * @return 文章是否置顶
     */
    public boolean isTop() {
        Boolean value = this.bizArticle.getTop();
        return value != null ? value : false;
    }

    /**
     * 设置文章是否置顶
     *
     * @param top
     */
    public void setTop(boolean top) {
        this.bizArticle.setTop(top);
    }


    /**
     * @return 返回文章分类Id
     */
    public Long getTypeId() {
        return this.bizArticle.getTypeId();
    }

    /**
     * 设置文章分类Id
     *
     * @param type
     */
    public void setTypeId(Long type) {
        this.bizArticle.setTypeId(type);
    }

    public ArticleStatusEnum getStatusEnum() {
        return ArticleStatusEnum.get(this.bizArticle.getStatus());
    }

    /**
     * @return 状态：发布？草稿
     */
    public Integer getStatus() {
        return this.bizArticle.getStatus();
    }

    /**
     * 设置状态：发布？草稿
     *
     * @param type
     */
    public void setStatus(Integer type) {
        this.bizArticle.setStatus(type);
    }

    /**
     * @return 获取是否推荐
     */
    public boolean getRecommended() {
        Boolean value = this.bizArticle.getRecommended();
        return value == null ? false : value;
    }

    /**
     * 设置推荐状态
     *
     * @param value
     */
    public void setRecommended(Boolean value) {
        this.bizArticle.setRecommended(value);
    }

    /**
     * @return 是否原创
     */
    public boolean isOriginal() {
        Boolean value = this.bizArticle.getOriginal();
        return value != null ? value : false;
    }

    /**
     * 设置是否原创
     *
     * @param original
     */
    public void setOriginal(boolean original) {
        this.bizArticle.setOriginal(original);
    }


    /**
     * @return 文件说明
     */
    public String getDescription() {
        return this.bizArticle.getDescription();
    }

    /**
     * 设置文章说明
     *
     * @param description
     */
    public void setDescription(String description) {
        this.bizArticle.setDescription(description);
    }

    /**
     * @return 文章关键词
     */
    public String getKeywords() {
        return this.bizArticle.getKeywords();
    }

    /**
     * 设置文章关键词
     *
     * @param keywords
     */
    public void setKeywords(String keywords) {
        this.bizArticle.setKeywords(keywords);
    }

    /**
     * @return 文章创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    public Date getCreateTime() {
        return this.bizArticle.getCreateTime();
    }

    /**
     * 设置文章创建时间
     *
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.bizArticle.setCreateTime(createTime);
    }

    /**
     * @return 返回文章更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    public Date getUpdateTime() {
        return this.bizArticle.getUpdateTime();
    }

    /**
     * 设置文章更新时间
     *
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.bizArticle.setUpdateTime(updateTime);
    }

    /**
     * @return 文章是否允许评论
     */
    public boolean isComment() {
        Boolean value = this.bizArticle.getComment();
        return value != null ? value : false;
    }

    /**
     * 设置文章是否允许评论
     *
     * @param comment
     */
    public void setComment(boolean comment) {
        this.bizArticle.setComment(comment);
    }

    /**
     * @return 返回文章标签
     */
    public List<BizTags> getTags() {
        return this.bizArticle.getTags();
    }

    /**
     * @return 返回文章分类
     */
    public BizType getType() {
        return this.bizArticle.getBizType();
    }

    /**
     * @return 返回文章观看次数
     */
    public int getLookCount(){
        Integer lookCount = this.bizArticle.getLookCount();
        return lookCount == null ? 0 : lookCount;
    }

    /**
     * @return 返回文章评论次数
     */
    public int getCommentCount(){
        Integer commentCount = this.bizArticle.getCommentCount();
        return commentCount == null ? 0 : commentCount;
    }

    /**
     * @return 返回文章喜欢次数
     */
    public int getLoveCount(){
        Integer loveCount = this.bizArticle.getLoveCount();
        return loveCount == null ? 0 : loveCount;
    }
}

