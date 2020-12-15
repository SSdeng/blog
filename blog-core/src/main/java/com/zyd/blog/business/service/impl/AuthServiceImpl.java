package com.zyd.blog.business.service.impl;

import com.zyd.blog.business.entity.User;
import com.zyd.blog.business.enums.UserTypeEnum;
import com.zyd.blog.business.service.AuthService;
import com.zyd.blog.business.service.SysUserService;
import com.zyd.blog.plugin.oauth.RequestFactory;
import com.zyd.blog.util.BeanConvertUtil;
import com.zyd.blog.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 授权登录服务实现类
 * 使用开源项目JustAuth来完成第三方授权登录
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2019/5/25 14:34
 * @since 1.8
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    /** 注入用户服务 */
    @Autowired
    private SysUserService userService;

    /**
     * 登录
     * @param source 第三方授权平台
     * @param callback 回调接口
     * @return 登录成功/失败
     */
    @Override
    public boolean login(String source, AuthCallback callback) {
        // 利用request工厂类，创建授权request
        AuthRequest authRequest = RequestFactory.getInstance(source).getRequest();
        // 登录并获取登录信息
        AuthResponse<? extends Object> response = authRequest.login(callback);
        // 如果登录成功
        if (response.ok()) {
            // 获取到的用户资料
            AuthUser authUser = (AuthUser) response.getData();
            // 转换为OneBlog内部所需的用户类
            User newUser = BeanConvertUtil.doConvert(authUser, User.class);
            newUser.setSource(authUser.getSource().toString());
            // 若获取的资料中有性别资料，则设置User中的性别属性
            if (null != authUser.getGender()) {
                newUser.setGender(authUser.getGender().getCode());
            }
            // 通过uuid和source来查找数据库中是否已创建该用户
            User user = userService.getByUuidAndSource(authUser.getUuid(), authUser.getSource().toString());
            newUser.setUserType(UserTypeEnum.USER);
            // 已创建，更新用户信息
            if (null != user) {
                newUser.setId(user.getId());
                userService.updateSelective(newUser);
            } else { // 未创建，插入新用户
                userService.insert(newUser);
            }
            // 将User放入session中
            SessionUtil.setUser(newUser);
            // 登录成功
            return true;
        }
        // 登录失败
        log.warn("[{}] {}", source, response.getMsg());
        return false;
    }

    /**
     * 取消指定用户（by userid）的授权
     * @param source 第三方授权平台
     * @param userId 用户id
     * @return 取消成功/失败
     */
    @Override
    public boolean revoke(String source, Long userId) {
        // 直接返回false，实际上未实现该功能
        return false;
    }

    /**
     * 登出
     */
    @Override
    public void logout() {
        // 调用session工具类，移除用户信息实现登出功能
        SessionUtil.removeUser();
    }
}
