package com.zyd.blog.business.service.impl;

import com.zyd.blog.business.entity.ArticleTags;
import com.zyd.blog.business.service.BizArticleTagsService;
import com.zyd.blog.persistence.beans.BizArticleTags;
import com.zyd.blog.persistence.mapper.BizArticleTagsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
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
@Service
public class BizArticleTagsServiceImpl implements BizArticleTagsService {

    @Autowired
    private BizArticleTagsMapper bizArticleTagsMapper;

    /**
     * 通过文章id删除文章-标签关联数据
     *
     * @param articleId 文章id
     * @return 删除数据数
     */
    @Override
    public int removeByArticleId(Long articleId) {
        // 删除 文章-标签关联数据
        // 设定条件
        Example loveExample = new Example(BizArticleTags.class);
        Example.Criteria loveCriteria = loveExample.createCriteria();
        // where articleId = 'articleId'
        loveCriteria.andEqualTo("articleId", articleId);
        // 执行删除操作并返回删除的行数
        return bizArticleTagsMapper.deleteByExample(loveExample);
    }

    /**
     * 给文章批量添加tag
     *
     * @param tagIds 待添加的tagId集合
     * @param articleId 添加tag的文章id
     */
    @Override
    public void insertList(Long[] tagIds, Long articleId) {
        // tagIds为空
        if (tagIds == null || tagIds.length == 0) {
            return;
        }
        // 创建存储文章tag的列表
        List<ArticleTags> list = new ArrayList<>();
        ArticleTags articleTags = null;
        // 遍历tagIds
        for (Long tagId : tagIds) {
            // 根据tagId创建对象
            articleTags = new ArticleTags();
            // 记录TagId
            articleTags.setTagId(tagId);
            // 记录该Tag对应articleId
            articleTags.setArticleId(articleId);
            // 加入表中
            list.add(articleTags);
        }
        // 将表插入数据库
        this.insertList(list);
    }

    /**
     * 保存一个实体，null的属性不会保存，会使用数据库默认值
     *
     * @param entity 待保存实体
     * @return 更新信息后的entity
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArticleTags insert(ArticleTags entity) {
        // entity不可为空
        Assert.notNull(entity, "ArticleTags不可为空！");
        // 记录更新时间
        entity.setUpdateTime(new Date());
        // 记录创建时间
        entity.setCreateTime(new Date());
        // 将实体插入数据库
        bizArticleTagsMapper.insertSelective(entity.getBizArticleTags());
        return entity;
    }

    /**
     * 批量插入，支持批量插入的数据库可以使用，例如MySQL,H2等，另外该接口限制实体包含id属性并且必须为自增列
     *
     * @param entities 实体集合
     */
    @Override
    public void insertList(List<ArticleTags> entities) {
        // 参数不可为空
        Assert.notNull(entities, "ArticleTags不可为空！");
        // 创建插入数据库的对象
        List<BizArticleTags> list = new ArrayList<>();
        // 遍历entities
        for (ArticleTags entity : entities) {
            // 设置更新时间
            entity.setUpdateTime(new Date());
            // 设置创建时间
            entity.setCreateTime(new Date());
            // 加入列表
            list.add(entity.getBizArticleTags());
        }
        // 将表插入数据库
        bizArticleTagsMapper.insertList(list);
    }
}
