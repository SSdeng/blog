package com.zyd.blog.framework.advice;

import com.zyd.blog.business.consts.CommonConst;
import com.zyd.blog.business.enums.ResponseStatus;
import com.zyd.blog.file.exception.GlobalFileException;
import com.zyd.blog.framework.exception.ZhydException;
import com.zyd.blog.framework.holder.RequestHolder;
import com.zyd.blog.framework.object.ResponseVO;
import com.zyd.blog.util.RequestUtil;
import com.zyd.blog.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * 统一异常处理类<br>
 * 捕获程序所有异常，针对不同异常，采取不同的处理方式
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/24 14:37
 * @since 1.0
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandleController {

    /**
     * Shiro权限认证异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = {UnauthorizedException.class, AccountException.class})
    @ResponseBody
    public ResponseVO unauthorizedExceptionHandle(Throwable e) {
        e.printStackTrace(); // 打印异常栈

        //返回UNAUTHORIZED错误信息
        return ResultUtil.error(HttpStatus.UNAUTHORIZED.value(), e.getLocalizedMessage());
    }

    /**
     * Shiro权限认证异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = {MaxUploadSizeExceededException.class})
    @ResponseBody
    public ResponseVO maxUploadSizeExceededExceptionHandle(Throwable e) {
        e.printStackTrace(); // 打印异常栈
        //返回文件上传失败错误信息
        return ResultUtil.error(CommonConst.DEFAULT_ERROR_CODE, ResponseStatus.UPLOAD_FILE_ERROR.getMessage() + "文件过大！");
    }

    /**
     * MethodArgumentTypeMismatchException ： 方法参数类型异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public Object methodArgumentTypeMismatchException(Throwable e) {
        log.error("url参数异常，请检查参数类型是否匹配！", e);

        //获取RequestHolder中的request
        //判断request是否有效
        //如果无效，则返回参数错误信息
        if (RequestUtil.isAjax(RequestHolder.getRequest())) {
            return ResultUtil.error(ResponseStatus.INVALID_PARAMS);
        }

        //如果request有效，则是服务器内部错误
        return ResultUtil.forward("/error/500");
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseVO handle(Throwable e) {
        //如果是ZhydException及其子类，或GlobalFileException，则直接返回错误
        if (e instanceof ZhydException || e instanceof GlobalFileException) {
            return ResultUtil.error(e.getMessage());
        }

        //判断e是否是抛出的异常是检查型异常，而代理类在处理异常时没有发现该类型的异常在接口中声明
        if (e instanceof UndeclaredThrowableException) {
            e = ((UndeclaredThrowableException) e).getUndeclaredThrowable();
        }

        //用ResponseStatus中的参数进行判断，判断是否是在ResponseStatus中定义了的错误
        ResponseStatus responseStatus = ResponseStatus.getResponseStatus(e.getMessage());

        //如果是，则日志记录错误信息，并班会错误信息
        if (responseStatus != null) {

            //日志记录错误信息
            log.error(responseStatus.getMessage());

            //返回错误信息
            return ResultUtil.error(responseStatus.getCode(), responseStatus.getMessage());
        }

        //如果是未定义的错误，则打印出来
        e.printStackTrace(); // 打印异常栈

        //返回程序默认的错误代码
        return ResultUtil.error(CommonConst.DEFAULT_ERROR_CODE, ResponseStatus.ERROR.getMessage());
    }
}
