package com.zyd.blog.file;

import com.alibaba.fastjson.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Region;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringUtils;
import com.zyd.blog.file.entity.VirtualFile;
import com.zyd.blog.file.exception.QiniuApiException;
import com.zyd.blog.file.util.BlogFileUtil;

import java.io.InputStream;
import java.util.Date;

/**
 * Qiniu云操作文件的api：v1
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @date 2018/4/16 16:26
 * @since 1.0
 */
public class QiniuApiClient extends BaseApiClient {

    /**
     * 默认前缀
     */
    private static final String DEFAULT_PREFIX = "oneblog/";
    /**
     * 账户AccessKey
     */
    private String accessKey;
    /**
     * 账户密码
     */
    private String secretKey;
    /**
     * 存储空间
     */
    private String bucket;
    /**
     * 存储路径
     */
    private String path;
    /**
     * 路径前缀
     */
    private String pathPrefix;

    public QiniuApiClient() {
        super("七牛云");
    }

    /**
     * 初始化方法
     *
     * @param accessKey     账户AccessKey
     * @param secretKey     账户密码
     * @param bucketName    存储空间
     * @param baseUrl       存储路径
     * @param uploadType    上传类型
     * @return
     */
    public QiniuApiClient init(String accessKey, String secretKey, String bucketName, String baseUrl, String uploadType) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucket = bucketName;
        this.path = baseUrl;
        StringBuilder builder = new StringBuilder();

        if(org.springframework.util.StringUtils.isEmpty(uploadType)){
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
     * @param is       图片流
     * @param imageUrl 图片路径
     * @return 上传后的路径
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
        //Zone.zone0:华东
        //Zone.zone1:华北
        //Zone.zone2:华南
        //Zone.zoneNa0:北美
        Configuration cfg = new Configuration(Region.autoRegion());//存储对象地区自动设置
        UploadManager uploadManager = new UploadManager(cfg);//创建上传对象
        try {
            //根据通行Key信息创建客户端
            Auth auth = Auth.create(this.accessKey, this.secretKey);
            //获得上传Token
            String upToken = auth.uploadToken(this.bucket);

            //获得上传结果响应
            Response response = uploadManager.put(is, this.newFileName, upToken, null, null);

            //解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);

            //设置文件属性并返回上传文件对象
            return new VirtualFile()
                    .setOriginalFileName(key)
                    .setSuffix(this.suffix)
                    .setUploadStartTime(startTime)
                    .setUploadEndTime(new Date())
                    .setFilePath(putRet.key)
                    .setFileHash(putRet.hash)
                    .setFullFilePath(this.path + putRet.key);
        } catch (QiniuException ex) {
            throw new QiniuApiException("[" + this.storageType + "]文件上传失败：" + ex.error());
        }
    }

    /**
     * 删除七牛空间图片方法
     *
     * @param key 七牛空间中文件名称
     */
    @Override
    public boolean removeFile(String key) {
        //检查客户端配置
        this.check();

        //文件key值为空时抛出异常
        if (StringUtils.isNullOrEmpty(key)) {
            throw new QiniuApiException("[" + this.storageType + "]删除文件失败：文件key为空");
        }
        //根据通行key信息创建客户端
        Auth auth = Auth.create(this.accessKey, this.secretKey);
        //获取默认配置
        Configuration config = new Configuration(Region.autoRegion());
        //创建存储对象
        BucketManager bucketManager = new BucketManager(auth, config);
        try {
            //获得删除结果响应
            Response re = bucketManager.delete(this.bucket, key);
            //返回响应
            return re.isOK();
        } catch (QiniuException e) {
            Response r = e.response;
            throw new QiniuApiException("[" + this.storageType + "]删除文件发生异常：" + r.toString());
        }
    }

    /**
     * 检查七牛云配置
     */
    @Override
    public void check() {
        //通行key，密码，存储空间任一为空时抛出异常
        if (StringUtils.isNullOrEmpty(this.accessKey) || StringUtils.isNullOrEmpty(this.secretKey) || StringUtils.isNullOrEmpty(this.bucket)) {
            throw new QiniuApiException("[" + this.storageType + "]尚未配置七牛云，文件上传功能暂时不可用！");
        }
    }

    /**
     * 返回存储路径
     * @return
     */
    public String getPath() {
        return this.path;
    }
}
