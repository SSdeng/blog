package com.zyd.blog.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyd.blog.business.entity.Template;
import com.zyd.blog.business.enums.TemplateKeyEnum;
import com.zyd.blog.business.service.SysTemplateService;
import com.zyd.blog.business.vo.TemplateConditionVO;
import com.zyd.blog.persistence.beans.SysTemplate;
import com.zyd.blog.persistence.mapper.SysTemplateMapper;
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
 * 模板服务实现类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Service
public class SysTemplateServiceImpl implements SysTemplateService {

    @Autowired
    private SysTemplateMapper sysTemplateMapper;

    /**
     * 分页查询
     * 使用PageHelper开源项目实现分页
     * @param vo 模板条件VO，包含分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<Template> findPageBreakByCondition(TemplateConditionVO vo) {
        // 设置分页参数，开启分页
        PageHelper.startPage(vo.getPageNumber(), vo.getPageSize());
        // 分页查询结果
        List<SysTemplate> list = sysTemplateMapper.findPageBreakByCondition(vo);
        // 判断是否为null
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        // 非空，则转换为业务实体
        List<Template> boList = new ArrayList<>();
        for (SysTemplate sysTemplate : list) {
            boList.add(new Template(sysTemplate));
        }
        PageInfo bean = new PageInfo<SysTemplate>(list);
        bean.setList(boList);
        return bean;
    }

    /**
     * 通过key(TemplateKeyEnum)获取模板信息
     *
     * @param key key
     * @return 模板
     */
    @Override
    public Template getTemplate(TemplateKeyEnum key) {
        // 调用重载方法实现
        return getTemplate(key.toString());
    }

    /**
     * 通过key(String)获取模板信息
     * @param key key
     * @return 模板
     */
    @Override
    public Template getTemplate(String key) {
        // 若key为空，返回null
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        // 非空，设置key，再调用SysTemplateMapper操作数据库
        SysTemplate entity = new SysTemplate();
        entity.setRefKey(key);
        entity = this.sysTemplateMapper.selectOne(entity);
        // 判断返回的模板是否非空，并转换为业务实体
        return null == entity ? null : new Template(entity);
    }

    /**
     * 保存一个模板实体，null的属性不会保存，会使用数据库默认值
     *
     * @param entity 模板实体
     * @return 模板
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Template insert(Template entity) {
        // 验证模板非空
        Assert.notNull(entity, "Template不可为空！");
        // 设置更新和创建时间
        entity.setUpdateTime(new Date());
        entity.setCreateTime(new Date());
        // 调用SysTemplateMapper操作数据库
        sysTemplateMapper.insertSelective(entity.getSysTemplate());
        return entity;
    }

    /**
     * 删除指定主键模板
     * @param primaryKey 主键
     * @return 删除成功/失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByPrimaryKey(Long primaryKey) {
        // 调用SysTemplateMapper操作数据库
        return sysTemplateMapper.deleteByPrimaryKey(primaryKey) > 0;
    }

    /**
     * 更新模板（若传入的某个字段为null值，则不更新该字段）
     * @param entity 待更新模板
     * @return 更新成功/失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSelective(Template entity) {
        // 验证模板非空
        Assert.notNull(entity, "Template不可为空！");
        // 设置更新时间
        entity.setUpdateTime(new Date());
        // 调用SysTemplateMapper操作数据库
        return sysTemplateMapper.updateByPrimaryKeySelective(entity.getSysTemplate()) > 0;
    }

    /**
     * 获取指定主键模板
     * @param primaryKey 主键
     * @return 模板
     */
    @Override
    public Template getByPrimaryKey(Long primaryKey) {
        // 验证主键非空
        Assert.notNull(primaryKey, "PrimaryKey不可为空！");
        // 调用SysTemplateMapper操作数据库
        SysTemplate entity = sysTemplateMapper.selectByPrimaryKey(primaryKey);
        // 判断得到的模板数据实体非空，并转换为业务实体
        return null == entity ? null : new Template(entity);
    }

    /**
     * 列出所有模板
     * @return 模板列表
     */
    @Override
    public List<Template> listAll() {
        // 调用SysTemplateMapper操作数据库
        List<SysTemplate> entityList = sysTemplateMapper.selectAll();
        // 若列表为空，返回null
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        // 非空，构建列表，并转换为业务实体
        List<Template> list = new ArrayList<>();
        for (SysTemplate entity : entityList) {
            list.add(new Template(entity));
        }
        return list;
    }
}
