package com.zyd.blog.controller;

import com.github.pagehelper.PageInfo;
import com.zyd.blog.business.annotation.BussinessLog;
import com.zyd.blog.business.entity.UpdateRecorde;
import com.zyd.blog.business.enums.ResponseStatus;
import com.zyd.blog.business.service.SysUpdateRecordeService;
import com.zyd.blog.business.vo.UpdateRecordeConditionVO;
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
 * 系统更新日志
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/24 14:37
 * @since 1.0
 */
@RestController
@RequestMapping("/update")
public class RestUpdateController {
    @Autowired
    private SysUpdateRecordeService updateRecordeService;//更新日志业务层

    /**
     * 更新日志分页
     * @param vo 封装好的更新日志对象
     * @return bootstrap table用到的返回json格式
     */
    @RequiresPermissions("updateLogs")
    @PostMapping("/list")
    public PageResult list(UpdateRecordeConditionVO vo) {
        PageInfo<UpdateRecorde> pageInfo = updateRecordeService.findPageBreakByCondition(vo);
        return ResultUtil.tablePage(pageInfo);
    }

    /**
     * 添加更新日志
     * @param updateRecorde 更新日志
     * @return JSON
     */
    @RequiresPermissions("updateLog:add")
    @PostMapping(value = "/add")
    @BussinessLog("添加更新日志")
    public ResponseVO<Object> add(UpdateRecorde updateRecorde) {
        updateRecordeService.insert(updateRecorde);
        return ResultUtil.success("成功");
    }

    /**
     * 删除更新日志
     * @param ids id
     * @return JSON
     */
    @RequiresPermissions(value = {"updateLog:batchDelete", "updateLog:delete"}, logical = Logical.OR)
    @PostMapping(value = "/remove")
    @BussinessLog("删除更新日志")
    public ResponseVO<Object> remove(Long[] ids) {
        if (null == ids) {
            return ResultUtil.error(500, "请至少选择一条记录");
        }
        for (Long id : ids) {
            updateRecordeService.removeByPrimaryKey(id);
        }
        return ResultUtil.success("成功删除 [" + ids.length + "] 个更新记录");
    }

    /**
     * 获取更新日志详情
     * @param id id
     * @return JSON
     */
    @RequiresPermissions("updateLog:get")
    @PostMapping("/get/{id}")
    @BussinessLog("获取更新日志详情")
    public ResponseVO<Object> get(@PathVariable Long id) {
        return ResultUtil.success(null, this.updateRecordeService.getByPrimaryKey(id));
    }

    /**
     * 编辑更新日志
     * @param updateRecorde 更新日志
     * @return JSON
     */
    @RequiresPermissions("updateLog:edit")
    @PostMapping("/edit")
    @BussinessLog("编辑更新日志")
    public ResponseVO<Object> edit(UpdateRecorde updateRecorde) {
        try {
            updateRecordeService.updateSelective(updateRecorde);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error("更新记录修改失败！");
        }
        return ResultUtil.success(ResponseStatus.SUCCESS);
    }

}
