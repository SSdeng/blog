package com.zyd.blog.business.service.impl;

import com.zyd.blog.business.entity.RoleResources;
import com.zyd.blog.business.service.SysRoleResourcesService;
import com.zyd.blog.framework.holder.RequestHolder;
import com.zyd.blog.persistence.beans.SysRoleResources;
import com.zyd.blog.persistence.mapper.SysRoleResourcesMapper;
import com.zyd.blog.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 角色资源
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Service
public class SysRoleResourcesServiceImpl implements SysRoleResourcesService {
    @Autowired
    private SysRoleResourcesMapper resourceMapper;

    /**
     * 插入单个角色-资源
     * @param entity 角色-资源
     * @return
     */
    @Override
    public RoleResources insert(RoleResources entity) {
        // 验证角色-资源非空
        Assert.notNull(entity, "RoleResources不可为空！");
        // 设置更新和创建信息
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        // 调用SysRoleResourcesMapper操作数据库
        resourceMapper.insert(entity.getSysRoleResources());
        return entity;
    }

    /**
     * 插入多个角色-资源
     * @param entities 角色资源列表
     */
    @Override
    public void insertList(List<RoleResources> entities) {
        // 验证列表非空
        Assert.notNull(entities, "entities不可为空！");
        // 新建角色-资源数据实体
        List<SysRoleResources> sysRoleResources = new ArrayList<>();
        // 调用ip工具类和RequeHolder获取ip信息
        String regIp = IpUtil.getRealIp(RequestHolder.getRequest());
        // 遍历传入的列表，设置信息，列表添加数据实体
        for (RoleResources RoleResources : entities) {
            RoleResources.setUpdateTime(new Date());
            RoleResources.setCreateTime(new Date());
            sysRoleResources.add(RoleResources.getSysRoleResources());
        }
        // 调用SysRoleResourcesMapper操作数据库
        resourceMapper.insertList(sysRoleResources);
    }

    /**
     * 删除指定主键角色-资源
     * @param primaryKey
     * @return
     */
    @Override
    public boolean removeByPrimaryKey(Long primaryKey) {
        // 调用SysRoleResourcesMapper操作数据库
        return resourceMapper.deleteByPrimaryKey(primaryKey) > 0;
    }

    /**
     * 更新角色-资源（若传入的某个字段为null值，则不更新该字段）
     * @param entity 角色-资源
     * @return 更新成功/失败
     */
    @Override
    public boolean updateSelective(RoleResources entity) {
        // 验证传入的角色-资源非空
        Assert.notNull(entity, "RoleResources不可为空！");
        // 设置更新信息
        entity.setUpdateTime(new Date());
        // 调用SysRoleResourcesMapper操作数据库
        return resourceMapper.updateByPrimaryKeySelective(entity.getSysRoleResources()) > 0;
    }

    /**
     * 获取指定主键的角色-资源
     * @param primaryKey 主键
     * @return 角色-资源
     */
    @Override
    public RoleResources getByPrimaryKey(Long primaryKey) {
        // 验证主键非空
        Assert.notNull(primaryKey, "PrimaryKey不可为空！");
        // 调用SysRoleResourcesMapper操作数据库
        SysRoleResources sysRoleResources = resourceMapper.selectByPrimaryKey(primaryKey);
        // 判断结果是否为空，并转换为业务实体
        return null == sysRoleResources ? null : new RoleResources(sysRoleResources);
    }

    /**
     * 获取所有角色-资源
     * @return 角色-资源列表
     */
    @Override
    public List<RoleResources> listAll() {
        // 调用SysRoleResourcesMapper操作数据库
        List<SysRoleResources> sysRoleResources = resourceMapper.selectAll();
        // 调用内部私有函数，将业务实体列表转换为数据实体列表
        return getRoleResources(sysRoleResources);
    }

    /**
     * 将角色-资源业务实体列表转换为数据实体列表
     * @param sysRoleResources 角色-资源业务实体列表
     * @return 角色-资源数据实体列表
     */
    private List<RoleResources> getRoleResources(List<SysRoleResources> sysRoleResources) {
        // 若列表为空，返回null
        if (CollectionUtils.isEmpty(sysRoleResources)) {
            return null;
        }
        // 非空，构建数据实体列表，遍历加入转换的数据实体
        List<RoleResources> RoleResources = new ArrayList<>();
        for (SysRoleResources r : sysRoleResources) {
            RoleResources.add(new RoleResources(r));
        }
        return RoleResources;
    }

    /**
     * 添加角色-资源（先删除该角色先前的角色-资源）
     * @param roleId 角色id
     * @param resourcesId 多个资源id以','分隔组成的字符串
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Exception.class})
    public void addRoleResources(Long roleId, String resourcesId) {
        //删除
        removeByRoleId(roleId);
        //添加
        if (!StringUtils.isEmpty(resourcesId)) {
            // 分离出多个资源id
            String[] resourcesArr = resourcesId.split(",");
            SysRoleResources r = null;
            List<SysRoleResources> roleResources = new ArrayList<>();
            // 设置角色-资源信息
            for (String ri : resourcesArr) {
                r = new SysRoleResources();
                r.setRoleId(roleId);
                r.setResourcesId(Long.parseLong(ri));
                r.setCreateTime(new Date());
                r.setUpdateTime(new Date());
                roleResources.add(r);

            }
            // 调用SysRoleResourcesMapper操作数据库
            resourceMapper.insertList(roleResources);
        }
    }

    /**
     * 通过角色id批量删除
     * @param roleId 角色id
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Exception.class})
    public void removeByRoleId(Long roleId) {
        // 相当于 delete from sys_role_resource where role_id = roleId
        Example example = new Example(SysRoleResources.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId", roleId);
        resourceMapper.deleteByExample(example);
    }
}
