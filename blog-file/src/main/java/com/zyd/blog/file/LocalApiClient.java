package com.zyd.blog.file;

import com.zyd.blog.file.entity.VirtualFile;
import com.zyd.blog.file.exception.LocalApiException;
import com.zyd.blog.file.util.BlogFileUtil;
import com.zyd.blog.file.util.StreamUtil;
import cn.hutool.core.io.FileUtil;
import org.springframework.util.DigestUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * 本地API客户端
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2019/2/11 15:06
 * @since 1.8
 */
public class LocalApiClient extends BaseApiClient {

    /**
     * 默认前缀
     */
    private static final String DEFAULT_PREFIX = "oneblog/";
    /**
     * URL
     */
    private String url;
    /**
     * 根目录
     */
    private String rootPath;
    /**
     * 路径前缀
     */
    private String pathPrefix;

    //设置文件存储类型为“Nginx文件服务器”
    public LocalApiClient() {
        super("Nginx文件服务器");
    }

    public LocalApiClient init(String url, String rootPath, String uploadType) {
        this.url = url;
        this.rootPath = rootPath;
        StringBuilder builder = new StringBuilder();

        if(StringUtils.isEmpty(uploadType)){
            builder.append(DEFAULT_PREFIX);
        }
        else{
            builder.append(uploadType);
            if(!uploadType.endsWith("/")) builder.append("/");
        }
        this.pathPrefix = builder.toString();
        return this;
    }

    /**
     * 上传图片
     *
     * @param is 要上传的输入流
     * @param imageUrl 图片url
     * @return
     */
    @Override
    public VirtualFile uploadImg(InputStream is, String imageUrl) {
        //检查客户端配置
        this.check();

        //获得文件key
        String key = BlogFileUtil.generateTempFileName(imageUrl);
        //设置文件名
        this.createNewFileName(key, this.pathPrefix);
        Date startTime = new Date();

        //创建文件完整路径
        String realFilePath = this.rootPath + this.newFileName;
        //检查路径
        BlogFileUtil.checkFilePath(realFilePath);
        //JDK1.7后的try with resources写法，用于对资源申请，确保异常时资源实时关闭
        //创建上传文件输入流，文件哈希输入流和文件输出流资源
        try (InputStream uploadIs = StreamUtil.clone(is);
             InputStream fileHashIs = StreamUtil.clone(is);
             FileOutputStream fos = new FileOutputStream(realFilePath)) {
            //从文件输出流中复制文件到上传文件输入流中
            FileCopyUtils.copy(uploadIs, fos);
            //设置文件属性并返回上传文件对象
            return new VirtualFile()
                    .setOriginalFileName(FileUtil.getName(key))
                    .setSuffix(this.suffix)
                    .setUploadStartTime(startTime)
                    .setUploadEndTime(new Date())
                    .setFilePath(this.newFileName)
                    .setFileHash(DigestUtils.md5DigestAsHex(fileHashIs))
                    .setFullFilePath(this.url + this.newFileName);
        } catch (Exception e) {
            throw new LocalApiException("[" + this.storageType + "]文件上传失败：" + e.getMessage() + imageUrl);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除图片
     *
     * @param key 要删除的图片key
     * @return 操作结果
     */
    @Override
    public boolean removeFile(String key) {
        //检查客户端配置
        this.check();
        //文件key为空时抛出异常
        if (StringUtils.isEmpty(key)) {
            throw new LocalApiException("[" + this.storageType + "]删除文件失败：文件key为空");
        }
        //根据文件key创建文件对象
        File file = new File(this.rootPath + key);
        //文件不存在时抛出异常
        if (!file.exists()) {
            throw new LocalApiException("[" + this.storageType + "]删除文件失败：文件不存在[" + this.rootPath + key + "]");
        }
        try {
            //删除文件并返回删除结果
            return file.delete();
        } catch (Exception e) {
            throw new LocalApiException("[" + this.storageType + "]删除文件失败：" + e.getMessage());
        }
    }

    /**
     *  检查本地API客户端配置
     */
    @Override
    public void check() {
        //url或者根路径为空时抛出异常
        if (StringUtils.isEmpty(url) || StringUtils.isEmpty(rootPath)) {
            throw new LocalApiException("[" + this.storageType + "]尚未配置Nginx文件服务器，文件上传功能暂时不可用！");
        }
    }
}
