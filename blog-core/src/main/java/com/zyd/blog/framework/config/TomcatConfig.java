package com.zyd.blog.framework.config;

import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Tomcat相关配置</p>
 * @author zmingchun
 * @version 1.0 (2018/6/20 10:55)
 */
@Configuration
public class TomcatConfig {
	/**
	 *对TomCat进行设置
	 * @return ConfigurableServletWebServerFactory 可以进行设置的WebServer，WebServer一个服务器的所有设置
	 */
	@Bean
	public ConfigurableServletWebServerFactory webServerFactory() {

		//TomcatServletWebServerFactory是一个ConfigurableServletWebServerFactory，可以用于创建Tomcat服务
		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();

		//调用addConnectorCustomizers，向TomcatServletWebServerFactory中添加使用的协议
		factory.addConnectorCustomizers(
				//使用lambda表达式，把函数作为参数传递入TomcatConnectorCustomizer的customize方法中
				//connector实现了Tomcat连接的Coyote框架
				//Tomecat用coyote来处理底层的socket，并将http请求、响应等字节流层面的东西，
				// 包装成Request和Response两个类，这两个类是tomcat定义的，供容器使用；
				connector -> {

					//Http11NioProtocol是Tomcat的线程池配置协议
					Http11NioProtocol protocol =
							//获得connector中的ProtocolHandler
							//ProtocolHandler是对抽象协议，例如线程等等的实现
							(Http11NioProtocol) connector.getProtocolHandler();

					//设置不忽略connectUploadTimeout
					protocol.setDisableUploadTimeout(false);
				}
		);

		//生成的TomcatServletWebServerFactory
		return factory;
	}


}
