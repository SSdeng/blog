package com.zyd.blog.business.service;


import com.github.pagehelper.PageInfo;
import com.zyd.blog.business.entity.Role;
import com.zyd.blog.business.vo.RoleConditionVO;
import com.zyd.blog.framework.object.AbstractService;

import java.util.List;
import java.util.Map;

/**
 * 角色服务接口类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
public interface SysRoleService extends AbstractService<Role, Long> {

    /**
     * 获取ztree使用的角色列表
     *
     * @param uid 用户id
     * @return 角色列表
     */
    List<Map<String, Object>> queryRoleListWithSelected(Integer uid);

    /**
     * 分页查询
     * 使用PageHelper开源项目实现分页
     * @param vo 角色条件VO，包含分页参数
     * @return PageInfo
     */
    PageInfo<Role> findPageBreakByCondition(RoleConditionVO vo);

    /**
     * 获取用户的角色
     *
     * @param userId 用户id
     * @return 用户角色列表
     */
    List<Role> listRolesByUserId(Long userId);
}
