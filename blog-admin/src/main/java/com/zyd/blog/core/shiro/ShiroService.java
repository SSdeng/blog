package com.zyd.blog.core.shiro;

import java.util.Map;

/**
 * Shiro-权限相关的业务处理接口
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2019/2/11 10:07
 * @since 1.8
 */
public interface ShiroService {

    Map<String, String> loadFilterChainDefinitions();

    void updatePermission();

    void reloadAuthorizingByRoleId(Long roleId);
}
