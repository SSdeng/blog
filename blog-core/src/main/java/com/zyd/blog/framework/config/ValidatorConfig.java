package com.zyd.blog.framework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Configuration
@Order(0)
public class ValidatorConfig implements WebMvcConfigurer {

    /**
     * 用于获取Validator
     * Validator是应用程序特定对象的验证器，自动化校验工具，检验输入数据是否符合规范
     * 参照企业级应用软件开发技术实验5
     * @return LocalValidatorFactoryBean是Spring上下文中javax.validation的中心配置类，都是继承自SpringValidatorAdapter
     */
    @Override
    public Validator getValidator() {
        return new LocalValidatorFactoryBean();
    }

    /**
     * MessageCodesResolver从验证错误代码生成消息代码的策略接口。
     * @return 返回空
     */
    @Override
    public MessageCodesResolver getMessageCodesResolver() {
        return null;
    }
}
