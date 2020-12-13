package com.zyd.blog.controller;

import com.github.pagehelper.PageInfo;
import com.zyd.blog.business.annotation.BussinessLog;
import com.zyd.blog.business.entity.Comment;
import com.zyd.blog.business.enums.ResponseStatus;
import com.zyd.blog.business.enums.TemplateKeyEnum;
import com.zyd.blog.business.service.BizCommentService;
import com.zyd.blog.business.service.MailService;
import com.zyd.blog.business.vo.CommentConditionVO;
import com.zyd.blog.framework.exception.ZhydCommentException;
import com.zyd.blog.framework.object.PageResult;
import com.zyd.blog.framework.object.ResponseVO;
import com.zyd.blog.util.ResultUtil;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 评论管理
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/24 14:37
 * @since 1.0
 */
@RestController
@RequestMapping("/comment")
public class RestCommentController {
    @Autowired
    //评论业务层对象
    private BizCommentService commentService;
    @Autowired
    //邮件业务层对象
    private MailService mailService;

    /**
     * 评论分页
     * @param vo 封装好的评论对象
     * @return bootstrap table用到的返回json格式
     */
    @RequiresPermissions("comments")
    @PostMapping("/list")
    public PageResult list(CommentConditionVO vo) {
        PageInfo<Comment> pageInfo = commentService.findPageBreakByCondition(vo);
        return ResultUtil.tablePage(pageInfo);
    }

    /**
     * 回复评论
     * @param comment 评论
     * @return JSON
     */
    @RequiresPermissions("comment:reply")
    @PostMapping(value = "/reply")
    @BussinessLog("回复评论")
    public ResponseVO<Object> reply(Comment comment) {
        try {
            commentService.commentForAdmin(comment);
        } catch (ZhydCommentException e){
            return ResultUtil.error(e.getMessage());
        }
        return ResultUtil.success("成功");
    }

    /**
     * 删除评论
     * @param ids id
     * @return JSON
     */
    @RequiresPermissions(value = {"comment:batchDelete", "comment:delete"}, logical = Logical.OR)
    @PostMapping(value = "/remove")
    @BussinessLog("删除评论[{1}]")
    public ResponseVO<Object> remove(Long[] ids) {
        if (null == ids) {
            return ResultUtil.error(500, "请至少选择一条记录");
        }
        for (Long id : ids) {
            commentService.removeByPrimaryKey(id);
        }
        return ResultUtil.success("成功删除 [" + ids.length + "] 条评论");
    }

    /**
     * 获取评论详情
     * @param id id
     * @return JSON
     */
    @RequiresPermissions("comments")
    @PostMapping("/get/{id}")
    @BussinessLog("获取评论[{1}]详情")
    public ResponseVO<Object> get(@PathVariable Long id) {
        return ResultUtil.success(null, this.commentService.getByPrimaryKey(id));
    }

    /**
     * 编辑评论
     * @param comment 评论
     * @return JSON
     */
    @RequiresPermissions("comments")
    @PostMapping("/edit")
    @BussinessLog("编辑评论")
    public ResponseVO<Object> edit(Comment comment) {
        try {
            commentService.updateSelective(comment);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error("评论修改失败！");
        }
        return ResultUtil.success(ResponseStatus.SUCCESS);
    }

    /**
     * 审核评论
     * @param comment 评论
     * @param contentText 内容
     * @param sendEmail 是否发送邮件
     * @return JSON
     */
    @RequiresPermissions("comment:audit")
    @PostMapping("/audit")
    @BussinessLog("审核评论")
    public ResponseVO<Object> audit(Comment comment, String contentText, Boolean sendEmail) {
        try {
            commentService.updateSelective(comment);
            if(!StringUtils.isEmpty(contentText)){
                comment.setContent(contentText);
                commentService.commentForAdmin(comment);
            }
            if(null != sendEmail && sendEmail){
                Comment commentDB = commentService.getByPrimaryKey(comment.getId());
                mailService.send(commentDB, TemplateKeyEnum.TM_COMMENT_AUDIT, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error("评论审核失败！");
        }
        return ResultUtil.success(ResponseStatus.SUCCESS);
    }

    /**
     * 正在审核的评论列表
     *
     * @param comment 评论
     * @return JSON
     */
    @RequiresUser
    @PostMapping("/listVerifying")
    public ResponseVO<Object> listVerifying(Comment comment) {
        return ResultUtil.success(null, commentService.listVerifying(10));
    }

}
