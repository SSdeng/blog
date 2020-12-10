package com.zyd.blog.business.service.impl;

import com.zyd.blog.business.entity.ArticleLook;
import com.zyd.blog.business.service.BizArticleLookService;
import com.zyd.blog.persistence.mapper.BizArticleLookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * 文章浏览记录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Service
public class BizArticleLookServiceImpl implements BizArticleLookService {

    @Autowired
    private BizArticleLookMapper bizArticleLookMapper;

    /**
     * 插入文章浏览记录
     *
     * @param entity 浏览记录
     * @return 更新后entity
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArticleLook insert(ArticleLook entity) {
        // entity不能为空
        Assert.notNull(entity, "ArticleLook不可为空！");
        // 更新时间设为当前时间
        entity.setUpdateTime(new Date());
        // 创建时间设为当前时间
        entity.setCreateTime(new Date());
        // 插入浏览记录
        bizArticleLookMapper.insertSelective(entity.getBizArticleLook());
        // 返回更新后的entity
        return entity;
    }
}
