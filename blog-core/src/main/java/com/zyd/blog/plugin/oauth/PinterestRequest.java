package com.zyd.blog.plugin.oauth;

import com.zyd.blog.framework.property.JustAuthProperties;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.request.AuthPinterestRequest;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2019/8/7 21:37
 * @since 1.8
 */
@Component
public class PinterestRequest implements OauthRequest, InitializingBean {
    //JustAuth属性
    @Autowired
    private JustAuthProperties properties;

    /**
     * 获得拼趣的授权
     * @return
     */
    @Override
    public AuthRequest getRequest() {
        AuthConfig authConfig = properties.getPinterest();
        return new AuthPinterestRequest(AuthConfig.builder()
                .clientId(authConfig.getClientId())
                .clientSecret(authConfig.getClientSecret())
                .redirectUri(authConfig.getRedirectUri())
                .build());
    }
    /**
     * spring  afterPropertiesSet()方法
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        RequestFactory.registerRequest("pinterest", this);
    }
}
