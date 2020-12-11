package com.zyd.blog.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyd.blog.business.entity.Type;
import com.zyd.blog.business.service.BizTypeService;
import com.zyd.blog.business.vo.TypeConditionVO;
import com.zyd.blog.framework.exception.ZhydException;
import com.zyd.blog.persistence.beans.BizArticle;
import com.zyd.blog.persistence.beans.BizType;
import com.zyd.blog.persistence.mapper.BizArticleMapper;
import com.zyd.blog.persistence.mapper.BizTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 分类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Service
public class BizTypeServiceImpl implements BizTypeService {

    @Autowired
    private BizTypeMapper bizTypeMapper;
    @Autowired
    private BizArticleMapper bizArticleMapper;

    /**
     * 分页查询
     *
     * @param vo 查询条件
     * @return 符合条件的对象列表
     */
    @Override
    public PageInfo<Type> findPageBreakByCondition(TypeConditionVO vo) {
        // 分页
        PageHelper.startPage(vo.getPageNumber(), vo.getPageSize());
        // 按条件搜索
        List<BizType> list = bizTypeMapper.findPageBreakByCondition(vo);
        // 转换元素类型
        List<Type> boList = getTypes(list);
        // 空表
        if (boList == null) {
            return null;
        }
        // 封装list到pageInfo对象实现分页
        PageInfo bean = new PageInfo<BizType>(list);
        // 将boList放入pageInfo
        bean.setList(boList);
        return bean;
    }

    /**
     * 查询Parent
     *
     * @return Parent表
     */
    @Override
    public List<Type> listParent() {
        // 从数据库取出Parent表
        List<BizType> list = bizTypeMapper.listParent();
        // 转换元素类型并返回
        return getTypes(list);
    }

    /**
     * 获取类型菜单
     *
     * @return 类型菜单表
     */
    @Override
    public List<Type> listTypeForMenu() {
        // 搜索条件设定类
        TypeConditionVO vo = new TypeConditionVO();
        // 设定分页起始页
        vo.setPageNumber(1);
        // 设定分页大小
        vo.setPageSize(100);
        // 分页
        PageHelper.startPage(vo.getPageNumber(), vo.getPageSize());
        // 获取类型菜单
        List<BizType> entityList = bizTypeMapper.listTypeForMenu();
        // 转换类型并返回
        return getTypes(entityList);
    }

    /**
     * List<BizType> -> List<Type>
     *
     * @param list 原表
     * @return 新表
     */
    private List<Type> getTypes(List<BizType> list) {
        // 空表
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        // 创建新表
        List<Type> boList = new ArrayList<>();
        // 靠别
        for (BizType bizType : list) {
            // 转换类型 加入表
            boList.add(new Type(bizType));
        }
        // 返回新表
        return boList;
    }

    /**
     * 将Type插入数据库
     *
     * @param entity 实体
     * @return 更新后的实体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Type insert(Type entity) {
        // entity不可为空
        Assert.notNull(entity, "Type不可为空！");
        // 记录更新时间
        entity.setUpdateTime(new Date());
        // 记录创建时间
        entity.setCreateTime(new Date());
        // 插入数据库 用默认值填充空值
        bizTypeMapper.insertSelective(entity.getBizType());
        return entity;
    }

    /**
     * 按主键删除
     *
     * @param primaryKey 主键
     * @return 删除结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByPrimaryKey(Long primaryKey) {
        // 作为搜索模板
        BizArticle article = new BizArticle();
        // 类型为待删除Type的文章
        article.setTypeId(primaryKey);
        // 在数据库中搜索属于该分类的文章
        List<BizArticle> articles = bizArticleMapper.select(article);
        // 有文章属于该分类
        if (!CollectionUtils.isEmpty(articles)) {
            // 禁止删除
            throw new ZhydException("当前分类下存在文章信息，禁止删除！");
        }
        return bizTypeMapper.deleteByPrimaryKey(primaryKey) > 0;
    }

    /**
     * 选择更新
     * 用默认值填充空值
     *
     * @param entity 实体
     * @return 更新结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSelective(Type entity) {
        // entity不可为空
        Assert.notNull(entity, "Type不可为空！");
        // 记录更新时间
        entity.setUpdateTime(new Date());
        // 按主键更新 返回结果
        return bizTypeMapper.updateByPrimaryKey(entity.getBizType()) > 0;
    }

    /**
     * 按主键获取对象
     *
     * @param primaryKey 主键
     * @return 实体
     */
    @Override
    public Type getByPrimaryKey(Long primaryKey) {
        // 主键参数不可为空
        Assert.notNull(primaryKey, "PrimaryKey不可为空！");
        // 按主键在数据库中查找
        BizType entity = bizTypeMapper.selectByPrimaryKey(primaryKey);
        // 不为空则转换类型后返回
        return null == entity ? null : new Type(entity);
    }

    /**
     * 列出所有分类
     *
     * @return 分类类型表
     */
    @Override
    public List<Type> listAll() {
        // 搜索模板
        TypeConditionVO vo = new TypeConditionVO();
        // 分页起始页
        vo.setPageNumber(1);
        // 分页大小
        vo.setPageSize(100);
        // 分页
        PageHelper.startPage(vo.getPageNumber(), vo.getPageSize());
        // 获取所有分类
        List<BizType> entityList = bizTypeMapper.selectAll();

        // 转换元素类型
        List<Type> list = getTypes(entityList);
        // 空表
        if (list == null) {
            return null;
        }
        // 返回表
        return list;
    }
}
