package com.zyd.blog.util;

import com.zyd.blog.framework.holder.RequestHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/18 11:48
 * @since 1.0
 */
public class RequestUtil {
    /**
     * 获取HTTPServlet 请求参数
     * @return 请求参数
     */
    public static String getParameters() {
        HttpServletRequest request = RequestHolder.getRequest();
        if (null == request) {
            return null;
        }
        /**
         * 通过getParameterNames()方法获取表单组件的value值
         */
        Enumeration<String> paraNames = request.getParameterNames();
        if (paraNames == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        /**
         * 遍历，获得表单控件的所有枚举的name属性的值
         */
        while (paraNames.hasMoreElements()) {
            String paraName = paraNames.nextElement();
            sb.append("&").append(paraName).append("=").append(request.getParameter(paraName));
        }
        return sb.toString();
    }

    public static Map<String, String[]> getParametersMap() {
        HttpServletRequest request = RequestHolder.getRequest();
        if (null == request) {
            return new HashMap<>();
        }
        return request.getParameterMap();
    }

    /**
     * 获取头部信息
     * @param headerName
     * @return 头部姓名
     */
    public static String getHeader(String headerName) {
        HttpServletRequest request = RequestHolder.getRequest();
        if (null == request) {
            return null;
        }
        return request.getHeader(headerName);
    }

    /**
     * 得到请求来源
     * @return 头部信息
     */
    public static String getReferer() {
        return getHeader("Referer");
    }

    /**
     * 获取用户代理
     * @return 头部信息
     */
    public static String getUa() {
        return getHeader("User-Agent");
    }

    /**
     * 获得请求的IP
     * @return 请求IP
     */
    public static String getIp() {
        HttpServletRequest request = RequestHolder.getRequest();
        if (null == request) {
            return null;
        }
        return IpUtil.getRealIp(request);
    }

    /**
     * 获取请求的Url
     * @return 请求URL
     */
    public static String getRequestUrl() {
        HttpServletRequest request = RequestHolder.getRequest();
        if (null == request) {
            return null;
        }
        return request.getRequestURL().toString();
    }

    /**
     * 获取请求使用的方法
     * @return 方法
     */
    public static String getMethod() {
        HttpServletRequest request = RequestHolder.getRequest();
        if (null == request) {
            return null;
        }
        return request.getMethod();
    }

    /**
     * 判断请求是否是Ajax
     * @param request
     * @return
     */
    public static boolean isAjax(HttpServletRequest request) {
        if (null == request) {
            request = RequestHolder.getRequest();
        }
        if (null == request) {
            return false;
        }
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))
                || request.getParameter("ajax") != null;

    }

}
