package com.zyd.blog.business.service;


import com.zyd.blog.business.entity.RoleResources;
import com.zyd.blog.framework.object.AbstractService;

/**
 * 角色-资源服务接口类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
public interface SysRoleResourcesService extends AbstractService<RoleResources, Long> {

    /**
     * 添加角色-资源
     *
     * @param roleId 角色id
     * @param resourcesId 资源id
     */
    void addRoleResources(Long roleId, String resourcesId);

    /**
     * 通过角色id批量删除
     *
     * @param roleId 角色id
     */
    void removeByRoleId(Long roleId);
}
