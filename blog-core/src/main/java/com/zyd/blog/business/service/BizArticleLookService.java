package com.zyd.blog.business.service;


import com.zyd.blog.business.entity.ArticleLook;

/**
 * 文章浏览记录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
public interface BizArticleLookService {

    /**
     * 插入文章浏览记录
     *
     * @param articleLook 浏览记录
     * @return 更新后的articleLook
     */
    ArticleLook insert(ArticleLook articleLook);
}
