package com.zyd.blog.controller;

import com.github.pagehelper.PageInfo;
import com.zyd.blog.business.annotation.BussinessLog;
import com.zyd.blog.business.entity.Article;
import com.zyd.blog.business.enums.ArticleStatusEnum;
import com.zyd.blog.business.enums.PlatformEnum;
import com.zyd.blog.business.service.BizArticleArchivesService;
import com.zyd.blog.business.service.BizArticleService;
import com.zyd.blog.business.service.SysLinkService;
import com.zyd.blog.business.service.SysUpdateRecordeService;
import com.zyd.blog.business.vo.ArticleConditionVO;
import com.zyd.blog.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 页面跳转类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/18 11:48
 * @since 1.0
 */
@Controller
public class RenderController {

    /**
     * sidebar部分的推荐、近期和随机tab页中显示的文章数
     */
    private static final int SIDEBAR_ARTICLE_SIZE = 8;
    private static final String INDEX_URL = "index";

    /**
     * 文章列表
     */
    @Autowired
    private BizArticleService bizArticleService;
    /**
     * 归档目录
     */
    @Autowired
    private BizArticleArchivesService bizArticleArchivesService;
    /**
     * 友情链接
     */
    @Autowired
    private SysLinkService sysLinkService;
    /**
     * 更新服务记录
     */
    @Autowired
    private SysUpdateRecordeService updateRecordeService;

    /**
     * 加载首页的数据
     *
     * @param vo 文章属性vo
     * @param model
     * @return
     */
    private void loadIndexPage(ArticleConditionVO vo, Model model) {
        vo.setStatus(ArticleStatusEnum.PUBLISHED.getCode());
        //分页查询处理
        PageInfo<Article> pageInfo = bizArticleService.findPageBreakByCondition(vo);
        //设置页面
        model.addAttribute("page", pageInfo);
        //设置视图
        model.addAttribute("model", vo);
        //设置首页友情链接
        model.addAttribute("indexLinkList", sysLinkService.listOfIndex());
    }

    /**
     * 首页
     *
     * @param vo 文章属性vo
     * @param model
     * @return
     */
    @RequestMapping("/")
    @BussinessLog(value = "进入首页", platform = PlatformEnum.WEB)
    public ModelAndView home(ArticleConditionVO vo, Model model) {
        //设置url属性为首页
        model.addAttribute("url", INDEX_URL);
        //加载首页内容
        loadIndexPage(vo, model);

        //返回首页视图
        return ResultUtil.view(INDEX_URL);
    }

    /**
     * 首页（分页）
     *
     * @param pageNumber 首页页码
     * @param vo 文章属性对象
     * @param model
     * @return
     */
    @RequestMapping("/index/{pageNumber}")
    @BussinessLog(value = "进入文章列表第{1}页", platform = PlatformEnum.WEB)
    public ModelAndView type(@PathVariable("pageNumber") Integer pageNumber, ArticleConditionVO vo, Model model) {
        //设置页码
        vo.setPageNumber(pageNumber);
        //设置url为首页
        model.addAttribute("url", INDEX_URL);
        //加载指定页内容
        loadIndexPage(vo, model);

        //返回分页后的首页视图
        return ResultUtil.view(INDEX_URL);
    }

    /**
     * 分类列表
     *
     * @param typeId 文章类别ID
     * @param model
     * @return
     */
    @GetMapping("/type/{typeId}")
    @BussinessLog(value = "进入文章分类[{1}]列表页", platform = PlatformEnum.WEB)
    public ModelAndView type(@PathVariable("typeId") Long typeId, Model model) {
        ArticleConditionVO vo = new ArticleConditionVO();
        //设置文章vo的类型
        vo.setTypeId(typeId);
        //设置url为对应类型的url
        model.addAttribute("url", "type/" + typeId);
        //加载指定类别内容
        loadIndexPage(vo, model);

        //返回类型列表视图
        return ResultUtil.view(INDEX_URL);
    }

    /**
     * 分类列表（分页）
     *
     * @param typeId 文章类别ID
     * @param pageNumber 列表页码
     * @param model
     * @return
     */
    @GetMapping("/type/{typeId}/{pageNumber}")
    @BussinessLog(value = "进入文章分类[{1}]列表第{2}页", platform = PlatformEnum.WEB)
    public ModelAndView type(@PathVariable("typeId") Long typeId, @PathVariable("pageNumber") Integer pageNumber, Model model) {
        ArticleConditionVO vo = new ArticleConditionVO();
        //设置文章vo的类型
        vo.setTypeId(typeId);
        //设置页码
        vo.setPageNumber(pageNumber);
        //设置url为指定类型的url
        model.addAttribute("url", "type/" + typeId);
        //加载指定类别指定页内容
        loadIndexPage(vo, model);

        //返回分页后的类型列表视图
        return ResultUtil.view(INDEX_URL);
    }

    /**
     * 标签列表
     *
     * @param tagId 标签ID
     * @param model
     * @return
     */
    @GetMapping("/tag/{tagId}")
    @BussinessLog(value = "进入文章标签[{1}]列表页", platform = PlatformEnum.WEB)
    public ModelAndView tag(@PathVariable("tagId") Long tagId, Model model) {
        ArticleConditionVO vo = new ArticleConditionVO();
        //设置文章vo的标签
        vo.setTagId(tagId);
        //设置url为指定标签的url
        model.addAttribute("url", "tag/" + tagId);
        //加载指定标签内容
        loadIndexPage(vo, model);

        //返回标签列表视图
        return ResultUtil.view(INDEX_URL);
    }

    /**
     * 标签列表（分页）
     *
     * @param tagId 标签ID
     * @param pageNumber 列表页码
     * @param model
     * @return
     */
    @GetMapping("/tag/{tagId}/{pageNumber}")
    @BussinessLog(value = "进入文章标签[{1}]列表第{2}页", platform = PlatformEnum.WEB)
    public ModelAndView tag(@PathVariable("tagId") Long tagId, @PathVariable("pageNumber") Integer pageNumber, Model model) {
        ArticleConditionVO vo = new ArticleConditionVO();
        //设置文章vo的标签
        vo.setTagId(tagId);
        //设置页码
        vo.setPageNumber(pageNumber);
        //设置url为指定标签的url
        model.addAttribute("url", "tag/" + tagId);
        //加载指定标签指定页内容
        loadIndexPage(vo, model);

        //返回分页后的标签列表视图
        return ResultUtil.view(INDEX_URL);
    }

    /**
     * 文章详情
     *
     * @param model
     * @param articleId 文章ID
     * @return
     */
    @GetMapping("/article/{articleId}")
    @BussinessLog(value = "进入文章[{2}]详情页", platform = PlatformEnum.WEB)
    public ModelAndView article(Model model, @PathVariable("articleId") Long articleId) {
        Article article = bizArticleService.getByPrimaryKey(articleId);
        //文章不存在或文章未发布时响应404
        if (article == null || ArticleStatusEnum.UNPUBLISHED.getCode() == article.getStatusEnum().getCode()) {
            return ResultUtil.forward("/error/404");
        }
        model.addAttribute("article", article);
        // 上一篇下一篇
        model.addAttribute("other", bizArticleService.getPrevAndNextArticles(article.getCreateTime()));
        // 相关文章
        model.addAttribute("relatedList", bizArticleService.listRelatedArticle(SIDEBAR_ARTICLE_SIZE, article));
        //文章内容
        model.addAttribute("articleDetail", true);
        //返回文章详情视图
        return ResultUtil.view("article");
    }

    /**
     * 关于
     *
     * @return
     */
    @GetMapping("/about")
    @BussinessLog(value = "进入关于页", platform = PlatformEnum.WEB)
    public ModelAndView about() {
        return ResultUtil.view("about");
    }//返回关于页视图

    /**
     * 友情链接
     *
     * @param model
     * @return
     */
    @GetMapping("/links")
    @BussinessLog(value = "进入友情链接页", platform = PlatformEnum.WEB)
    public ModelAndView links(Model model) {
        model.addAttribute("link", sysLinkService.listAllByGroup());
        //返回友链视图
        return ResultUtil.view("links");
    }

    /**
     * 留言板
     *
     * @return
     */
    @GetMapping("/guestbook")
    @BussinessLog(value = "进入留言板页", platform = PlatformEnum.WEB)
    public ModelAndView guestbook() {
        return ResultUtil.view("guestbook");
    }//返回留言板视图

    /**
     * 归档目录
     *
     * @param model
     * @return
     */
    @GetMapping("/archives")
    @BussinessLog(value = "进入归档目录页", platform = PlatformEnum.WEB)
    public ModelAndView archives(Model model) {
        //获取归档目录列表
        Map<String, List<Object> > map = bizArticleArchivesService.listArchives();
        model.addAttribute("archives", map);
        //返回归档目录视图
        return ResultUtil.view("archives");
    }

    /**
     * 免责声明
     *
     * @return
     */
    @GetMapping("/disclaimer")
    @BussinessLog(value = "进入免责声明页", platform = PlatformEnum.WEB)
    public ModelAndView disclaimer() {
        return ResultUtil.view("disclaimer");
    }//返回免责声明视图

    /**
     * 站长推荐
     *
     * @param model
     * @return
     */
    @GetMapping("/recommended")
    @BussinessLog(value = "进入站长推荐页", platform = PlatformEnum.WEB)
    public ModelAndView recommended(Model model) {
        model.addAttribute("list", bizArticleService.listRecommended(100));
        //返回站长推荐视图
        return ResultUtil.view("recommended");
    }

    /**
     * 更新日志
     *
     * @param model
     * @return
     */
    @GetMapping("/updateLog")
    @BussinessLog(value = "进入更新记录页", platform = PlatformEnum.WEB)
    public ModelAndView updateLog(Model model) {
        model.addAttribute("list", updateRecordeService.listAll());
        //返回更新日志视图
        return ResultUtil.view("updateLog");
    }

}
