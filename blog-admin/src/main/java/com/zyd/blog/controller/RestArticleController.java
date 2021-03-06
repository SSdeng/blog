package com.zyd.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zyd.blog.business.annotation.BussinessLog;
import com.zyd.blog.business.entity.Article;
import com.zyd.blog.business.enums.BaiduPushTypeEnum;
import com.zyd.blog.business.enums.ConfigKeyEnum;
import com.zyd.blog.business.enums.ResponseStatus;
import com.zyd.blog.business.service.BizArticleService;
import com.zyd.blog.business.service.SysConfigService;
import com.zyd.blog.business.util.BaiduPushUtil;
import com.zyd.blog.business.vo.ArticleConditionVO;
import com.zyd.blog.framework.object.PageResult;
import com.zyd.blog.framework.object.ResponseVO;
import com.zyd.blog.util.ResultUtil;
import com.zyd.blog.util.UrlBuildUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * 文章管理
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/24 14:37
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping("/article")
public class RestArticleController {
    @Autowired
    /**
     * 文章业务层对象
     */
    private BizArticleService articleService;
    @Autowired
    /**
     *  配置业务层对象 面向接口
     */
    private SysConfigService configService;
    /**
     * 字符串常量
     *
     */
    private static final String AT_LEAST_ONE = "请至少选择一条记录";

    /**
     * 文章分页
     * @param vo 封装好的文章对象
     * @return bootstrap table用到的返回json格式
     */
    @RequiresPermissions("articles")
    @PostMapping("/list")
    public PageResult list(ArticleConditionVO vo) {
        PageInfo<Article> pageInfo = articleService.findPageBreakByCondition(vo);
        return ResultUtil.tablePage(pageInfo);
    }

    /**
     * 删除文章
     * @param ids id
     * @return JSON
     */
    @RequiresPermissions(value = {"article:batchDelete", "article:delete"}, logical = Logical.OR)
    @PostMapping(value = "/remove")
    @BussinessLog("删除文章[{1}]")
    public ResponseVO<Object> remove(Long[] ids) {
        if (null == ids) {
            return ResultUtil.error(500, AT_LEAST_ONE);
        }
        for (Long id : ids) {
            articleService.removeByPrimaryKey(id);
        }
        return ResultUtil.success("成功删除 [" + ids.length + "] 篇文章");
    }

    /**
     * 获取文章详情
     * @param id id
     * @return JSON
     */
    @RequiresPermissions("article:get")
    @PostMapping("/get/{id}")
    @BussinessLog("获取文章[{1}]详情")
    public ResponseVO<Object> get(@PathVariable Long id) {
        return ResultUtil.success(null, this.articleService.getByPrimaryKey(id));
    }

    /**
     * 发布文章
     * @param article 文章
     * @param tags 标签
     * @param file 文件
     * @return JSON
     */
    @RequiresPermissions(value = {"article:edit", "article:publish"}, logical = Logical.OR)
    @PostMapping("/save")
    @BussinessLog("发布文章")
    public ResponseVO<Object> edit(Article article, Long[] tags, MultipartFile file) {
        articleService.publish(article, tags, file);
        return ResultUtil.success(ResponseStatus.SUCCESS);
    }

    /**
     * 修改文章的状态
     * @param type 要修改的文章状态
     * @param id 要修改的文章id
     * @return JSON
     */
    @RequiresPermissions(value = {"article:top", "article:recommend"}, logical = Logical.OR)
    @PostMapping("/update/{type}")
    @BussinessLog("修改文章[{2}]的状态[{1}]")
    public ResponseVO<Object> update(@PathVariable("type") String type, Long id) {
        articleService.updateTopOrRecommendedById(type, id);
        return ResultUtil.success(ResponseStatus.SUCCESS);
    }

    /**
     * 推送文章到百度站长平台
     * @param type 文章的状态
     * @param ids 文章的id
     * @return JSON
     */
    @RequiresPermissions(value = {"article:batchPush", "article:push"}, logical = Logical.OR)
    @PostMapping(value = "/pushToBaidu/{type}")
    @BussinessLog("推送文章[{2}]到百度站长平台")
    public ResponseVO<Object> pushToBaidu(@PathVariable("type") BaiduPushTypeEnum type, Long[] ids) {
        if (null == ids) {
            return ResultUtil.error(500, AT_LEAST_ONE);
        }
        Map<String, Object> config = configService.getConfigs();
        String siteUrl = (String) config.get(ConfigKeyEnum.SITE_URL.getKey());
        StringBuilder params = new StringBuilder();
        for (Long id : ids) {
            params.append(siteUrl).append("/article/").append(id).append("\n");
        }
        // urls: 推送, update: 更新, del: 删除
        String url = UrlBuildUtil.getBaiduPushUrl(type.toString(), (String) config.get(ConfigKeyEnum.SITE_URL.getKey()), (String) config.get(ConfigKeyEnum.BAIDU_PUSH_TOKEN.getKey()));
        String result = BaiduPushUtil.doPush(url, params.toString(), (String) config.get(ConfigKeyEnum.BAIDU_PUSH_COOKIE.getKey()));
        log.info(result);

        JSONObject resultJson = parseObject(result);

        if (resultJson.containsKey("error")) {
            return ResultUtil.error(resultJson.getString("message"));
        }
        return ResultUtil.success(null, result);
    }

    /**
     * 批量发布文章
     * @param ids 文章的id
     * @return JSON
     */
    @RequiresPermissions(value = {"article:publish"}, logical = Logical.OR)
    @PostMapping(value = "/batchPublish")
    @BussinessLog("批量发布文章[{1}]")
    public ResponseVO<Object> batchPublish(Long[] ids) {
        if (null == ids) {
            return ResultUtil.error(500, AT_LEAST_ONE);
        }
        articleService.batchUpdateStatus(ids, true);
        return ResultUtil.success("批量发布完成");
    }
}
