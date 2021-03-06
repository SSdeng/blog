package com.zyd.blog.controller;

import com.github.pagehelper.PageInfo;
import com.zyd.blog.business.annotation.BussinessLog;
import com.zyd.blog.business.entity.Tags;
import com.zyd.blog.business.enums.ResponseStatus;
import com.zyd.blog.business.service.BizTagsService;
import com.zyd.blog.business.vo.TagsConditionVO;
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
 * 文章标签管理
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/24 14:37
 * @since 1.0
 */
@RestController
@RequestMapping("/tag")
public class RestTagController {
    @Autowired
    private BizTagsService tagsService;//标签业务层

    /**
     * 标签分页
     * @param vo 封装好的标签对象
     * @return bootstrap table用到的返回json格式
     */
    @RequiresPermissions("tags")
    @PostMapping("/list")
    public PageResult list(TagsConditionVO vo) {
        PageInfo<Tags> pageInfo = tagsService.findPageBreakByCondition(vo);
        return ResultUtil.tablePage(pageInfo);
    }

    /**
     * 添加标签
     * @param tags 标签
     * @return JSON
     */
    @RequiresPermissions("tag:add")
    @PostMapping(value = "/add")
    @BussinessLog("添加标签")
    public ResponseVO<Object> add(Tags tags) {
        tags = tagsService.insert(tags);
        return ResultUtil.success("标签添加成功！新标签 - " + tags.getName(), tags);
    }

    /**
     * 删除标签
     * @param ids id
     * @return JSON
     */
    @RequiresPermissions(value = {"tag:batchDelete", "tag:delete"}, logical = Logical.OR)
    @PostMapping(value = "/remove")
    @BussinessLog("删除标签")
    public ResponseVO<Object> remove(Long[] ids) {
        if (null == ids) {
            return ResultUtil.error(500, "请至少选择一条记录");
        }
        for (Long id : ids) {
            tagsService.removeByPrimaryKey(id);
        }
        return ResultUtil.success("成功删除 [" + ids.length + "] 个标签");
    }

    /**
     * 获取标签详情
     * @param id id
     * @return JSON
     */
    @RequiresPermissions("tag:get")
    @PostMapping("/get/{id}")
    @BussinessLog("获取标签详情")
    public ResponseVO<Object> get(@PathVariable Long id) {
        return ResultUtil.success(null, this.tagsService.getByPrimaryKey(id));
    }

    /**
     * 编辑标签
     * @param tags 标签
     * @return JSON
     */
    @RequiresPermissions("tag:edit")
    @PostMapping("/edit")
    @BussinessLog("编辑标签")
    public ResponseVO<Object> edit(Tags tags) {
        try {
            tagsService.updateSelective(tags);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error("标签修改失败！");
        }
        return ResultUtil.success(ResponseStatus.SUCCESS);
    }

    /**
     * 显示所有标签
     * @return JSON
     */
    @PostMapping("/listAll")
    public ResponseVO<Object> list() {
        return ResultUtil.success(null, tagsService.listAll());
    }

}
