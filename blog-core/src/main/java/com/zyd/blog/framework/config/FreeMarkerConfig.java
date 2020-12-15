package com.zyd.blog.framework.config;

import com.jagregory.shiro.freemarker.ShiroTags;
import com.zyd.blog.business.service.SysConfigService;
import com.zyd.blog.framework.tag.ArticleTags;
import com.zyd.blog.framework.tag.CustomTags;
import freemarker.template.TemplateModelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * freemarker配置类
 * 
 */
@Configuration
public class FreeMarkerConfig {

    /**
     * 创建freemaker配置实例
     */
    @Autowired
    protected freemarker.template.Configuration configuration;

    /**
     *自定义标签customTags
     */
    @Autowired
    protected CustomTags customTags;

    /**
     * 自定义标签articleTags
     */
    @Autowired
    protected ArticleTags articleTags;

    /**
     *系统设置
     */
    @Autowired
    private SysConfigService configService;

    /**
     * 添加自定义标签
     * 使用setShareVariable实现变量共享
     *  @PostConstruct 在服务器加载Servlet时自动运行
     */
    @PostConstruct
    public void setSharedVariable() {
        //向freemarker的configuration中加入自定义标签customTag
        configuration.setSharedVariable("zhydTag", customTags);

        //向freemarker的configuration中加入自定义标签articleTag
        configuration.setSharedVariable("articleTag", articleTags);

        try {
            //将系统设置加入到freemarker的configuration中
            configuration.setSharedVariable("config", configService.getConfigs());
            //shiro标签
            configuration.setSharedVariable("shiro", new ShiroTags());
        }
        //当包装/解包失败时，ObjectWrapper-s可能会抛出这个，或者如果无法检索请求的数据，TemplateModel方法会抛出这个。
        catch (TemplateModelException e) {
            e.printStackTrace();
        }
    }
}
