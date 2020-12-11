package com.zyd.blog.business.service;


import com.zyd.blog.framework.object.AbstractService;
import com.zyd.blog.business.entity.Type;
import com.zyd.blog.business.vo.TypeConditionVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 分类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
public interface BizTypeService extends AbstractService<Type, Long> {

    /**
     * 分页查询
     *
     * @param vo 查询条件
     * @return 符合条件的对象列表
     */
    PageInfo<Type> findPageBreakByCondition(TypeConditionVO vo);

    /**
     * 查询Parent
     *
     * @return Parent表
     */
    List<Type> listParent();

    /**
     * 获取类型菜单
     *
     * @return 类型菜单表
     */
    List<Type> listTypeForMenu();
}
