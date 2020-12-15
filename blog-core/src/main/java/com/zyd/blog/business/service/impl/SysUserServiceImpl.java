package com.zyd.blog.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.zyd.blog.business.entity.User;
import com.zyd.blog.business.entity.UserPwd;
import com.zyd.blog.business.enums.UserNotificationEnum;
import com.zyd.blog.business.enums.UserPrivacyEnum;
import com.zyd.blog.business.enums.UserStatusEnum;
import com.zyd.blog.business.service.SysUserService;
import com.zyd.blog.business.vo.UserConditionVO;
import com.zyd.blog.framework.exception.ZhydCommentException;
import com.zyd.blog.framework.exception.ZhydException;
import com.zyd.blog.framework.holder.RequestHolder;
import com.zyd.blog.persistence.beans.SysUser;
import com.zyd.blog.persistence.mapper.SysUserMapper;
import com.zyd.blog.util.IpUtil;
import com.zyd.blog.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户服务实现类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    // 注入用户mapper类
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    @Lazy
    private SysUserService sysUserService;

    /**
     * 插入单个用户
     * @param user 待插入的用户
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User insert(User user) {
        // 验证传入的user是否非空
        Assert.notNull(user, "User不可为空！");
        // 更新user信息
        user.setUpdateTime(new Date());
        user.setCreateTime(new Date());
        // 调用Ip工具类和RequestHolder类获取实际IP
        user.setRegIp(IpUtil.getRealIp(RequestHolder.getRequest()));
        user.setPrivacy(UserPrivacyEnum.PUBLIC.getCode());
        user.setNotification(UserNotificationEnum.DETAIL);
        user.setStatus(UserStatusEnum.NORMAL.getCode());
        // 调用用户SysUserMapper相应方法插入用户
        sysUserMapper.insertSelective(user.getSysUser());
        return user;
    }

    /**
     * 插入多个用户
     * @param users 待插入用户列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertList(List<User> users) {
        // 验证传入的user列表是否非空
        Assert.notNull(users, "Users不可为空！");
        List<SysUser> sysUsers = new ArrayList<>();
        // 调用Ip工具类和RequestHolder类获取实际IP
        String regIp = IpUtil.getRealIp(RequestHolder.getRequest());
        // 遍历，更新用户信息，转换为业务实体
        for (User user : users) {
            user.setUpdateTime(new Date());
            user.setCreateTime(new Date());
            user.setRegIp(regIp);
            user.setPrivacy(UserPrivacyEnum.PUBLIC.getCode());
            user.setNotification(UserNotificationEnum.DETAIL);
            sysUsers.add(user.getSysUser());
        }
        // 调用SysUserMapper相应方法插入多个用户
        sysUserMapper.insertList(sysUsers);
    }

    /**
     * 根据主键删除用户
     * @param primaryKey 主键
     * @return 删除成功/失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByPrimaryKey(Long primaryKey) {
        // 调用SysUserMapper相应方法删除用户
        return sysUserMapper.deleteByPrimaryKey(primaryKey) > 0;
    }

    /**
     * 根据传来的user更新用户信息（若传入的某个字段为null值，则不更新该字段）
     * @param user 新用户信息
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSelective(User user) {
        // 验证user是否非空
        Assert.notNull(user, "User不可为空！");
        user.setUpdateTime(new Date());
        if (!StringUtils.isEmpty(user.getPassword())) { // 验证密码是否非空
            try {
                // 密码加密
                user.setPassword(PasswordUtil.encrypt(user.getPassword(), user.getUsername()));
            } catch (Exception e) {
                e.printStackTrace();
                throw new ZhydCommentException("密码加密失败");
            }
        } else {
            user.setPassword(null);
        }
        // 调用SysUserMapper相应方法更新用户信息
        return sysUserMapper.updateByPrimaryKeySelective(user.getSysUser()) > 0;
    }

    /**
     * 根据主键获取用户
     * @param primaryKey 主键
     * @return 获取成功/失败
     */
    @Override
    public User getByPrimaryKey(Long primaryKey) {
        // 验证主键是否非空
        Assert.notNull(primaryKey, "PrimaryKey不可为空！");
        // 调用SysUserMapper相应方法获取用户数据实体，返回时转换为业务实体
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(primaryKey);
        return null == sysUser ? null : new User(sysUser);
    }

    /**
     * 列出所有用户
     * @return 所有用户列表
     */
    @Override
    public List<User> listAll() {
        // 调用SysUserMapper相应方法获取所有用户
        List<SysUser> sysUsers = sysUserMapper.selectAll();
        // 列表为空则返回null
        if (CollectionUtils.isEmpty(sysUsers)) {
            return null;
        }
        List<User> users = new ArrayList<>();
        // 遍历，数据实体转换为业务实体
        for (SysUser sysUser : sysUsers) {
            users.add(new User(sysUser));
        }
        return users;
    }

    /**
     * 分页查询用户
     * 使用PageHelper开源项目实现分页
     * @param vo 用户条件VO，包含分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<User> findPageBreakByCondition(UserConditionVO vo) {
        // 设置分页参数，开启分页
        PageMethod.startPage(vo.getPageNumber(), vo.getPageSize());
        // 紧跟着的第一个数据查询会被分页
        List<SysUser> sysUsers = sysUserMapper.findPageBreakByCondition(vo);
        // 结果列表为空，则返回null
        if (CollectionUtils.isEmpty(sysUsers)) {
            return null;
        }
        List<User> users = new ArrayList<>();
        // 遍历，数据实体转换为业务实体
        for (SysUser su : sysUsers) {
            users.add(new User(su));
        }
        // 用PageInfo对查询结果进行包装
        return new PageInfo<>(users);
    }

    /**
     * 更新用户最后一次登录的状态信息
     *
     * @param user 待更新的用户
     * @return 更新后的用户
     */
    @Override
    public User updateUserLastLoginInfo(User user) {
        if (user != null) { // 验证user非空
            // 更新user信息
            user.setLoginCount(user.getLoginCount() + 1);
            user.setLastLoginTime(new Date());
            // 调用Ip工具类和RequestHolder类获取实际IP
            user.setLastLoginIp(IpUtil.getRealIp(RequestHolder.getRequest()));
            user.setPassword(null);
            sysUserService.updateSelective(user);
        }
        return user;
    }

    /**
     * 根据用户名获取用户
     *
     * @param userName 用户名
     * @return 用户
     */
    @Override
    public User getByUserName(String userName) {
        User user = new User(userName, null);
        // 调用SysUserMapper相应方法
        SysUser sysUser = this.sysUserMapper.selectOne(user.getSysUser());
        // 判断是否为空，并转换为数据实体
        return null == sysUser ? null : new User(sysUser);
    }

    /**
     * 通过角色Id获取用户列表
     *
     * @param roleId 角色id
     * @return 用户列表
     */
    @Override
    public List<User> listByRoleId(Long roleId) {
        // 调用SysUserMapper相应方法
        List<SysUser> sysUsers = sysUserMapper.listByRoleId(roleId);
        if (CollectionUtils.isEmpty(sysUsers)) {
            return null;
        }
        List<User> users = new ArrayList<>();
        // 遍历，数据实体转换为业务实体
        for (SysUser su : sysUsers) {
            users.add(new User(su));
        }
        return users;
    }

    /**
     * 修改密码
     *
     * @param userPwd 密码修改信息
     * @return 修改密码成功/失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePwd(UserPwd userPwd) throws Exception {
        // 验证两次密码输入是否一致
        if (!userPwd.getNewPassword().equals(userPwd.getNewPasswordRepeat())) {
            throw new ZhydException("新密码不一致！");
        }
        User user = this.getByPrimaryKey(userPwd.getId());
        // 验证用户是否存在
        if (null == user) {
            throw new ZhydException("用户编号错误！请不要手动操作用户ID！");
        }
        // 验证原密码是否正确
        if (!user.getPassword().equals(PasswordUtil.encrypt(userPwd.getPassword(), user.getUsername()))) {
            throw new ZhydException("原密码不正确！");
        }
        user.setPassword(userPwd.getNewPassword());

        return this.updateSelective(user);
    }

    /**
     * 根据uuid和source获取用户
     * @param uuid uuid
     * @param source 第三方授权平台
     * @return 用户
     */
    @Override
    public User getByUuidAndSource(String uuid, String source) {
        // 验证uuid和source是否非空
        if (StringUtils.isEmpty(uuid) || StringUtils.isEmpty(source)) {
            return null;
        }
        // 构建用户业务实体
        SysUser user = new SysUser();
        user.setUuid(uuid);
        user.setSource(source);
        // 调用SysUserMapper相应方法，取出相应业务实体，返回时转换
        user = sysUserMapper.selectOne(user);
        return null == user ? null : new User(user);
    }


}
