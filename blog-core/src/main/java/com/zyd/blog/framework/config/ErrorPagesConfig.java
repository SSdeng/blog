package com.zyd.blog.framework.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * 自定义的异常页面配置
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Component
public class ErrorPagesConfig {
    /**
     * 自定义异常处理路径
     *
     * @return
     */
    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> containerCustomizer() {
		/**
		 * 使用lambda表达式，将factory的函数customize作为参数传入
		 */
		return factory -> {
			//向factory中加入错误页面
			//ErrorPage中定义了错误属性的HTTP状态及对应的路径
			factory.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/error/400"));
			factory.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED, "/error/401"));
			factory.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/error/403"));
			factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error/404"));
			factory.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500"));
			factory.addErrorPages(new ErrorPage(Throwable.class, "/error/500"));
		};
    }


}
