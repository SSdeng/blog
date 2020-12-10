package com.zyd.blog.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyd.blog.business.entity.Role;
import com.zyd.blog.business.service.SysRoleService;
import com.zyd.blog.business.vo.RoleConditionVO;
import com.zyd.blog.persistence.beans.SysRole;
import com.zyd.blog.persistence.mapper.SysRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 角色服务实现类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper roleMapper;

    /**
     * 获取ztree使用的角色列表
     *
     * @param userId 用户id
     * @return 角色列表
     */
    @Override
    public List<Map<String, Object>> queryRoleListWithSelected(Integer userId) {
        // 调用SysRoleMapper操作数据库
        List<SysRole> sysRole = roleMapper.queryRoleListWithSelected(userId);
        // 若返回的列表为空，则返回null
        if (CollectionUtils.isEmpty(sysRole)) {
            return null;
        }
        // 非空，则创建map列表
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        // 遍历结果集，设置信息
        for (SysRole role : sysRole) {
            map = new HashMap<String, Object>(3);
            map.put("id", role.getId());
            map.put("pId", 0);
            map.put("checked", role.getSelected() != null && role.getSelected() == 1);
            map.put("name", role.getDescription());
            mapList.add(map);
        }
        return mapList;
    }

    /**
     * 分页查询角色
     * 使用PageHelper开源项目实现分页
     * @param vo 角色条件VO，包含分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<Role> findPageBreakByCondition(RoleConditionVO vo) {
        // 设置分页参数，开启分页
        PageHelper.startPage(vo.getPageNumber(), vo.getPageSize());
        // 调用SysRoleMapper操作数据库
        List<SysRole> sysRoles = roleMapper.findPageBreakByCondition(vo);
        // 列表为空，则返回null
        if (CollectionUtils.isEmpty(sysRoles)) {
            return null;
        }
        // 非空，则构建列表，并调用私有函数转换为业务实体
        List<Role> roles = this.getRole(sysRoles);
        // 用PageInfo包装结果集
        PageInfo bean = new PageInfo<SysRole>(sysRoles);
        bean.setList(roles);
        return bean;
    }

    /**
     * 获取指定id用户的角色
     *
     * @param userId 用户id
     * @return 角色列表
     */
    @Override
    public List<Role> listRolesByUserId(Long userId) {
        // 调用SysRoleMapper操作数据库
        List<SysRole> sysRoles = roleMapper.listRolesByUserId(userId);
        // 若列表为空，返回null
        if (CollectionUtils.isEmpty(sysRoles)) {
            return null;
        }
        // 非空，转换为业务实体
        return this.getRole(sysRoles);
    }

    /**
     * 插入新的角色
     * @param entity 待插入角色
     * @return 新插入的角色
     */
    @Override
    public Role insert(Role entity) {
        // 验证传入的角色非空
        Assert.notNull(entity, "Role不可为空！");
        // 设置更新和创建信息
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        // 调用SysRoleMapper操作数据库
        roleMapper.insert(entity.getSysRole());
        return entity;
    }

    /**
     * 插入角色列表
     * @param entities 待插入角色列表
     */
    @Override
    public void insertList(List<Role> entities) {
        // 验证传入的列表非空
        Assert.notNull(entities, "entities不可为空！");
        // 构建数据实体角色列表
        List<SysRole> sysRole = new ArrayList<>();
        // 遍历，转换为数据实体，并加入列表
        for (Role role : entities) {
            role.setUpdateTime(new Date());
            role.setCreateTime(new Date());
            sysRole.add(role.getSysRole());
        }
        // 调用SysRoleMapper操作数据库
        roleMapper.insertList(sysRole);
    }

    /**
     * 删除指定主键角色
     * @param primaryKey 主键
     * @return 删除成功/失败
     */
    @Override
    public boolean removeByPrimaryKey(Long primaryKey) {
        // 调用SysRoleMapper操作数据库
        return roleMapper.deleteByPrimaryKey(primaryKey) > 0;
    }

    /**
     * 更新角色信息（若传入的某个字段为null值，则不更新该字段）
     * @param entity 角色实体
     * @return 更新成功/失败
     */
    @Override
    public boolean updateSelective(Role entity) {
        // 验证传入的角色实体非空
        Assert.notNull(entity, "Role不可为空！");
        // 设置更新时间
        entity.setUpdateTime(new Date());
        // 调用SysRoleMapper操作数据库
        return roleMapper.updateByPrimaryKeySelective(entity.getSysRole()) > 0;
    }

    /**
     * 获取指定主键的角色
     * @param primaryKey 主键
     * @return 角色
     */
    @Override
    public Role getByPrimaryKey(Long primaryKey) {
        // 验证主键非空
        Assert.notNull(primaryKey, "PrimaryKey不可为空！");
        // 调用SysRoleMapper操作数据库
        SysRole sysRole = roleMapper.selectByPrimaryKey(primaryKey);
        // 判断结果是否为空，并转换为业务实体
        return null == sysRole ? null : new Role(sysRole);
    }

    /**
     * 获取所有角色
     * @return 角色列表
     */
    @Override
    public List<Role> listAll() {
        // 调用SysRoleMapper操作数据库
        List<SysRole> sysRole = roleMapper.selectAll();
        // 调用内部私有函数，将业务实体列表转换为数据实体列表
        return getRole(sysRole);
    }

    /**
     * 将角色业务实体列表转换为数据实体列表
     * @param sysRole 角色业务实体列表
     * @return 角色数据实体列表
     */
    private List<Role> getRole(List<SysRole> sysRole) {
        // 若列表为空，返回null
        if (CollectionUtils.isEmpty(sysRole)) {
            return null;
        }
        // 非空，构建数据实体列表，遍历加入转换的数据实体
        List<Role> role = new ArrayList<>();
        for (SysRole r : sysRole) {
            role.add(new Role(r));
        }
        return role;
    }
}
