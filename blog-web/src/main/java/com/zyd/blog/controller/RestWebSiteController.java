package com.zyd.blog.controller;

import com.zyd.blog.business.annotation.BussinessLog;
import com.zyd.blog.business.entity.Template;
import com.zyd.blog.business.enums.PlatformEnum;
import com.zyd.blog.business.enums.TemplateKeyEnum;
import com.zyd.blog.business.service.*;
import com.zyd.blog.util.FreeMarkerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 站点相关的接口类，主要为sitemap和robots的生成
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/18 11:48
 * @since 1.0
 */
@RestController
public class RestWebSiteController {

    /**
     * 文章列表
     */
    @Autowired
    private BizArticleService articleService;
    /**
     * 分类服务
     */
    @Autowired
    private BizTypeService typeService;
    /**
     * 标签服务
     */
    @Autowired
    private BizTagsService tagsService;
    /**
     * 系统模板
     */
    @Autowired
    private SysTemplateService templateService;
    /**
     * 系统配置
     */
    @Autowired
    private SysConfigService configService;

    /**
     * 查看sitemap.xml
     *
     * @return
     */
    @GetMapping(value = "/sitemap.xml", produces = {"application/xml"})
    @BussinessLog(value = "查看sitemap.xml", platform = PlatformEnum.WEB)
    public String sitemapXml() {
        return getSitemap(TemplateKeyEnum.TM_SITEMAP_XML);
    }//返回xml格式网站地图

    /**
     * 查看sitemap.txt
     *
     * @return
     */
    @GetMapping(value = "/sitemap.txt", produces = {"text/plain"})
    @BussinessLog(value = "查看sitemap.txt", platform = PlatformEnum.WEB)
    public String sitemapTxt() {
        return getSitemap(TemplateKeyEnum.TM_SITEMAP_TXT);
    }//返回txt格式网站地图

    /**
     * 查看sitemap.html
     *
     * @return
     */
    @GetMapping(value = "/sitemap.html", produces = {"text/html"})
    @BussinessLog(value = "查看sitemap.html", platform = PlatformEnum.WEB)
    public String sitemapHtml() {
        return getSitemap(TemplateKeyEnum.TM_SITEMAP_HTML);
    }//返回html格式网站地图

    /**
     * 查看robots
     *
     * @return
     */
    @GetMapping(value = "/robots.txt", produces = {"text/plain"})
    @BussinessLog(value = "查看robots", platform = PlatformEnum.WEB)
    public String robots() {
        //新建robots模板
        Template template = templateService.getTemplate(TemplateKeyEnum.TM_ROBOTS);
        Map<String, Object> map = new HashMap<>();
        //载入系统配置信息
        map.put("config", configService.getConfigs());
        //调用FreeMarker生成robots文件
        return FreeMarkerUtil.template2String(template.getRefValue(), map, true);
    }

    /**
     * 生成sitemap
     *
     * @param key 模板类型
     * @return
     */
    private String getSitemap(TemplateKeyEnum key) {
        //新建key类型的模板
        Template template = templateService.getTemplate(key);
        Map<String, Object> map = new HashMap<>();
        //载入文章类型列表
        map.put("articleTypeList", typeService.listAll());
        //载入文章标签列表
        map.put("articleTagsList", tagsService.listAll());
        //载入文章列表
        map.put("articleList", articleService.listAll());
        //载入系统配置信息
        map.put("config", configService.getConfigs());
        //调用FreeMarker生成sitemap文件
        return FreeMarkerUtil.template2String(template.getRefValue(), map, true);
    }
}
