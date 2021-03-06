package com.zyd.blog.controller;

import com.github.pagehelper.PageInfo;
import com.zyd.blog.business.annotation.BussinessLog;
import com.zyd.blog.business.entity.File;
import com.zyd.blog.business.service.BizFileService;
import com.zyd.blog.business.vo.FileConditionVO;
import com.zyd.blog.framework.object.ResponseVO;
import com.zyd.blog.util.ResultUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件管理
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2019/2/14 11:37
 * @since 1.0
 */
@RestController
@RequestMapping("/file")
public class RestFileController {
    @Autowired
    private BizFileService fileService;//文件业务层对象

    /**
     * 文件分页
     * @param vo 封装好的文件对象
     * @return bootstrap table用到的返回json格式
     */
    @RequiresPermissions("files")
    @PostMapping("/list")
    public PageInfo<File> list(FileConditionVO vo) {
        vo.setPageSize(20);
        return fileService.findPageBreakByCondition(vo);
    }

    /**
     * 删除文件
     * @param ids id
     * @return JSON
     */
    @RequiresPermissions("files")
    @PostMapping(value = "/remove")
    @BussinessLog("删除文件，ids:{1}")
    public ResponseVO<Object> remove(Long[] ids) {
        if (null == ids) {
            return ResultUtil.error(500, "请至少选择一条记录");
        }
        fileService.remove(ids);

        return ResultUtil.success("成功删除 [" + ids.length + "] 张图片");
    }

    /**
     * 添加文件
     * @param file 文件
     * @return JSON
     */
    @RequiresPermissions("files")
    @PostMapping(value = "/add")
    @BussinessLog("添加文件")
    public ResponseVO<Object> add(MultipartFile[] file) {
        if (null == file || file.length == 0) {
            return ResultUtil.error("请至少选择一张图片！");
        }
        int res = fileService.upload(file);
        return ResultUtil.success("成功上传" + res + "张图片");
    }
}
