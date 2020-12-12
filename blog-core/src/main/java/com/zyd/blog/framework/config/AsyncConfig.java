package com.zyd.blog.framework.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

/**
 * 异步线程配置
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/28 11:04
 * @since 1.0
 */
@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    /**
     * 覆写函数，返回自定义线程池
     * @return 自定义线程池
     */
    @Override
    @Bean
    public Executor getAsyncExecutor() {
        return new ContextAwarePoolExecutor();
    }

    /**
     *捕捉处理线程抛出的异常
     * @return 自定义异常
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SpringAsyncExceptionHandler();
    }

    /**
     *自定义一个异步方法抛出的未捕获异常
     */
    class SpringAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

        //用于处理异步方法抛出的未捕获异常
        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
            //日志记录错误
            log.error("异步线程发生异常！Method [{}]，Error Message [{}]", method.getName(), throwable.getMessage());
        }
    }

    /**
     * 自定义一个线程池
     */
    public class ContextAwarePoolExecutor extends ThreadPoolTaskExecutor {


        /**
         * Callable类似与Runnable，不过Callable可以返回线程运行的结果
         * 提交值返回任务以执行，并返回结果
         * @param task
         * @param <T>
         * @return 线程运行的结果
         */
        @Override
        public <T> Future<T> submit(Callable<T> task) {
            return super.submit(new ContextAwareCallable(task, RequestContextHolder.currentRequestAttributes()));
        }

        /**
         *  提交一个Callable任务，将来完成时将会返回结果
         * @param task
         * @param <T>
         * @return 将来完成后返回完成的结果
         */
        @Override
        public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
            return super.submitListenable(new ContextAwareCallable(task, RequestContextHolder.currentRequestAttributes()));
        }
    }

    /**
     * 自定义Callable中的方法
     * @param <T>
     */
    public class ContextAwareCallable<T> implements Callable<T> {
        private Callable<T> task;

        //包含Request中的属性
        private RequestAttributes context;

        //构造函数
        public ContextAwareCallable(Callable<T> task, RequestAttributes context) {
            this.task = task;
            this.context = context;
        }

        /**
         * 自定义call方法
         * @return
         * @throws Exception
         */
        @Override
        public T call() throws Exception {
            //如果context为空，则将RequestContextHolder中的RequestAttributes设置为context
            if (context != null) {
                RequestContextHolder.setRequestAttributes(context);
            }

            try {
                //使用父类Callable中的call方法
                return task.call();
            } finally {
                //最后重置RequestContextHolder中的RequestAttributes
                RequestContextHolder.resetRequestAttributes();
            }
        }
    }
}
