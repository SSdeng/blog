package com.zyd.blog.business.service.impl;


import com.github.pagehelper.page.PageMethod;
import com.zyd.blog.business.entity.Article;
import com.zyd.blog.business.entity.Statistics;
import com.zyd.blog.business.service.BizArticleService;
import com.zyd.blog.business.service.BizStatisticsService;
import com.zyd.blog.persistence.beans.BizStatistics;
import com.zyd.blog.persistence.mapper.BizStatisticsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 统计
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Service
public class BizStatisticsServiceImpl implements BizStatisticsService {

    @Autowired
    private BizStatisticsMapper statisticsMapper;
    @Autowired
    private BizArticleService articleService;

    /**
     * 获取热门文章
     *
     * @param pageSize 分页大小
     * @return 热门文章列表
     */
    @Override
    public List<Article> listHotArticle(int pageSize) {
        return articleService.listHotArticle(pageSize);
    }

    /**
     * 获取爬虫统计
     *
     * @param pageSize 分页大小
     * @return 统计结果表
     */
    @Override
    public List<Statistics> listSpider(int pageSize) {
        // 分页
        PageMethod.startPage(1, pageSize);
        // 获取统计表
        List<BizStatistics> entityList = statisticsMapper.listSpider();
        // 空表
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        // 拷贝 转换元素类型
        List<Statistics> list = new ArrayList<>();
        for (BizStatistics entity : entityList) {
            list.add(new Statistics(entity));
        }
        // 返回结果
        return list;
    }

    /**
     * 获取文章分类统计
     *
     * @param pageSize 分页大小
     * @return 文章分类统计表
     */
    @Override
    public List<Statistics> listType(int pageSize) {
        // 分页
        PageMethod.startPage(1, pageSize);
        // 获取文章分类统计表
        List<BizStatistics> entityList = statisticsMapper.listType();
        // 空表
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        // 拷贝 转换元素类型
        List<Statistics> list = new ArrayList<>();
        for (BizStatistics entity : entityList) {
            list.add(new Statistics(entity));
        }
        // 返回结果
        return list;
    }

}
