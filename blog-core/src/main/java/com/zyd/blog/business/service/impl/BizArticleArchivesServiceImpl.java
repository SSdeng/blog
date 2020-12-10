package com.zyd.blog.business.service.impl;

import com.zyd.blog.business.entity.ArticleArchives;
import com.zyd.blog.business.service.BizArticleArchivesService;
import com.zyd.blog.persistence.beans.BizArticleArchives;
import com.zyd.blog.persistence.mapper.BizArticleArchivesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 归档目录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Service
public class BizArticleArchivesServiceImpl implements BizArticleArchivesService {

    /**
     *文章档案mapper
     */
    @Autowired
    private BizArticleArchivesMapper articleArchivesMapper;

    /**
     *生成归档目录 Map
     *
     * @return 归档目录列表
     */
    @Override
    public Map<String, List> listArchives() {
        // 获取文章档案列表
        List<BizArticleArchives> articleArchivesList = articleArchivesMapper.listArchives();
        // 若列表为空 返回空
        if (CollectionUtils.isEmpty(articleArchivesList)) {
            return null;
        }
        // 生成归档目录对象
        Map<String, List> resultMap = new HashMap<String, List>();
        // 生成存储年份的列表
        List<String> years = new LinkedList<>();
        // 遍历文章档案列表
        for (BizArticleArchives bizArticleArchives : articleArchivesList) {
            // 获取档案中存储的时间信息
            String datetime = bizArticleArchives.getDatetime();
            // 以“-”为分割符 分割datetime字符串
            String[] datetimeArr = datetime.split("-");
            String year = datetimeArr[0];
            String month = datetimeArr[1];
            String day = datetimeArr[2];
            String yearMonth = year + "-" + month;
            // 将年份存入对应表中
            addToList(years, year, null, null);
            // 将月份存入Map中键值year对应的list
            addToList(resultMap.get(year), month, resultMap, year);
            // 将日期存入Map中键值yearMonth对应的list
            addToList(resultMap.get(yearMonth), day, resultMap, yearMonth);
            // 将文章存入Map中键值datetime对应的list
            addToList(resultMap.get(datetime), new ArticleArchives(bizArticleArchives), resultMap, datetime);
        }
        // 将年份列表加入Map中
        resultMap.put("years", years);
        return resultMap;
    }

    /**
     * 将 value 存入 map 中 key 值对应的 list
     *
     * @param list 存入实值的列表
     * @param value 实值
     * @param map 存放list映射的map
     * @param key list在map中的键值
     * @param <T> 泛型
     */
    private <T> void addToList(List<T> list, T value, Map<String, List> map, String key) {
        // 若list为空
        if (null == list) {
            // 初始化后保存
            list = new LinkedList<>();
            list.add(value);
            // Map中没有对应键值的列表 存入该列表
            if (null != map && !StringUtils.isEmpty(key)) {
                map.put(key, list);
            }
        } else {
            // 去重
            if (!list.contains(value)) {
                list.add(value);
            }
        }
    }
}
