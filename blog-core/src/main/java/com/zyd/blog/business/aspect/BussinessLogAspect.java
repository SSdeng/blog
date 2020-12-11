package com.zyd.blog.business.aspect;

import com.zyd.blog.business.annotation.BussinessLog;
import com.zyd.blog.business.enums.PlatformEnum;
import com.zyd.blog.business.service.SysLogService;
import com.zyd.blog.util.AspectUtil;
import com.zyd.blog.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * AOP切面记录日志
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Slf4j
@Aspect
@Component
public class BussinessLogAspect {

    @Autowired
    private SysLogService logService;//注入logService
    //1、匹配当前方法持有com.zyd.blog.business.annotation.BussinessLog注解的方法
    @Pointcut(value = "@annotation(com.zyd.blog.business.annotation.BussinessLog)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object writeLog(ProceedingJoinPoint point) throws Throwable {

        //先执行业务
        Object result = point.proceed();

        try {
            handle(point);
        } catch (Exception e) {
            log.error("日志记录出错!", e);
        }

        return result;
    }
        /**
         * 处理AOP切面日志
         */
    private void handle(ProceedingJoinPoint point) throws Exception {
        Method currentMethod = AspectUtil.INSTANCE.getMethod(point);
        //获取操作名称
        BussinessLog annotation = currentMethod.getAnnotation(BussinessLog.class);
        //获取是否操作是否存入数据库
        boolean save = annotation.save();
        //操作进行后台管理
        PlatformEnum platform = annotation.platform();
        String bussinessName = AspectUtil.INSTANCE.parseParams(point.getArgs(), annotation.value());
        String ua = RequestUtil.getUa();
        //以名字、IP、方法、URL和ua状况登录
        log.info("{} | {} - {} {} - {}", bussinessName, RequestUtil.getIp(), RequestUtil.getMethod(), RequestUtil.getRequestUrl(), ua);
        if (!save) {
            return;
        }
        //5、记录数据库操作
        logService.asyncSaveSystemLog(platform, bussinessName);
    }


}
