package com.zyd.blog.business.service;

import me.zhyd.oauth.model.AuthCallback;

/**
 * 授权登录服务接口类
 * 使用开源项目JustAuth来完成第三方授权登录
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2019/5/25 14:32
 * @since 1.8
 */
public interface AuthService {
    /**
     * 登录
     * @param source 第三方授权平台
     * @param callback 回调接口
     * @return 登录成功/失败
     */
    boolean login(String source, AuthCallback callback);

    /**
     * 取消指定用户（by userid）的授权
     * @param source 第三方授权平台
     * @param userId 用户id
     * @return 取消成功/失败
     */
    boolean revoke(String source, Long userId);

    /**
     * 登出
     */
    void logout();
}
