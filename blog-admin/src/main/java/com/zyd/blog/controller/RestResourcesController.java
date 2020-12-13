package com.zyd.blog.controller;

import com.github.pagehelper.PageInfo;
import com.zyd.blog.business.annotation.BussinessLog;
import com.zyd.blog.business.entity.Resources;
import com.zyd.blog.business.enums.ResponseStatus;
import com.zyd.blog.business.service.SysResourcesService;
import com.zyd.blog.business.vo.ResourceConditionVO;
import com.zyd.blog.core.shiro.ShiroService;
import com.zyd.blog.framework.object.PageResult;
import com.zyd.blog.framework.object.ResponseVO;
import com.zyd.blog.util.ResultUtil;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统资源管理
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/24 14:37
 * @since 1.0
 */
@RestController
@RequestMapping("/resources")
public class RestResourcesController {

    @Autowired
    private SysResourcesService resourcesService;//资源业务层对象
    @Autowired
    private ShiroService shiroService;//Shiro业务层对象

    /**
     * 资源分页
     * @param vo 封装好的资源对象
     * @return bootstrap table用到的返回json格式
     */
    @RequiresPermissions("resources")
    @PostMapping("/list")
    public PageResult getAll(ResourceConditionVO vo) {
        vo.setPageSize(Integer.MAX_VALUE);
        PageInfo<Resources> pageInfo = resourcesService.findPageBreakByCondition(vo);
        return ResultUtil.tablePage(pageInfo);
    }

    /**
     * 显示选择的资源列表
     * @param rid 资源id
     * @return JSON
     */
    @RequiresPermissions("role:allotResource")
    @PostMapping("/resourcesWithSelected")
    public ResponseVO<List<Resources>> resourcesWithSelected(Long rid) {
        return ResultUtil.success(null, resourcesService.queryResourcesListWithSelected(rid));
    }

    /**
     * 添加资源
     * @param resources 资源
     * @return JSON
     */
    @RequiresPermissions("resource:add")
    @PostMapping(value = "/add")
    @BussinessLog("添加资源")
    public ResponseVO<Object> add(Resources resources) {
        resourcesService.insert(resources);
        //更新权限
        shiroService.updatePermission();
        return ResultUtil.success("成功");
    }

    /**
     * 删除资源
     * @param ids id
     * @return JSON
     */
    @RequiresPermissions(value = {"resource:batchDelete", "resource:delete"}, logical = Logical.OR)
    @PostMapping(value = "/remove")
    @BussinessLog("删除资源")
    public ResponseVO<Object> remove(Long[] ids) {
        if (null == ids) {
            return ResultUtil.error(500, "请至少选择一条记录");
        }
        for (Long id : ids) {
            resourcesService.removeByPrimaryKey(id);
        }

        //更新权限
        shiroService.updatePermission();
        return ResultUtil.success("成功删除 [" + ids.length + "] 个资源");
    }

    /**
     * 获取资源详情
     * @param id id
     * @return JSON
     */
    @RequiresPermissions("resource:get")
    @PostMapping("/get/{id}")
    @BussinessLog("获取资源详情")
    public ResponseVO<Object> get(@PathVariable Long id) {
        return ResultUtil.success(null, this.resourcesService.getByPrimaryKey(id));
    }

    /**
     * 编辑资源
     * @param resources 资源
     * @return JSON
     */
    @RequiresPermissions("resource:edit")
    @PostMapping("/edit")
    @BussinessLog("编辑资源")
    public ResponseVO<Object> edit(Resources resources) {
        try {
            resourcesService.updateSelective(resources);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error("资源修改失败！");
        }
        return ResultUtil.success(ResponseStatus.SUCCESS);
    }
}
