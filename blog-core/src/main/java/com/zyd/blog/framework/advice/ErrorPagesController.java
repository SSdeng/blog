package com.zyd.blog.framework.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 重写BasicErrorController,主要负责系统的异常页面的处理以及错误信息的显示
 * <p/>
 * 此处指需要记录
 * <p/>
 * 要注意，这个类里面的代码一定不能有异常或者潜在异常发生，否则可能会让程序陷入死循环。
 * <p/>
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @website https://www.zhyd.me
 * @version 1.0
 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Slf4j
@Controller
@RequestMapping("/error")
@EnableConfigurationProperties({ServerProperties.class})
public class ErrorPagesController implements ErrorController {


    //可以向用户显示错误或提供访问权限的属性
    private ErrorAttributes errorAttributes;

    //对于Web Server的外部化注释
    @Autowired
    private ServerProperties serverProperties;

    /**
     * 初始化ExceptionController，获取错误信息
     * @param errorAttributes
     */
    @Autowired
    public ErrorPagesController(ErrorAttributes errorAttributes) {

        //检查参数是否正确传入，没有正确传入则报错
        Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
        this.errorAttributes = errorAttributes;
    }

    /**
     * 处理PAGE NOT FOUND 404 的情况
     * @param request
     * @param response
     * @param webRequest web请求的通用接口。主要用于一般的web请求拦截器，允许它们访问一般的请求元数据，而不是用于实际处理请求。
     * @return
     */
    @RequestMapping("/404")
    public ModelAndView errorHtml404(HttpServletRequest request, HttpServletResponse response, WebRequest webRequest) {

        //将response的状态响应码设置为404
        response.setStatus(HttpStatus.NOT_FOUND.value());

        //获取网页的错误信息并存放入model中
        //MediaType选择HTML
        Map<String, Object> model = getErrorAttributes(webRequest, isIncludeStackTrace(request, MediaType.TEXT_HTML));

        //getQueryString 获取请求的URL字符串
        model.put("queryString", request.getQueryString());

        //ModelAndView是WebMVC框架中模型和视图的持有者。
        //创建一个新的ModelAndView，给定一个视图名和model
        return new ModelAndView("error/404", model);
    }

    //处理 403 FORBIDDEN 拒绝访问的情况
    @RequestMapping("/403")
    public ModelAndView errorHtml403(HttpServletRequest request, HttpServletResponse response, WebRequest webRequest) {

        //将response的状态响应码设置为403
        response.setStatus(HttpStatus.FORBIDDEN.value());

        //获取网页的错误信息并存放入model中
        //MediaType选择HTML
        Map<String, Object> model = getErrorAttributes(webRequest, isIncludeStackTrace(request, MediaType.TEXT_HTML));

        //getQueryString 获取请求的URL字符串
        // 404拦截规则，如果是静态文件发生的404则不记录到DB
        model.put("queryString", request.getQueryString());

        if (!String.valueOf(model.get("path")).contains(".")) {
            model.put("status", HttpStatus.FORBIDDEN.value());
        }

        //ModelAndView是WebMVC框架中模型和视图的持有者。
        //创建一个新的ModelAndView，给定一个视图名和model
        return new ModelAndView("error/403", model);
    }

    //处理 403 BAD REQUEST 的情况
    @RequestMapping("/400")
    public ModelAndView errorHtml400(HttpServletRequest request, HttpServletResponse response, WebRequest webRequest) {

        //将response的状态响应码设置为400
        response.setStatus(HttpStatus.BAD_REQUEST.value());

        //获取网页的错误信息并存放入model中
        Map<String, Object> model = getErrorAttributes(webRequest, isIncludeStackTrace(request, MediaType.TEXT_HTML));

        //getQueryString 获取请求的URL字符串
        model.put("queryString", request.getQueryString());

        //ModelAndView是WebMVC框架中模型和视图的持有者。
        //创建一个新的ModelAndView，给定一个视图名和model
        return new ModelAndView("error/400", model);
    }

    //处理 401 UNAUTHORIZED 的情况
    @RequestMapping("/401")
    public ModelAndView errorHtml401(HttpServletRequest request, HttpServletResponse response, WebRequest webRequest) {

        //将response的状态响应码设置为401
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        //获取网页的错误信息并存放入model中
        Map<String, Object> model = getErrorAttributes(webRequest, isIncludeStackTrace(request, MediaType.TEXT_HTML));

        //getQueryString 获取请求的URL字符串
        model.put("queryString", request.getQueryString());

        //ModelAndView是WebMVC框架中模型和视图的持有者。
        //创建一个新的ModelAndView，给定一个视图名和model
        return new ModelAndView("error/401", model);
    }

    //处理 500 INTERNAL_SERVER_ERROR 的情况
    @RequestMapping("/500")
    public ModelAndView errorHtml500(HttpServletRequest request, HttpServletResponse response, WebRequest webRequest) {

        //将response的状态响应码设置为500
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        //获取网页的错误信息并存放入model中
        Map<String, Object> model = getErrorAttributes(webRequest, isIncludeStackTrace(request, MediaType.TEXT_HTML));

        //getQueryString 获取请求的URL字符串
        model.put("queryString", request.getQueryString());

        //ModelAndView是WebMVC框架中模型和视图的持有者。
        //创建一个新的ModelAndView，给定一个视图名和model
        return new ModelAndView("error/500", model);
    }

    /**
     * Determine if the stacktrace attribute should be included.
     *
     * @param request
     *         the source request
     * @param produces
     *         the media type produced (or {@code MediaType.ALL})
     * @return if the stacktrace attribute should be included
     */

    //MediaType是添加了参数的MimeType，MimeType中封装了RFC 2046中定义的Internet协议，包括HTTP
    protected boolean isIncludeStackTrace(HttpServletRequest request,
                                          MediaType produces) {

        //获取StackTrace的选择
        ErrorProperties.IncludeStacktrace include = this.serverProperties.getError().getIncludeStacktrace();

        //如果是ALWAYS的话，直接返回true
        if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
            return true;
        }

        //如果是ON_TRACE_PARAM（不是NEVER）进行下一步判断
        //判断HttpServletRequest中是否包含trace
        return include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM && getTraceParameter(request);
    }


    /**
     * 获取错误的信息
     *
     * @param webRequest
     * @param includeStackTrace
     * @return
     */
    private Map<String, Object> getErrorAttributes(WebRequest webRequest,
                                                   boolean includeStackTrace) {
        //返回ErrorAttributes中包含的错误属性
        return this.errorAttributes.getErrorAttributes(webRequest, includeStackTrace);
    }

    /**
     * 是否包含trace
     *
     * @param request
     * @return
     */
    private boolean getTraceParameter(HttpServletRequest request) {

        //从HttpServletRequest中获得trace
        String parameter = request.getParameter("trace");

        //如果trace不为空
        //且trace的内容为false
        return parameter != null && !"false".equalsIgnoreCase(parameter);
    }

    /**
     * 获取错误编码
     *
     * @param request
     * @return
     */
    private HttpStatus getStatus(HttpServletRequest request) {

        //获取错误编码
        Integer statusCode = (Integer) request
                .getAttribute("javax.servlet.error.status_code");

        //如果发生错误而没有错误编码，则是服务器错误
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        //返回Http的状态
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {

            //如果返回status错误，则返回服务器内部错误
            log.error("获取当前HttpStatus发生异常", ex);
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    /**
     * 实现错误路径,暂时无用
     *
     * @return
     */
    @Override
    public String getErrorPath() {
        return "";
    }
}
