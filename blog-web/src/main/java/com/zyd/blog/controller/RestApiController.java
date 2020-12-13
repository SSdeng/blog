package com.zyd.blog.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zyd.blog.business.annotation.BussinessLog;
import com.zyd.blog.business.entity.Comment;
import com.zyd.blog.business.entity.Link;
import com.zyd.blog.business.enums.CommentStatusEnum;
import com.zyd.blog.business.enums.PlatformEnum;
import com.zyd.blog.business.service.BizArticleService;
import com.zyd.blog.business.service.BizCommentService;
import com.zyd.blog.business.service.SysLinkService;
import com.zyd.blog.business.service.SysNoticeService;
import com.zyd.blog.business.vo.CommentConditionVO;
import com.zyd.blog.framework.exception.ZhydArticleException;
import com.zyd.blog.framework.exception.ZhydCommentException;
import com.zyd.blog.framework.exception.ZhydLinkException;
import com.zyd.blog.framework.object.ResponseVO;
import com.zyd.blog.util.RestClientUtil;
import com.zyd.blog.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 网站接口类，申请友链、评论、点赞等
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/18 11:48
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class RestApiController {

    /**
     * 友情链接
     */
    @Autowired
    private SysLinkService sysLinkService;
    /**
     * 评论服务
     */
    @Autowired
    private BizCommentService commentService;
    /**
     * 文章列表
     */
    @Autowired
    private BizArticleService articleService;
    /**
     * 系统通知
     */
    @Autowired
    private SysNoticeService noticeService;

    /**
     * 申请友链
     *
     * @param link 友情链接
     * @param bindingResult 参数校验
     * @return
     */
    @PostMapping("/autoLink")
    @BussinessLog(value = "自助申请友链", platform = PlatformEnum.WEB)
    public ResponseVO<Object> autoLink(@Validated Link link, BindingResult bindingResult) {
        log.info("申请友情链接......");
        log.info(JSON.toJSONString(link));
        //参数校验错误时报错
        if (bindingResult.hasErrors()) {
            FieldError error;
            if((error = bindingResult.getFieldError())!=null){
                return ResultUtil.error(error.getDefaultMessage());
            }
            else{
                return ResultUtil.error("Field Error is Null!");
            }
        }
        try {
            sysLinkService.autoLink(link);//添加友链
        } catch (ZhydLinkException e) {
            log.error("客户端自助申请友链发生异常", e);
            return ResultUtil.error(e.getMessage());
        }
        //返回结果响应
        return ResultUtil.success("已成功添加友链，祝您生活愉快！");
    }

    /**
     * 获取QQ信息
     *
     * @param qq qq账号
     * @return
     */
    @PostMapping("/qq/{qq}")
    @BussinessLog(value = "获取QQ信息", platform = PlatformEnum.WEB)
    public ResponseVO<Object> qq(@PathVariable("qq") String qq) {
        //qq为空时报错
        if (StringUtils.isEmpty(qq)) {
            return ResultUtil.error("");
        }
        Map<String, String> resultMap = new HashMap<>(4);
        //默认名为匿名
        String nickname = "匿名";
        //qq地址
        String json = RestClientUtil.get("http://users.qzone.qq.com/fcg-bin/cgi_get_portrait.fcg?uins=" + qq, "GBK");
        if (!StringUtils.isEmpty(json)) {
            try {
                json = json.replaceAll("portraitCallBack|\\\\s*|\\t|\\r|\\n", "");
                json = json.substring(1, json.length() - 1);
                log.info(json);
                //字符串转JSON对象
                JSONObject object = JSON.parseObject(json);
                //字符串转JSON数组
                JSONArray array = object.getJSONArray(qq);
                nickname = array.getString(6);
            } catch (Exception e) {
                log.error("通过QQ号获取用户昵称发生异常", e);
            }
        }
        //设置结果属性
        resultMap.put("avatar", "https://q1.qlogo.cn/g?b=qq&nk=" + qq + "&s=40");
        resultMap.put("nickname", nickname);
        resultMap.put("email", qq + "@qq.com");
        resultMap.put("url", "https://user.qzone.qq.com/" + qq);
        //返回结果响应
        return ResultUtil.success(null, resultMap);
    }

    /**
     * 评论区
     *
     * @param vo 评论属性对象
     * @return
     */
    @PostMapping("/comments")
    @BussinessLog(value = "评论列表", platform = PlatformEnum.WEB, save = false)
    public ResponseVO<Object> comments(CommentConditionVO vo) {
        //设置评论vo的状态
        vo.setStatus(CommentStatusEnum.APPROVED.toString());
        //返回结果响应
        return ResultUtil.success(null, commentService.list(vo));
    }

    /**
     * 发表评论
     *
     * @param comment 评论对象
     * @return
     */
    @PostMapping("/comment")
    @BussinessLog(value = "发表评论", platform = PlatformEnum.WEB)
    public ResponseVO<Object> comment(Comment comment) {
        try {
            //执行发表评论
            commentService.comment(comment);
        } catch (ZhydCommentException e) {
            return ResultUtil.error(e.getMessage());
        }
        //返回结果响应
        return ResultUtil.success("评论发表成功，系统正在审核，请稍后刷新页面查看！");
    }

    /**
     * 点赞评论
     *
     * @param id 评论ID
     * @return
     */
    @PostMapping("/doSupport/{id}")
    @BussinessLog(value = "点赞评论{1}", platform = PlatformEnum.WEB)
    public ResponseVO<Object> doSupport(@PathVariable("id") Long id) {
        try {
            //执行点赞评论
            commentService.doSupport(id);
        } catch (ZhydCommentException e) {
            return ResultUtil.error(e.getMessage());
        }
        //返回结果响应
        return ResultUtil.success("");
    }

    /**
     * 点踩评论
     *
     * @param id 评论ID
     * @return
     */
    @PostMapping("/doOppose/{id}")
    @BussinessLog(value = "点踩评论{1}", platform = PlatformEnum.WEB)
    public ResponseVO<Object> doOppose(@PathVariable("id") Long id) {
        try {
            //执行点踩评论
            commentService.doOppose(id);
        } catch (ZhydCommentException e) {
            return ResultUtil.error(e.getMessage());
        }
        //返回结果响应
        return ResultUtil.success("");
    }

    /**
     * 点赞文章
     *
     * @param id 文章ID
     * @return
     */
    @PostMapping("/doPraise/{id}")
    @BussinessLog(value = "点赞文章{1}", platform = PlatformEnum.WEB)
    public ResponseVO<Object> doPraise(@PathVariable("id") Long id) {
        try {
            //执行点赞文章
            articleService.doPraise(id);
        } catch (ZhydArticleException e) {
            return ResultUtil.error(e.getMessage());
        }
        //返回结果响应
        return ResultUtil.success("");
    }

    /**
     * 查看公告列表
     *
     * @return
     */
    @PostMapping("/listNotice")
    @BussinessLog(value = "公告列表", platform = PlatformEnum.WEB, save = false)
    public ResponseVO<Object> listNotice() {
        return ResultUtil.success("", noticeService.listRelease());
    }//返回公告列表响应

}
