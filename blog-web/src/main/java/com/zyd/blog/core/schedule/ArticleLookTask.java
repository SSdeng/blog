package com.zyd.blog.core.schedule;

import com.zyd.blog.business.entity.ArticleLook;
import com.zyd.blog.business.service.BizArticleLookService;
import com.zyd.blog.business.service.BizArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 文章浏览记录保存类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2019/3/21 17:53
 * @since 1.8
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleLookTask {

    /**
     * 文章列表服务
     */
    private final BizArticleService bizArticleService;

    /**
     * 文章浏览记录服务
     */
    private final BizArticleLookService articleLookService;

    /**
     * 文章浏览记录阻塞队列
     */
    private BlockingQueue<ArticleLook> queue = new ArrayBlockingQueue<>(1024);

    /**
     * 在阻塞队列中保存文章的浏览记录，先进先出
     */
    public void addLookRecordToQueue(ArticleLook articleLook) {
        if (null == articleLook) {
            return;
        }
        if(!queue.offer(articleLook)){
            log.warn("文章浏览记录入队失败！", articleLook.getArticleId());
        }
    }

    /**
     * 取出阻塞队列中的文章浏览记录保存至服务
     */
    public void save() {
        List<ArticleLook> bufferList = new ArrayList<>();
        while (true) {
            try {
                bufferList.add(queue.take());//取出阻塞队列中的浏览记录
                //遍历浏览记录
                for (ArticleLook articleLook : bufferList) {
                    if (!bizArticleService.isExist(articleLook.getArticleId())) {
                        log.warn("{}-该文章不存在！", articleLook.getArticleId());
                        continue;
                    }
                    articleLookService.insert(articleLook);//加入文章浏览记录
                }
            } catch (InterruptedException e) {
                log.error("保存文章浏览记录失败--->[{}]", e.getMessage());
                // 防止缓冲队列填充数据出现异常时不断刷屏
                try {
                    Thread.sleep(1000);
                } catch (Exception err) {
                    log.error(err.getMessage());
                    // 中断线程
                    Thread.currentThread().interrupt();
                }
            } finally {
                bufferList.clear();
            }
        }
    }

}
