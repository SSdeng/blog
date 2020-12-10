package com.zyd.blog.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyd.blog.business.entity.Resources;
import com.zyd.blog.business.service.SysResourcesService;
import com.zyd.blog.business.vo.ResourceConditionVO;
import com.zyd.blog.persistence.beans.SysResources;
import com.zyd.blog.persistence.mapper.SysResourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 资源服务实现类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Service
public class SysResourcesServiceImpl implements SysResourcesService {

    @Autowired
    private SysResourceMapper resourceMapper;

    /**
     * 分页查询
     * 使用PageHelper开源项目
     * @param vo 资源条件VO，包含分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<Resources> findPageBreakByCondition(ResourceConditionVO vo) {
        // 设置分页参数，开启分页
        PageHelper.startPage(vo.getPageNumber(), vo.getPageSize());
        // 紧跟着的第一个数据查询会被分页
        List<SysResources> sysResources = resourceMapper.findPageBreakByCondition(vo);
        // 结果列表为空，则返回null
        if (CollectionUtils.isEmpty(sysResources)) {
            return null;
        }
        // 调用内部私有函数，将业务实体列表转换为数据实体列表
        List<Resources> resources = this.getResources(sysResources);
        // 用PageInfo对查询结果进行包装
        PageInfo bean = new PageInfo<SysResources>(sysResources);
        bean.setList(resources);
        return bean;
    }

    /**
     * 获取用户的资源列表
     *
     * @param map 存储了用户id和资源类型
     * @return 资源列表
     */
    @Override
    public List<Resources> listUserResources(Map<String, Object> map) {
        // 调用SysResourceMapper操作数据库
        List<SysResources> sysResources = resourceMapper.listUserResources(map);
        // 列表为空，则返回null
        if (CollectionUtils.isEmpty(sysResources)) {
            return null;
        }
        // 非空，转换为业务实体返回
        return this.getResources(sysResources);
    }

    /**
     * 获取ztree使用的资源列表
     *
     * @param rid 资源id
     * @return 存入了资源部分信息的map集合
     */
    @Override
    public List<Map<String, Object>> queryResourcesListWithSelected(Long rid) {
        // 调用SysResourceMapper操作数据库
        List<SysResources> sysResources = resourceMapper.queryResourcesListWithSelected(rid);
        // 列表为空，则返回null
        if (CollectionUtils.isEmpty(sysResources)) {
            return null;
        }
        // 非空，遍历结果集，将其中的信息加入map集合
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        for (SysResources resources : sysResources) {
            map = new HashMap<String, Object>(3);
            map.put("id", resources.getId());
            map.put("pId", resources.getParentId());
            map.put("checked", resources.getChecked());
            map.put("name", resources.getName());
            mapList.add(map);
        }
        return mapList;
    }

    /**
     * 获取资源的url和permission
     *
     * @return 只包含url和permission的资源列表
     */
    @Override
    public List<Resources> listUrlAndPermission() {
        // 调用SysResourceMapper操作数据库
        List<SysResources> sysResources = resourceMapper.listUrlAndPermission();
        // 返回其业务实体列表
        return getResources(sysResources);
    }

    /**
     * 获取所有可用的菜单资源
     *
     * @return 可用的菜单资源列表
     */
    @Override
    public List<Resources> listAllAvailableMenu() {
        // 调用SysResourceMapper操作数据库
        List<SysResources> sysResources = resourceMapper.listAllAvailableMenu();
        // 返回其业务实体列表
        return getResources(sysResources);
    }

    /**
     * 获取用户关联的所有资源
     *
     * @param userId 用户id
     * @return 用户关联的所有资源
     */
    @Override
    public List<Resources> listByUserId(Long userId) {
        // 调用SysResourceMapper操作数据库
        List<SysResources> sysResources = resourceMapper.listByUserId(userId);
        // 返回其业务实体列表
        return getResources(sysResources);
    }

    /**
     * 保存一个实体
     *
     * @param entity 资源实体
     * @return 存入的资源实体
     */
    @Override
    public Resources insert(Resources entity) {
        // 验证传入的资源实体非空
        Assert.notNull(entity, "Resources不可为空！");
        // 设置更新和创建信息
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        // 调用SysResourceMapper操作数据库,保存一个实体，null的属性也会保存，不会使用数据库默认值
        resourceMapper.insert(entity.getSysResources());
        return entity;
    }

    /**
     * 删除指定主键的资源实体
     * @param primaryKey 主键
     * @return 删除成功/失败
     */
    @Override
    public boolean removeByPrimaryKey(Long primaryKey) {
        // 调用SysResourceMapper操作数据库,根据主键字段进行删除
        return resourceMapper.deleteByPrimaryKey(primaryKey) > 0;
    }

    /**
     * 更新资源，null值不会被更新到数据库
     * @param entity 新的资源
     * @return 更新成功/失败
     */
    @Override
    public boolean updateSelective(Resources entity) {
        // 验证资源实体非空
        Assert.notNull(entity, "Resources不可为空！");
        // 设置更新时间
        entity.setUpdateTime(new Date());
        // 调用SysResourceMapper操作数据库，根据主键更新属性不为null的值
        return resourceMapper.updateByPrimaryKeySelective(entity.getSysResources()) > 0;
    }

    /**
     * 获取指定主键的资源
     * @param primaryKey 主键
     * @return 资源实体
     */
    @Override
    public Resources getByPrimaryKey(Long primaryKey) {
        // 验证主键非空
        Assert.notNull(primaryKey, "PrimaryKey不可为空！");
        // 调用SysResourceMapper操作数据库，根据主键字段进行查询
        SysResources sysResources = resourceMapper.selectByPrimaryKey(primaryKey);
        return null == sysResources ? null : new Resources(sysResources);
    }

    /**
     * 获取所有资源实体
     * @return 所有资源列表
     */
    @Override
    public List<Resources> listAll() {
        // 调用SysResourceMapper操作数据库
        List<SysResources> sysResources = resourceMapper.selectAll();
        // 调用内部私有函数，将业务实体列表转换为数据实体列表
        return getResources(sysResources);
    }

    /**
     * 将资源业务实体列表转换为数据实体列表
     * @param sysResources 资源业务实体列表
     * @return 资源数据实体列表
     */
    private List<Resources> getResources(List<SysResources> sysResources) {
        // 若列表为空，返回null
        if (CollectionUtils.isEmpty(sysResources)) {
            return null;
        }
        // 非空，构建数据实体列表，遍历加入转换的数据实体
        List<Resources> resources = new ArrayList<>();
        for (SysResources r : sysResources) {
            resources.add(new Resources(r));
        }
        return resources;
    }
}
