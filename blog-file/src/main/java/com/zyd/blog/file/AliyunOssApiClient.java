package com.zyd.blog.file;

import com.zyd.blog.file.alioss.api.OssApi;
import com.zyd.blog.file.entity.VirtualFile;
import com.zyd.blog.file.exception.OssApiException;
import com.zyd.blog.file.util.BlogFileUtil;
import com.zyd.blog.file.util.StreamUtil;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * 阿里云OSS API客户端
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2019/2/12 15:18
 * @since 1.8
 */
public class AliyunOssApiClient extends BaseApiClient {

    /**
     * 默认前缀
     */
    private static final String DEFAULT_PREFIX = "oneblog/";
    /**
     * 阿里云OSS API
     */
    private OssApi ossApi;
    /**
     * url
     */
    private String url;
    /**
     * 存储空间名
     */
    private String bucketName;
    /**
     * 路径前缀
     */
    private String pathPrefix;

    public AliyunOssApiClient() {
        super("阿里云OSS");
    }

    /**
     * 初始化方法
     *
     * @param endpoint          地区节点域名
     * @param accessKeyId       AccessKeyID
     * @param accessKeySecret   AccessKey密码
     * @param url               url
     * @param bucketName        存储空间名
     * @param uploadType        上传类型
     * @return
     */
    public AliyunOssApiClient init(String endpoint, String accessKeyId, String accessKeySecret, String url, String bucketName, String uploadType) {
        //根据节点域名，通行Key信息建立客户端对象并初始化参数
        ossApi = new OssApi(endpoint, accessKeyId, accessKeySecret);
        this.url = url;
        this.bucketName = bucketName;
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
     * @return 文件实体对象
     */
    @Override
    public VirtualFile uploadImg(InputStream is, String imageUrl) {
        //检查客户端配置
        this.check();

        //获取文件key
        String key = BlogFileUtil.generateTempFileName(imageUrl);
        //设置文件名
        this.createNewFileName(key, this.pathPrefix);
        Date startTime = new Date();
        //创建上传输入流和文件哈希输入流资源
        try (InputStream uploadIs = StreamUtil.clone(is);
             InputStream fileHashIs = StreamUtil.clone(is)) {
            //通过输入流上传文件
            ossApi.uploadFile(uploadIs, this.newFileName, bucketName);
            //设置文件属性并返回上传文件对象
            return new VirtualFile()
                    .setOriginalFileName(cn.hutool.core.io.FileUtil.getName(key))
                    .setSuffix(this.suffix)
                    .setUploadStartTime(startTime)
                    .setUploadEndTime(new Date())
                    .setFilePath(this.newFileName)
                    .setFileHash(DigestUtils.md5DigestAsHex(fileHashIs))
                    .setFullFilePath(this.url + this.newFileName);
        } catch (IOException e) {
            throw new OssApiException("[" + this.storageType + "]文件上传失败：" + e.getMessage());
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
     * 删除文件
     *
     * @param key 要删除的文件key
     * @return 操作结果
     */
    @Override
    public boolean removeFile(String key) {
        //检查客户端配置
        this.check();

        //文件key值为空时抛出异常
        if (StringUtils.isEmpty(key)) {
            throw new OssApiException("[" + this.storageType + "]删除文件失败：文件key为空");
        }

        try {
            //从存储空间中删除文件
            this.ossApi.deleteFile(key, bucketName);
            return true;
        } catch (Exception e) {
            throw new OssApiException(e.getMessage());
        }
    }

    /**
     * 检查阿里云OSS配置
     */
    @Override
    public void check() {
        //客户端为创建或初始化时抛出异常
        if (null == ossApi) {
            throw new OssApiException("[" + this.storageType + "]尚未配置阿里云OSS，文件上传功能暂时不可用！");
        }
    }
}
