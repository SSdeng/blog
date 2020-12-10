package com.zyd.blog.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyd.blog.business.entity.UpdateRecorde;
import com.zyd.blog.business.service.SysUpdateRecordeService;
import com.zyd.blog.business.vo.UpdateRecordeConditionVO;
import com.zyd.blog.persistence.beans.SysUpdateRecorde;
import com.zyd.blog.persistence.mapper.SysUpdateRecordeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 更新记录服务实现类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Service
public class SysUpdateRecordeServiceImpl implements SysUpdateRecordeService{

    @Autowired
    private SysUpdateRecordeMapper sysUpdateRecordeMapper;

    /**
     * 分页查询更新记录
     * 使用PageHelper开源项目实现分页
     * @param vo 更新记录条件VO，包含分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<UpdateRecorde>findPageBreakByCondition(UpdateRecordeConditionVO vo){
        // 设置分页参数，开启分页
        PageHelper.startPage(vo.getPageNumber(),vo.getPageSize());
        // 紧跟着的第一个数据查询会进行分页
        List<SysUpdateRecorde>list=sysUpdateRecordeMapper.findPageBreakByCondition(vo);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<UpdateRecorde> boList = new ArrayList<>();
        for(SysUpdateRecorde sysUpdateRecorde : list){
            boList.add(new UpdateRecorde(sysUpdateRecorde));
        }
        // 用PageInfo对查询结果进行包装
        PageInfo bean = new PageInfo<SysUpdateRecorde>(list);
        bean.setList(boList);
        return bean;
    }

    /**
     * 插入单个更新记录
     * @param entity 待插入记录
     * @return 插入的更新记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UpdateRecorde insert(UpdateRecorde entity){
        // 验证待插入记录非空
        Assert.notNull(entity, "UpdateRecorde不可为空！");
        // 设置更新和创建时间
        entity.setUpdateTime(new Date());
        entity.setCreateTime(new Date());
        // 调用SysUpdateRecordeMapper操作数据库，插入更新记录的业务实体
        sysUpdateRecordeMapper.insertSelective(entity.getSysUpdateRecorde());
        return entity;
    }

    /**
     * 插入更新记录列表
     * @param entities 待插入更新记录列表
     */
    @Override
    public void insertList(List<UpdateRecorde> entities){
        // 验证列表非空
        Assert.notNull(entities, "UpdateRecordes不可为空！");
        // 设置更新和创建时间，并将数据实体转换为业务实体
        List<SysUpdateRecorde> list = new ArrayList<>();
        for (UpdateRecorde entity : entities) {
            entity.setUpdateTime(new Date());
            entity.setCreateTime(new Date());
            list.add(entity.getSysUpdateRecorde());
        }
        // 调用SysUpdateRecordeMapper操作数据库
        sysUpdateRecordeMapper.insertList(list);
    }

    /**
     * 删除指定主键记录
     * @param primaryKey 主键
     * @return 删除成功/失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByPrimaryKey(Long primaryKey){
        // 调用SysUpdateRecordeMapper操作数据库
        return sysUpdateRecordeMapper.deleteByPrimaryKey(primaryKey) > 0;
    }

    /**
     * 更新更新记录信息（若传入的某个字段为null值，则不更新该字段）
     * @param entity
     * @return 更新成功/失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSelective(UpdateRecorde entity){
        // 验证待更新实体非空
        Assert.notNull(entity, "UpdateRecorde不可为空！");
        // 设置更新时间
        entity.setUpdateTime(new Date());
        // 调用SysUpdateRecordeMapper操作数据库
        return sysUpdateRecordeMapper.updateByPrimaryKeySelective(entity.getSysUpdateRecorde()) > 0;
    }

    /**
     * 获取指定主键更新记录
     * @param primaryKey 主键
     * @return 更新记录
     */
    @Override
    public UpdateRecorde getByPrimaryKey(Long primaryKey){
        // 验证主键非空
        Assert.notNull(primaryKey, "PrimaryKey不可为空！");
        // 调用SysUpdateRecordeMapper操作数据库
        SysUpdateRecorde entity = sysUpdateRecordeMapper.selectByPrimaryKey(primaryKey);
        // 判断得到的记录是否非空，并转换为业务实体返回
        return null == entity ? null : new UpdateRecorde(entity);
    }

    /**
     * 获取所有更新记录
     * @return 更新记录列表
     */
    @Override
    public List<UpdateRecorde> listAll(){
        // 调用SysUpdateRecordeMapper操作数据库
        List<SysUpdateRecorde> entityList = sysUpdateRecordeMapper.selectAll();
        // 列表为空则返回null
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        // 非空，则将数据实体转换为业务业务实体
        List<UpdateRecorde> list = new ArrayList<>();
        for (SysUpdateRecorde entity : entityList) {
            list.add(new UpdateRecorde(entity));
        }
        return list;
    }
}
