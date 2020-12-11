package com.zyd.blog.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyd.blog.business.annotation.RedisCache;
import com.zyd.blog.business.entity.Tags;
import com.zyd.blog.business.service.BizTagsService;
import com.zyd.blog.business.vo.TagsConditionVO;
import com.zyd.blog.framework.exception.ZhydException;
import com.zyd.blog.persistence.beans.BizArticleTags;
import com.zyd.blog.persistence.beans.BizTags;
import com.zyd.blog.persistence.mapper.BizArticleTagsMapper;
import com.zyd.blog.persistence.mapper.BizTagsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 标签
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Service
public class BizTagsServiceImpl implements BizTagsService {

    @Autowired
    private BizTagsMapper bizTagsMapper;
    @Autowired
    private BizArticleTagsMapper bizArticleTagsMapper;

    /**
     * 分页查询
     *
     * @param vo 搜索条件
     * @return 查询结果表
     */
    @Override
    public PageInfo<Tags> findPageBreakByCondition(TagsConditionVO vo) {
        // 分页
        PageHelper.startPage(vo.getPageNumber(), vo.getPageSize());
        // 按条件查询
        List<BizTags> list = bizTagsMapper.findPageBreakByCondition(vo);
        // 转换元素类型
        List<Tags> boList = getTags(list);
        // 空表
        if (boList == null) {
            return null;
        }
        // 封装list到pageInfo对象实现分页
        PageInfo bean = new PageInfo<BizTags>(list);
        // 将boList放入pageInfo
        bean.setList(boList);
        return bean;
    }

    /**
     * 按标签名获取标签对象
     *
     * @param name 标签名
     * @return 标签对象
     */
    @Override
    public Tags getByName(String name) {
        // 标签名为空
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        // 设定搜索条件
        BizTags tags = new BizTags();
        tags.setName(name);
        // 从数据库中搜索对应实体
        tags = bizTagsMapper.selectOne(tags);
        // 不为空返回转换类型的搜索结果
        return null == tags ? null : new Tags(tags);
    }

    /**
     * 向数据库插入标签
     *
     * @param entity 标签实体
     * @return 更新后的实体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedisCache(flush = true)
    public Tags insert(Tags entity) {
        // entity不可为空
        Assert.notNull(entity, "Tags不可为空！");
        // 数据库中有同名标签
        if (this.getByName(entity.getName()) != null) {
            throw new ZhydException("标签添加失败，标签已存在！[" + entity.getName() + "]");
        }
        // 记录创建时间
        entity.setUpdateTime(new Date());
        // 记录更新时间
        entity.setCreateTime(new Date());
        // 选择插入 用默认值填充空值
        bizTagsMapper.insertSelective(entity.getBizTags());
        return entity;
    }

    /**
     * 按主键删除标签
     *
     * @param primaryKey 主键
     * @return 删除结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedisCache(flush = true)
    public boolean removeByPrimaryKey(Long primaryKey) {
        // 设置搜索条件
        BizArticleTags articleTag = new BizArticleTags();
        articleTag.setTagId(primaryKey);
        // 在数据库中搜索
        List<BizArticleTags> articleTags = bizArticleTagsMapper.select(articleTag);
        // 存在标有该标签的文章
        if (!CollectionUtils.isEmpty(articleTags)) {
            // 禁止删除该标签
            throw new ZhydException("当前标签下存在文章信息，禁止删除！");
        }
        // 从数据库删除该标签 返回删除结果
        return bizTagsMapper.deleteByPrimaryKey(primaryKey) > 0;
    }


    /**
     * 选择更新
     * 以默认值填充空值
     *
     * @param entity 实体
     * @return 更新结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedisCache(flush = true)
    public boolean updateSelective(Tags entity) {
        // entity不可为空
        Assert.notNull(entity, "Tags不可为空！");
        // 按名字从数据库获取标签
        Tags old = this.getByName(entity.getName());
        // 数据库中有同名同id的标签
        if (old != null && !old.getId().equals(entity.getId())) {
            throw new ZhydException("标签修改失败，标签已存在！[" + entity.getName() + "]");
        }
        // 记录更新时间
        entity.setUpdateTime(new Date());
        // 更新 返回结果
        return bizTagsMapper.updateByPrimaryKeySelective(entity.getBizTags()) > 0;
    }

    /**
     * 按主键获取标签
     *
     * @param primaryKey 主键
     * @return 标签实体
     */
    @Override
    public Tags getByPrimaryKey(Long primaryKey) {
        // 主键参数不可为空
        Assert.notNull(primaryKey, "PrimaryKey不可为空！");
        // 按主键从数据库取出标签
        BizTags entity = bizTagsMapper.selectByPrimaryKey(primaryKey);
        // 不为空则转换类型后返回结果
        return null == entity ? null : new Tags(entity);
    }

    /**
     * 列出所有标签
     *
     * @return 标签列表
     */
    @Override
    @RedisCache
    public List<Tags> listAll() {
        // 取出所有标签
        List<BizTags> entityList = bizTagsMapper.selectAll();

        // 转换元素类型并返回
        return getTags(entityList);
    }

    /**
     * List<BizTags> - > List<Tags>
     *
     * @param entityList 原表
     * @return 新表
     */
    private List<Tags> getTags(List<BizTags> entityList) {
        // 原表为空
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        // 建立新表 转换类型 拷贝
        List<Tags> list = new ArrayList<>();
        for (BizTags entity : entityList) {
            list.add(new Tags(entity));
        }
        // 返回新表
        return list;
    }
}
