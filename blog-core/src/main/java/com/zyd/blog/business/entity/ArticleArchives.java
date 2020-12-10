package com.zyd.blog.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zyd.blog.business.entity.Article;
import com.zyd.blog.persistence.beans.BizArticleArchives;

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
public class ArticleArchives {//业务实体文章归档目录，包装了持久化中的数据实体BizArticleArchives

    private BizArticleArchives bizArticleArchives;
    private List<Article> articleList;

    /**
     * 带参数的构造函数
     *
     * @param bizArticleArchives
     */
    public ArticleArchives(BizArticleArchives bizArticleArchives) {
        this.bizArticleArchives = bizArticleArchives;
    }

    /**
     * 构造函数
     */
    public ArticleArchives() {
    }

    /**
     * @return 返回归档文章
     */
    @JsonIgnore//在json序列化时将pojo中的一些属性忽略掉，标记在属性或者方法上，返回的json数据即不包含该属性。
    public BizArticleArchives getBizArticleArchives() {
        return bizArticleArchives;
    }

    /**
     * 返回归档文章中的文章列表
     *
     * @return
     */
    public List<Article> getArticleList() {
        return articleList;
    }

    /**
     * 设置归档文章中的文章列表
     *
     * @param articleList
     */
    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    /**
     * @return 归档文章的Id
     */
    public Long getId() {
        return this.bizArticleArchives.getId();
    }

    /**
     * 设置归档文章的Id
     *
     * @param id
     */
    public void setId(Long id) {
        this.bizArticleArchives.setId(id);
    }

    /**
     * @return 归档文章的标题
     */
    public String getTitle() {
        return this.bizArticleArchives.getTitle();
    }

    /**
     * 设置归档文章的标题
     *
     * @param title
     */
    public void setTitle(String title) {
        this.bizArticleArchives.setTitle(title);
    }

    /**
     * @return 返回是否原创标识
     */
    public String getOriginal() {
        return this.bizArticleArchives.getOriginal();
    }

    /**
     * 设置原创标识
     *
     * @param original
     */
    public void setOriginal(String original) {
        this.bizArticleArchives.setOriginal(original);
    }

    /**
     * @return 返回归档时间
     */
    public String getDatetime() {
        return this.bizArticleArchives.getDatetime();
    }

    /**
     * 设置归档时间
     *
     * @param datetime
     */
    public void setDatetime(String datetime) {
        this.bizArticleArchives.setDatetime(datetime);
    }

    public String getDay() {
        String time = getDatetime();
        return time.substring(time.lastIndexOf("-") + 1);
    }
}
