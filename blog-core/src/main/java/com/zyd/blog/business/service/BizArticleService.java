package com.zyd.blog.business.service;


import com.github.pagehelper.PageInfo;
import com.zyd.blog.business.entity.Article;
import com.zyd.blog.business.vo.ArticleConditionVO;
import com.zyd.blog.framework.object.AbstractService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 文章列表
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
public interface BizArticleService extends AbstractService<Article, Long> {

    /**
     * 分页查询
     *
     * @param vo 文章状态规定
     * @return 符合条件的文章列表
     */
    PageInfo<Article> findPageBreakByCondition(ArticleConditionVO vo);

    /**
     * 站长推荐
     *
     * @param pageSize 分页大小
     * @return 站长推荐文章列表
     */
    List<Article> listRecommended(int pageSize);

    /**
     * 近期文章
     *
     * @param pageSize 分页大小
     * @return 近期文章列表
     */
    List<Article> listRecent(int pageSize);

    /**
     * 随机文章
     *
     * @param pageSize 分页大小
     * @return 随机文章列表
     */
    List<Article> listRandom(int pageSize);

    /**
     * 获取热门文章
     *
     * @param pageSize 分页大小
     * @return 热门文章列表
     */
    List<Article> listHotArticle(int pageSize);

    /**
     * 根据某篇文章获取与该文章相关的文章<br>
     * 搜索同类型、同标签下的文章
     *
     * @param pageSize 分页大小
     * @param article 原型文章
     * @return 类似文章列表
     */
    List<Article> listRelatedArticle(int pageSize, Article article);

    /**
     * 获取上一篇和下一篇
     *
     * @param insertTime 当前文章创建时间
     * @return 存储两篇文章的Map对象
     */
    Map<String, Article> getPrevAndNextArticles(Date insertTime);

    /**
     * 文章点赞
     *
     * @param id 点赞文章id
     */
    void doPraise(Long id);

    /**
     * 查询是否存在文章
     *
     * @param id 查询文章id
     * @return 存在返回true 否则返回false
     */
    boolean isExist(Long id);

    /**
     * 发布文章
     *
     * @param article 待发布文章
     * @param tags 文章相关tag
     * @param file 封面图片文件
     * @return 发布结果
     */
    boolean publish(Article article, Long[] tags, MultipartFile file);

    /**
     * 修改置顶、推荐
     *
     * @param type 操作类型
     * @param id 文章id
     * @return 操作结果
     */
    boolean updateTopOrRecommendedById(String type, Long id);

    /**
     * 批量修改status
     *
     * @param ids 待修改id表
     * @param status 修改值
     */
    void batchUpdateStatus(Long[] ids, boolean status);

}
