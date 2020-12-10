package com.zyd.blog.business.service;


import com.zyd.blog.business.entity.ArticleTags;

import java.util.List;

/**
 * 文章标签
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
public interface BizArticleTagsService {

    /**
     * 通过文章id删除文章-标签关联数据
     *
     * @param articleId 文章id
     * @return 删除数据数
     */
    int removeByArticleId(Long articleId);

    /**
     * 给文章批量添加tag
     *
     * @param tagIds 待添加的tagId集合
     * @param articleId 添加tag的文章id
     */
    void insertList(Long[] tagIds, Long articleId);

    /**
     * 保存一个实体，null的属性不会保存，会使用数据库默认值
     *
     * @param entity 待保存实体
     * @return 更新信息后的entity
     */
    ArticleTags insert(ArticleTags entity);

    /**
     * 批量插入，支持批量插入的数据库可以使用，例如MySQL,H2等，另外该接口限制实体包含id属性并且必须为自增列
     *
     * @param entities 实体集合
     */
    void insertList(List<ArticleTags> entities);
}
