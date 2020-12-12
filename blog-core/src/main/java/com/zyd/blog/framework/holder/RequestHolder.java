package com.zyd.blog.framework.holder;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 用于保存request，response，session数据
 * 用户登陆时会将信息存在此类
 * 将用户信息存储在holder中，需要时可以直接取
 */
@Slf4j
public class RequestHolder {

    /**
     * 获取request
     *
     * @return HttpServletRequest 用户的HttpServletRequest
     */
    public static HttpServletRequest getRequest() {

        //日志记录获取request的线程信息
        log.debug("getRequest -- Thread id :{}, name : {}", Thread.currentThread().getId(), Thread.currentThread().getName());

        //从 RequestContextHolder中获得RequestAttributes信息
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());

        //如果获得的RequestAttributes信息为空，则直接返回空，避免报错
        if (null == servletRequestAttributes) {
            return null;
        }

        //如果不为空，则调用RequestAttributes的getRequest方法，获得request
        return servletRequestAttributes.getRequest();
    }

    /**
     * 获取Response
     *
     * @return HttpServletRequest
     */
    public static HttpServletResponse getResponse() {

        //日志记录获取response的线程信息
        log.debug("getResponse -- Thread id :{}, name : {}", Thread.currentThread().getId(), Thread.currentThread().getName());

        //从 RequestContextHolder中获得RequestAttributes信息
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());

        //如果获得的RequestAttributes信息为空，则直接返回空，避免报错
        if (null == servletRequestAttributes) {
            return null;
        }

        //如果不为空，则调用RequestAttributes的getRequest方法，获得response
        return servletRequestAttributes.getResponse();
    }

    /**
     * 获取session
     *
     * @return HttpSession
     */
    public static HttpSession getSession() {

        //日志记录获取session的线程信息
        log.debug("getSession -- Thread id :{}, name : {}", Thread.currentThread().getId(), Thread.currentThread().getName());

        HttpServletRequest request = null;

        //使用上面的getRequest方法获取HttpServletRequest，并判断是否为空
        if (null == (request = getRequest())) {
            return null;
        }

        //如果不为空，则从HttpServletRequest中取出session
        return request.getSession();
    }

    /**
     * 获取session的Attribute
     *
     * @param name session的key
     * @return Object
     */
    public static Object getSession(String name) {

        //日志记录获取session的线程信息
        log.debug("getSession -- Thread id :{}, name : {}", Thread.currentThread().getId(), Thread.currentThread().getName());

        //从 RequestContextHolder中获得RequestAttributes信息
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());

        //如果获得的RequestAttributes信息为空，则直接返回空，避免报错
        if (null == servletRequestAttributes) {
            return null;
        }

        //根据name直接从RequestAttributes中获取attribute的信息
        //设置RequestAttributes取的种类为session
        return servletRequestAttributes.getAttribute(name, RequestAttributes.SCOPE_SESSION);
    }

    /**
     * 添加session
     *
     * @param name 添加session的名字
     * @param value 添加session的值
     */
    public static void setSession(String name, Object value) {

        //日志记录添加session的线程信息
        log.debug("setSession -- Thread id :{}, name : {}", Thread.currentThread().getId(), Thread.currentThread().getName());

        //从 RequestContextHolder中获得 RequestAttributes 信息
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());

        //如果获得的RequestAttributes信息为空，则直接结束函数，避免报错
        if (null == servletRequestAttributes) {
            return;
        }

        //如果不为空，则向RequestAttributes中添加session信息
        servletRequestAttributes.setAttribute(name, value, RequestAttributes.SCOPE_SESSION);
    }

    /**
     * 清除指定session
     *
     * @param name 移除session的名字
     * @return void
     */
    public static void removeSession(String name) {

        //日志记录删除session的线程信息
        log.debug("removeSession -- Thread id :{}, name : {}", Thread.currentThread().getId(), Thread.currentThread().getName());

        //从 RequestContextHolder中获得 RequestAttributes 信息
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());

        //如果获得的RequestAttributes信息为空，则直接结束函数，避免报错
        if (null == servletRequestAttributes) {
            return;
        }

        //如果不为空，则根据name直接从RequestAttributes中移除session信息
        servletRequestAttributes.removeAttribute(name, RequestAttributes.SCOPE_SESSION);
    }

    /**
     * 获取所有session key
     *
     * @return String[] 所有的 session key
     */
    public static String[] getSessionKeys() {

        //日志记录获取session的线程信息
        log.debug("getSessionKeys -- Thread id :{}, name : {}", Thread.currentThread().getId(), Thread.currentThread().getName());

        //从 RequestContextHolder中获得 RequestAttributes 信息
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());

        //如果获得的RequestAttributes信息为空，则直接结束函数，避免报错
        if (null == servletRequestAttributes) {
            return null;
        }

        //如果不为空，从RequestAttributes中获取所有session的key
        return servletRequestAttributes.getAttributeNames(RequestAttributes.SCOPE_SESSION);
    }
}
