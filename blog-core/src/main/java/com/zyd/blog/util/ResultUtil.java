package com.zyd.blog.util;

import com.github.pagehelper.PageInfo;
import com.zyd.blog.business.consts.CommonConst;
import com.zyd.blog.framework.object.PageResult;
import com.zyd.blog.business.enums.ResponseStatus;
import com.zyd.blog.framework.object.ResponseVO;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 接口返回工具类，支持ModelAndView、ResponseVO、PageResult
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/18 11:48
 * @since 1.0
 */
public class ResultUtil {
	private ResultUtil() {
		throw new IllegalStateException();
	}

    //根据view通过ModelAnView工具创建ModelAndView
    public static ModelAndView view(String view) {
        return new ModelAndView(view);
    }
    //根据vie和模型modelw通过ModelAnView工具创建ModelAndView
    public static ModelAndView view(String view, Map<String, Object> model) {
        return new ModelAndView(view, model);
    }
    //根据view通过ModelAnView工具重定向ModelAndView
    public static ModelAndView redirect(String view) {
        return new ModelAndView("redirect:" + view);
    }
    //根据view通过ModelAnView工具重定向ModelAndView
    public static ModelAndView forward(String view) {
        return new ModelAndView("forward:" + view);
    }
    //根据回应的code和message返回错误的方法
    public static ResponseVO error(int code, String message) {
        return vo(code, message, null);
    }
    //根据回应的状态ResponseStatus返回错误的方法
    public static ResponseVO error(ResponseStatus status) {
        return vo(status.getCode(), status.getMessage(), null);
    }
    //根据回应的状态message返回错误的方法
    public static ResponseVO error(String message) {
        return vo(CommonConst.DEFAULT_ERROR_CODE, message, null);
    }
    //根据回应的状态message和data返回成功的方法
    public static ResponseVO success(String message, Object data) {
        return vo(CommonConst.DEFAULT_SUCCESS_CODE, message, data);
    }
    //根据回应的状态data返回成功的方法
    public static ResponseVO success(Object data) {
        return vo(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data);
    }
    //根据回应的状态message返回成功的方法
    public static ResponseVO success(String message) {
        return success(message, null);
    }
    //根据回应的状态ResponseStatus返回成功的方法
    public static ResponseVO success(ResponseStatus status) {
        return vo(status.getCode(), status.getMessage(), null);
    }

    public static ResponseVO vo(int code, String message, Object data) {
        return new ResponseVO<>(code, message, data);
    }

    public static PageResult tablePage(Long total, List<?> list) {
        return new PageResult(total, list);
    }

    /**
     * 根据页面的响应结果的信息info返回信息的数量和信息的表单
     * @param info
     * @return
     */
    public static PageResult tablePage(PageInfo info) {
        if (info == null) {
            return new PageResult(0L, new ArrayList());
        }
        return tablePage(info.getTotal(), info.getList());
    }

}
