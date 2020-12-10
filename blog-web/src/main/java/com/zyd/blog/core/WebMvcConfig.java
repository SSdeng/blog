package com.zyd.blog.core;

import com.zyd.blog.core.intercepter.BraumIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/11/19 9:39
 * @since 1.8
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 恶意请求拦截器
     */
    @Autowired
    BraumIntercepter braumIntercepter;

    /**
     * 添加拦截器
     *
     * @param registry 拦截器注册对象
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //对指定路径添加恶意请求拦截器
        registry.addInterceptor(braumIntercepter)
                .excludePathPatterns("/assets/**", "/error/**", "favicon.ico", "/css/**", "/js/**", "/img/**")
                .addPathPatterns("/**");
    }
}
