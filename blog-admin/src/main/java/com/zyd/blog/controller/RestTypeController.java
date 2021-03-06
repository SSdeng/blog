package com.zyd.blog.controller;

import com.github.pagehelper.PageInfo;
import com.zyd.blog.business.annotation.BussinessLog;
import com.zyd.blog.business.entity.Type;
import com.zyd.blog.business.enums.ResponseStatus;
import com.zyd.blog.business.service.BizTypeService;
import com.zyd.blog.business.vo.TypeConditionVO;
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

/**
 * 文章类型管理
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/24 14:37
 * @since 1.0
 */
@RestController
@RequestMapping("/type")
public class RestTypeController {
    @Autowired
    private BizTypeService typeService;//文章类型业务层

    /**
     * 文章类型分页显示
     * @param vo 封装好的类型对象
     * @return bootstrap table用到的返回json格式
     */
    @RequiresPermissions("types")
    @PostMapping("/list")
    public PageResult list(TypeConditionVO vo) {
        vo.setPageSize(Integer.MAX_VALUE);
        PageInfo<Type> pageInfo = typeService.findPageBreakByCondition(vo);
        return ResultUtil.tablePage(pageInfo);
    }

    /**
     * 添加分类
     * @param type 类型
     * @return JSON
     */
    @RequiresPermissions("type:add")
    @PostMapping(value = "/add")
    @BussinessLog("添加分类")
    public ResponseVO<Object> add(Type type) {
        typeService.insert(type);
        return ResultUtil.success("文章类型添加成功！新类型 - " + type.getName());
    }

    /**
     * 删除分类
     * @param ids id
     * @return JSON
     */
    @RequiresPermissions(value = {"type:batchDelete", "type:delete"}, logical = Logical.OR)
    @PostMapping(value = "/remove")
    @BussinessLog("删除分类")
    public ResponseVO<Object> remove(Long[] ids) {
        if (null == ids) {
            return ResultUtil.error(500, "请至少选择一条记录");
        }
        for (Long id : ids) {
            typeService.removeByPrimaryKey(id);
        }
        return ResultUtil.success("成功删除 [" + ids.length + "] 个文章类型");
    }

    /**
     * 获取分类详情
     * @param id id
     * @return JSON
     */
    @RequiresPermissions("type:get")
    @PostMapping("/get/{id}")
    @BussinessLog("获取分类详情")
    public ResponseVO<Object> get(@PathVariable Long id) {
        return ResultUtil.success(null, this.typeService.getByPrimaryKey(id));
    }

    /**
     * 编辑分类
     * @param type 类型
     * @return JSON
     */
    @RequiresPermissions("type:edit")
    @PostMapping("/edit")
    @BussinessLog("编辑分类")
    public ResponseVO<Object> edit(Type type) {
        try {
            typeService.updateSelective(type);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error("文章类型修改失败！");
        }
        return ResultUtil.success(ResponseStatus.SUCCESS);
    }

    /**
     * 显示所有文章类型
     * @return JSON
     */
    @PostMapping("/listAll")
    public ResponseVO<Object> listType() {
        return ResultUtil.success(null, typeService.listTypeForMenu());
    }

    /**
     * 显示父类型
     * @return JSON
     */
    @PostMapping("/listParent")
    public ResponseVO<Object> listParent() {
        return ResultUtil.success(null, typeService.listParent());
    }

}
