package com.zyd.blog.core.aspects;

import com.zyd.blog.business.entity.ArticleLook;
import com.zyd.blog.core.schedule.ArticleLookTask;
import com.zyd.blog.framework.holder.RequestHolder;
import com.zyd.blog.util.IpUtil;
import com.zyd.blog.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 文章浏览记录aop操作
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/18 11:48
 * @since 1.0
 */
@Slf4j
@Component
@Aspect
@Order(1)
public class ArticleLookAspects {

    /**
     * 文章浏览记录保存执行器
     */
    @Autowired
    private ArticleLookTask task;

    /**
     * 设置匹配方法和切入点
     *
     *  格式：execution(modifiers-pattern? ret-type-pattern declaring-type-pattern? name-pattern(param-pattern)throws-pattern?)
     * （1）execution(* *(..))
     *  //表示匹配所有方法
     * （2）execution(public * com. savage.service.UserService.*(..))
     *  //表示匹配com.savage.server.UserService中所有的公有方法
     * （3）execution(* com.savage.server..*.*(..))
     *  //表示匹配com.savage.server包及其子包下的所有方法
     */
    @Pointcut("execution(* com.zyd.blog.controller.RenderController.article(..))")
    public void pointcut() {
        // 切面切入点
    }

    /**
     * 切入前置通知
     *
     * @param joinPoint 切面方法对象
     */
    @Before("pointcut()")
    public void doBefore(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();//获取参数对象
        if (args != null && args.length > 0) {
            String userIp = IpUtil.getRealIp(RequestHolder.getRequest());//获取请求IP
            Long articleId = (Long) args[1];//从参数对象中获取文章ID
            //创建浏览记录对象并设置各项属性
            ArticleLook articleLook = new ArticleLook();
            articleLook.setArticleId(articleId);
            articleLook.setUserIp(userIp);
            articleLook.setLookTime(new Date());
            if (SessionUtil.getUser() != null) {
                articleLook.setUserId(SessionUtil.getUser().getId());
            }
            task.addLookRecordToQueue(articleLook);//将包装好的浏览记录对象加入阻塞队列
        }
    }
}
