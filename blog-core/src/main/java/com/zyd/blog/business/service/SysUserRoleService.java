package com.zyd.blog.business.service;


import com.zyd.blog.business.entity.UserRole;
import com.zyd.blog.framework.object.AbstractService;

/**
 * 用户-角色服务接口类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
public interface SysUserRoleService extends AbstractService<UserRole, Long> {

    /**
     * 给指定id用户添加角色
     *
     * @param userId 用户id
     * @param roleIds 角色id
     */
    void addUserRole(Long userId, String roleIds);

    /**
     * 删除指定id用户的角色
     *
     * @param userId 用户id
     */
    void removeByUserId(Long userId);
}
