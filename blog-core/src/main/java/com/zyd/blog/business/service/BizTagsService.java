package com.zyd.blog.business.service;


import com.github.pagehelper.PageInfo;
import com.zyd.blog.business.entity.Tags;
import com.zyd.blog.business.vo.TagsConditionVO;
import com.zyd.blog.framework.object.AbstractService;

/**
 * 标签
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
public interface BizTagsService extends AbstractService<Tags, Long> {

    /**
     * 分页查询
     *
     * @param vo 搜索条件
     * @return 查询结果表
     */
    PageInfo<Tags> findPageBreakByCondition(TagsConditionVO vo);

    /**
     * 按标签名获取标签对象
     *
     * @param name 标签名
     * @return 标签对象
     */
    Tags getByName(String name);
}
