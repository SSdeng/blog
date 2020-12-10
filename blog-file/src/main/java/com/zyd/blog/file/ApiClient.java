package com.zyd.blog.file;

import com.zyd.blog.file.entity.VirtualFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

/**
 * API客户端接口
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2019/2/11 14:37
 * @since 1.8
 */
public interface ApiClient {

    /**
     * 上传文件
     *
     * @param file MultipartFile对象
     * @return 文件实体
     */
    VirtualFile uploadImg(MultipartFile file);

    /**
     * 上传文件
     *
     * @param file File对象
     * @return 文件实体
     */
    VirtualFile uploadImg(File file);

    /**
     * 上传文件
     *
     * @param is 要上传文件的输入流
     * @param key 文件key
     * @return 文件实体
     */
    VirtualFile uploadImg(InputStream is, String key);

    /**
     * 删除文件
     *
     * @param key 文件key
     * @return 执行结果
     */
    boolean removeFile(String key);
}
