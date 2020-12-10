package com.zyd.blog.business.service.impl;

import com.zyd.blog.business.entity.UserRole;
import com.zyd.blog.business.service.SysUserRoleService;
import com.zyd.blog.framework.holder.RequestHolder;
import com.zyd.blog.persistence.beans.SysUserRole;
import com.zyd.blog.persistence.mapper.SysUserRoleMapper;
import com.zyd.blog.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户-角色服务实现类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Service
public class SysUserRoleServiceImpl implements SysUserRoleService {

    @Autowired
    private SysUserRoleMapper resourceMapper;

    /**
     * 插入单个新的用户-角色
     * @param entity 用户-角色
     * @return 新的用户-角色
     */
    @Override
    public UserRole insert(UserRole entity) {
        // 验证要插入的用户角色非空
        Assert.notNull(entity, "UserRole不可为空！");
        // 设置角色创建和更新时间
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        // 调用SysUserRoleMapper方法
        resourceMapper.insert(entity.getSysUserRole());
        return entity;
    }

    /**
     * 插入多个用户-角色
     * @param entities 用户-角色列表
     */
    @Override
    public void insertList(List<UserRole> entities) {
        // 验证用户角色列表非空
        Assert.notNull(entities, "entities不可为空！");
        List<SysUserRole> sysUserRole = new ArrayList<>();
        // 遍历，设置角色创建和更新时间
        for (UserRole UserRole : entities) {
            UserRole.setUpdateTime(new Date());
            UserRole.setCreateTime(new Date());
            sysUserRole.add(UserRole.getSysUserRole());
        }
        // 调用SysUserRoleMapper方法
        resourceMapper.insertList(sysUserRole);
    }

    /**
     * 删除指定主键的用户-角色
     * @param primaryKey 主键
     * @return 删除成功/失败
     */
    @Override
    public boolean removeByPrimaryKey(Long primaryKey) {
        // 调用resourceMapper方法
        return resourceMapper.deleteByPrimaryKey(primaryKey) > 0;
    }

    /**
     * 更新用户-角色信息（若传入的某个字段为null值，则不更新该字段）
     * @param entity 用户-角色
     * @return 更新成功/失败
     */
    @Override
    public boolean updateSelective(UserRole entity) {
        // 验证传入的角色非空
        Assert.notNull(entity, "UserRole不可为空！");
        // 设置更新时间
        entity.setUpdateTime(new Date());
        // 调用SysUserRoleMapper方法
        return resourceMapper.updateByPrimaryKeySelective(entity.getSysUserRole()) > 0;
    }

    /**
     * 获取指定主键的用户-角色
     * @param primaryKey 主键
     * @return 用户-角色
     */
    @Override
    public UserRole getByPrimaryKey(Long primaryKey) {
        // 验证主键非空
        Assert.notNull(primaryKey, "PrimaryKey不可为空！");
        // 调用SysUserRoleMapper方法
        SysUserRole sysUserRole = resourceMapper.selectByPrimaryKey(primaryKey);
        return null == sysUserRole ? null : new UserRole(sysUserRole);
    }

    /**
     * 获取所有用户-角色
     * @return 用户-角色列表
     */
    @Override
    public List<UserRole> listAll() {
        // 调用SysUserRoleMapper方法
        List<SysUserRole> sysUserRole = resourceMapper.selectAll();
        // 调用私有方法将数据实体转换为业务实体
        return getUserRole(sysUserRole);
    }

    /**
     * 用户-角色数据实体转换为业务实体
     * @param sysUserRole 用户-角色数据实体
     * @return 用户-角色业务实体
     */
    private List<UserRole> getUserRole(List<SysUserRole> sysUserRole) {
        // 验证数据实体列表非空
        if (CollectionUtils.isEmpty(sysUserRole)) {
            return null;
        }
        // 创建业务实体列表
        List<UserRole> UserRole = new ArrayList<>();
        // 遍历，添加
        for (SysUserRole r : sysUserRole) {
            UserRole.add(new UserRole(r));
        }
        return UserRole;
    }

    /**
     * 根据用户id和角色id列表添加用户-角色，用户id先前的用户-角色会被清除
     *
     * @param userId 用户id
     * @param roleIds 角色id列表
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Exception.class})
    public void addUserRole(Long userId, String roleIds) {
        // 删除用户id之前相关的所有用户-角色
        removeByUserId(userId);
        // 添加新的用户-角色
        String[] roleIdArr = roleIds.split(",");
        if (roleIdArr.length == 0) {
            return;
        }
        UserRole u = null;
        // 创建用户-角色列表
        List<UserRole> roles = new ArrayList<>();
        // 用户id不变，遍历添加传入的角色id，构成新的用户-角色
        for (String roleId : roleIdArr) {
            u = new UserRole();
            u.setUserId(userId);
            u.setRoleId(Long.parseLong(roleId));
            roles.add(u);
        }
        insertList(roles);
    }

    /**
     * 删除指定用户id的所有用户-角色
     *
     * @param userId 用户id
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Exception.class})
    public void removeByUserId(Long userId) {
        // 相当于 delete from sys_user_role where user_id = userid
        Example example = new Example(SysUserRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        resourceMapper.deleteByExample(example);
    }
}
