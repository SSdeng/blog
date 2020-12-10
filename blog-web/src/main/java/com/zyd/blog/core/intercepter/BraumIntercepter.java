package com.zyd.blog.core.intercepter;

import com.zyd.blog.util.RequestUtil;
import com.zyd.blog.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.braum.spring.boot.BraumProcessor;
import me.zhyd.braum.spring.boot.BraumResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * braum，自动识别恶意请求
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/11/19 9:24
 * @since 1.8
 */
@Component
@Slf4j
public class BraumIntercepter implements HandlerInterceptor {

    private static final int SUCCESS = 1;
    /**
     * Braum Web防护工具对象
     *
     * https://gitee.com/mybluedream/braum-spring-boot-starter
     */
    @Autowired
    private BraumProcessor processor;

    /**
     * 拦截恶意请求
     *
     * @param request HTTP请求
     * @param response HTTP响应
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        BraumResponse br = processor.process(request);//调用Braum进程判断请求
        if(br.getCode() == SUCCESS) {
            return true;
        }
        String errorMsg = String.format("第%s次被限制！", br.getLimitCount());
        log.warn(errorMsg);
        //设置拦截响应报文
        if(RequestUtil.isAjax(request)) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(ResultUtil.error(errorMsg).toJson());
            writer.flush();
            writer.close();
            return false;
        }
        request.setAttribute("errorMsg", errorMsg);
        request.setAttribute("expire", TimeUnit.MILLISECONDS.toSeconds(br.getExpire()));
        request.getRequestDispatcher("/error/403").forward(request, response);
        return false;
    }
}
