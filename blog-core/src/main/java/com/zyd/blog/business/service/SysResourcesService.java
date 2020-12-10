package com.zyd.blog.business.service;

import com.github.pagehelper.PageInfo;
import com.zyd.blog.business.entity.Resources;
import com.zyd.blog.business.vo.ResourceConditionVO;
import com.zyd.blog.framework.object.AbstractService;

import java.util.List;
import java.util.Map;

/**
 * 资源服务接口类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
public interface SysResourcesService extends AbstractService<Resources, Long> {

    /**
     * 分页查询
     * 使用PageHelper开源项目
     * @param vo 资源条件VO，包含分页参数
     * @return PageInfo
     */
    PageInfo<Resources> findPageBreakByCondition(ResourceConditionVO vo);

    /**
     * 获取用户的资源列表
     *
     * @param map 存储了用户id和资源类型
     * @return 资源列表
     */
    List<Resources> listUserResources(Map<String, Object> map);

    /**
     * 获取ztree使用的资源列表
     *
     * @param rid 资源id
     * @return 资源列表
     */
    List<Map<String, Object>> queryResourcesListWithSelected(Long rid);

    /**
     * 获取资源的url和permission
     *
     * @return 只包含url和permission的资源列表
     */
    List<Resources> listUrlAndPermission();

    /**
     * 获取所有可用的菜单资源
     *
     * @return 可用的菜单资源列表
     */
    List<Resources> listAllAvailableMenu();

    /**
     * 获取用户关联的所有资源
     *
     * @param userId 用户id
     * @return 用户关联的所有资源
     */
    List<Resources> listByUserId(Long userId);
}
