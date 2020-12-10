package com.zyd.blog.business.service;


import com.github.pagehelper.PageInfo;
import com.zyd.blog.business.entity.User;
import com.zyd.blog.business.entity.UserPwd;
import com.zyd.blog.business.vo.UserConditionVO;
import com.zyd.blog.framework.object.AbstractService;

import java.util.List;

/**
 * 用户服务接口类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
public interface SysUserService extends AbstractService<User, Long> {

    /**
     * 分页查询
     * 使用PageHelper开源项目实现分页
     * @param vo 用户条件VO，包含分页参数
     * @return PageInfo
     */
    PageInfo<User> findPageBreakByCondition(UserConditionVO vo);

    /**
     * 更新用户最后一次登录的状态信息
     *
     * @param user 待更新的用户
     * @return 更新后的用户
     */
    User updateUserLastLoginInfo(User user);

    /**
     * 根据用户名查找
     *
     * @param userName 用户名
     * @return 用户
     */
    User getByUserName(String userName);

    /**
     * 通过角色Id获取用户列表
     *
     * @param roleId 角色id
     * @return 用户列表
     */
    List<User> listByRoleId(Long roleId);

    /**
     * 修改密码
     *
     * @param userPwd 密码修改信息
     * @return 修改密码成功/失败
     */
    boolean updatePwd(UserPwd userPwd) throws Exception;


    /**
     * 通过用户的uuid和source查询用户是否存在
     *
     * @param uuid uuid
     * @param source 第三方授权平台
     * @return 用户
     */
    User getByUuidAndSource(String uuid, String source);
}
