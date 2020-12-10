package com.zyd.blog.runner;

import com.zyd.blog.core.schedule.ArticleLookTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 执行保存文章浏览记录任务
 *
 * @author kongchong
 * date: 2019-07-10 11:17
 */
@Component
public class TaskRunner implements ApplicationRunner {

    /**
     * 文章浏览记录保存
     */
    @Autowired
    private ArticleLookTask articleLookTask;

    /**
     * 执行文章浏览记录保存任务
     *
     * @param args
     */
    @Override
    public void run(ApplicationArguments args) {
        articleLookTask.save();
    }
}
