package com.zyd.blog.file.alioss.api;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import com.zyd.blog.file.alioss.entity.BucketEntity;
import com.zyd.blog.file.alioss.entity.CorsRoleEntity;
import com.zyd.blog.file.alioss.entity.ObjectsRequestEntity;
import com.zyd.blog.file.alioss.entity.RefererEntity;
import com.zyd.blog.file.exception.OssApiException;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 阿里云OSSAPI
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2019/2/12 15:49
 * @since 1.8
 */
public class OssApi {

    private OSSClient client;

    public OssApi(OSSClient client) {
        this.client = client;
    }

    /**
     * 创建OSS客户端
     *
     * @param endpoint          阿里云地区节点域名
     * @param accessKeyId       阿里云账户AccessKeyID
     * @param accessKeySecret   阿里云账户AccessKey密码
     */
    public OssApi(String endpoint, String accessKeyId, String accessKeySecret) {
        this.client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 授权访问文件的URL
     *
     * @param fileName       待授权的文件名
     * @param bucketName     存储空间
     * @param expirationTime 授权失效时间，单位秒
     */
    public String authFile(String fileName, String bucketName, long expirationTime) {
        try {
            //存储空间不存在时抛出异常
            if (!this.client.doesBucketExist(bucketName)) {
                throw new OssApiException("[阿里云OSS] 无法授权访问文件的URL！Bucket不存在：" + bucketName);
            }
            //文件不存在时抛出异常
            if (!this.client.doesObjectExist(bucketName, fileName)) {
                throw new OssApiException("[阿里云OSS] 文件授权失败！文件不存在：" + bucketName + "/" + fileName);
            }
            // 设置URL过期时间为1小时
            Date expiration = new Date(new Date().getTime() + expirationTime * 1000);
            // 生成URL
            return this.client.generatePresignedUrl(bucketName, fileName, expiration).toString();
        } finally {
            //关闭客户端
            this.shutdown();
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName   OSS中保存的文件名
     * @param bucketName 存储空间
     */
    public boolean isExistFile(String fileName, String bucketName) {
        try {
            //存储空间不存在时抛出异常
            if (!this.client.doesBucketExist(bucketName)) {
                throw new OssApiException("[阿里云OSS] Bucket不存在：" + bucketName);
            }
            //返回文件是否存在的结果
            return this.client.doesObjectExist(bucketName, fileName);
        } finally {
            //关闭客户端
            this.shutdown();
        }
    }

    /**
     * 获取指定bucket下的文件的访问权限
     *
     * @param fileName   OSS中保存的文件名
     * @param bucketName 存储空间
     * @return
     */
    public ObjectPermission getFileAcl(String fileName, String bucketName) {
        try {
            //存储空间不存在时抛出异常
            if (!this.client.doesBucketExist(bucketName)) {
                throw new OssApiException("[阿里云OSS] 无法获取文件的访问权限！Bucket不存在：" + bucketName);
            }
            //文件不存在时抛出异常
            if (!this.client.doesObjectExist(bucketName, fileName)) {
                throw new OssApiException("[阿里云OSS] 无法获取文件的访问权限！文件不存在：" + bucketName + "/" + fileName);
            }
            return this.client.getObjectAcl(bucketName, fileName).getPermission();
        } finally {
            this.shutdown();
        }
    }

    /**
     * 获取文件列表
     *
     * @param bucketName 存储空间名
     * @param request    查询条件
     * @return 文件列表
     */
    public List<OSSObjectSummary> listFile(String bucketName, ObjectsRequestEntity request) {
        try {
            //存储空间不存在时抛出异常
            if (!this.client.doesBucketExist(bucketName)) {
                throw new OssApiException("[阿里云OSS] 无法获取文件列表！Bucket不存在：" + bucketName);
            }
            //创建新的存储空间请求对象
            ListObjectsRequest listRequest = new ListObjectsRequest(bucketName);
            //从请求中载入请求信息
            if (null != request) {
                listRequest.withDelimiter(request.getDelimiter())
                        .withEncodingType(request.getEncodingType())
                        .withMarker(request.getMarker())
                        .withMaxKeys(request.getMaxKeys())
                        .withPrefix(request.getPrefix());
            }
            // 列举Object
            ObjectListing objectListing = this.client.listObjects(listRequest);
            //返回Object列表
            return objectListing.getObjectSummaries();
        } finally {
            //关闭客户端
            this.shutdown();
        }
    }

    /**
     * 修改指定bucket下的文件的访问权限
     *
     * @param fileName   OSS中保存的文件名
     * @param bucketName 保存文件的目标bucket
     * @param acl        权限
     */
    public void setFileAcl(String fileName, String bucketName, CannedAccessControlList acl) {
        try {
            //存储空间不存在时抛出异常
            boolean exists = this.client.doesBucketExist(bucketName);
            if (!exists) {
                throw new OssApiException("[阿里云OSS] 无法修改文件的访问权限！Bucket不存在：" + bucketName);
            }
            //文件不存在时抛出异常
            if (!this.client.doesObjectExist(bucketName, fileName)) {
                throw new OssApiException("[阿里云OSS] 无法修改文件的访问权限！文件不存在：" + bucketName + "/" + fileName);
            }
            //修改指定存储空间指定文件的访问权限
            this.client.setObjectAcl(bucketName, fileName, acl);
        } finally {
            //关闭客户端
            this.shutdown();
        }
    }

    /**
     * 删除文件
     *
     * @param bucketName 保存文件的目标bucket
     * @param fileName   OSS中保存的文件名
     */
    public void deleteFile(String fileName, String bucketName) {
        try {
            boolean exists = this.client.doesBucketExist(bucketName);
            //存储空间不存在时抛出异常
            if (!exists) {
                throw new OssApiException("[阿里云OSS] 文件删除失败！Bucket不存在：" + bucketName);
            }
            //文件不存在时抛出异常
            if (!this.client.doesObjectExist(bucketName, fileName)) {
                throw new OssApiException("[阿里云OSS] 文件删除失败！文件不存在：" + bucketName + "/" + fileName);
            }
            //删除指定存储空间指定文件
            this.client.deleteObject(bucketName, fileName);
        } finally {
            //关闭客户端
            this.shutdown();
        }
    }

    /**
     * 创建存储空间
     *
     * @param bucketName 存储空间名称
     */
    public void createBucket(String bucketName) {
        try {
            boolean exists = this.client.doesBucketExist(bucketName);
            //存储空间不存在时抛出异常
            if (exists) {
                throw new OssApiException("[阿里云OSS] Bucket创建失败！Bucket名称[" + bucketName + "]已被使用！");
            }
            // -- 创建指定类型的Bucket，请使用Java SDK 2.6.0及以上版本。
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
            // 设置bucket权限为公共读，默认是私有读写
            createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
            // 设置bucket存储类型为低频访问类型，默认是标准类型
            createBucketRequest.setStorageClass(StorageClass.IA);
            //创建存储空间
            this.client.createBucket(createBucketRequest);
        } finally {
            //关闭客户端
            this.shutdown();
        }
    }

    /**
     * 设置bucket的访问权限
     *
     * @param bucket bucket
     */
    public void setBucketAcl(BucketEntity bucket) {
        try {
            //存储空间不存在时抛出异常
            if (!this.client.doesBucketExist(bucket.getBucketName())) {
                throw new OssApiException("[阿里云OSS] 无法修改Bucket的访问权限！Bucket不存在：" + bucket.getBucketName());
            }
            //设置指定存储空间的权限
            this.client.setBucketAcl(bucket.getBucketName(), bucket.getAcl());
        } finally {
            //关闭客户端
            this.shutdown();
        }
    }

    /**
     * 跨域访问管理：跨域资源共享(CORS)允许web端的应用程序访问不属于本域的资源
     *
     * @param corsRole 跨域规则
     */
    public void setBucketCors(CorsRoleEntity corsRole) {
        try {
            //存储空间不存在时抛出异常
            if (!this.client.doesBucketExist(corsRole.getBucketName())) {
                throw new OssApiException("[阿里云OSS] 无法修改Bucket的跨域设置！Bucket不存在：" + corsRole.getBucketName());
            }
            //从跨域资源共享对象中获取请求
            SetBucketCORSRequest request = new SetBucketCORSRequest(corsRole.getBucketName());

            //CORS规则的容器,每个bucket最多允许10条规则
            ArrayList<SetBucketCORSRequest.CORSRule> putCorsRules = new ArrayList<>();
            SetBucketCORSRequest.CORSRule corRule = new SetBucketCORSRequest.CORSRule();

            //指定允许的跨域请求方法(GET/PUT/DELETE/POST/HEAD)。
            corRule.setAllowedMethods(corsRole.getAllowedMethod());
            //指定允许跨域请求的来源。
            corRule.setAllowedOrigins(corsRole.getAllowedOrigin());
            //是否允许预取指令（OPTIONS）中Access-Control-Request-Headers头中指定的Header。
            corRule.setAllowedHeaders(corsRole.getAllowedHeader());
            //指定允许用户从应用程序中访问的响应头。
            corRule.setExposeHeaders(corsRole.getExposedHeader());
            //指定浏览器对特定资源的预取(OPTIONS)请求返回结果的缓存时间,单位为秒。
            corRule.setMaxAgeSeconds(corsRole.getMaxAgeSeconds());
            //最多允许10条规则
            putCorsRules.add(corRule);

            //已存在的规则将被覆盖。
            request.setCorsRules(putCorsRules);
            //设置跨域资源共享信息
            this.client.setBucketCORS(request);
        } finally {
            //关闭客户端
            this.shutdown();
        }
    }

    /**
     * 创建模拟文件夹本质上来说是创建了一个名字以“/”结尾的文件；<br>
     * 对于这个文件照样可以上传下载,只是控制台会对以“/”结尾的文件以文件夹的方式展示；<br>
     * 多级目录创建最后一级即可，比如dir1/dir2/dir3/，创建dir1/dir2/dir3/即可，dir1/、dir1/dir2/不需要创建；
     *
     * @param folder     目录名
     * @param bucketName 存储空间
     */
    public void createFolder(String folder, String bucketName) throws IOException {
        try (InputStream inputStream = new ByteArrayInputStream(new byte[0])){

            //存储空间名为空时抛出异常
            if (null == bucketName) {
                throw new OssApiException("[阿里云OSS] 尚未指定Bucket！");
            }

            //存储空间不存在时抛出异常
            if (!this.client.doesBucketExist(bucketName)) {
                throw new OssApiException("[阿里云OSS] 无法创建目录！Bucket不存在：" + bucketName);
            }
            //创建目录名
            folder = folder.endsWith("/") ? folder : folder + "/";
            //上传模拟文件
            this.client.putObject(bucketName, folder, inputStream);
        } finally {
            //关闭客户端
            this.shutdown();
        }
    }

    /**
     * 批量设置Referer白名单
     *
     * @param refererEntity refererEntity
     */
    public void addReferers(RefererEntity refererEntity) {
        try {
            //存储空间不存在时抛出异常
            if (!this.client.doesBucketExist(refererEntity.getBucketName())) {
                throw new OssApiException("[阿里云OSS] 无法设置Referer白名单！Bucket不存在：" + refererEntity.getBucketName());
            }
            //refer列表为空时结束
            if (CollectionUtils.isEmpty(refererEntity.getRefererList())) {
                return;
            }
            //获取referer列表
            BucketReferer br = new BucketReferer(true, refererEntity.getRefererList());
            //设置白名单
            this.client.setBucketReferer(refererEntity.getBucketName(), br);
        } finally {
            //关闭客户端
            this.shutdown();
        }
    }

    /**
     * 清空Referer白名单
     *
     * @param bucketName 存储空间名
     */
    public void removeReferers(String bucketName) {
        try {
            //存储空间不存在时抛出异常
            if (!this.client.doesBucketExist(bucketName)) {
                throw new OssApiException("[阿里云OSS] 无法清空Referer白名单！Bucket不存在：" + bucketName);
            }
            // 默认允许referer字段为空，且referer白名单为空。
            BucketReferer br = new BucketReferer();
            //设置空的白名单
            client.setBucketReferer(bucketName, br);
        } finally {
            //关闭客户端
            this.shutdown();
        }
    }

    /**
     * 获取Referer白名单
     *
     * @param bucketName 存储空间名
     */
    public List<String> getReferers(String bucketName) {
        try {
            //存储空间不存在时抛出异常
            if (!this.client.doesBucketExist(bucketName)) {
                throw new OssApiException("[阿里云OSS] 无法获取Referer白名单！Bucket不存在：" + bucketName);
            }
            //获取指定存储空间的referer白名单
            BucketReferer br = this.client.getBucketReferer(bucketName);
            //返回白名单
            return br.getRefererList();
        } finally {
            //关闭客户端
            this.shutdown();
        }
    }

    /**
     * 通过文件对象上传文件
     *
     * @param localFile 待上传的文件
     * @param fileName  文件名:最终保存到云端的文件名
     * @param bucket    需要上传到的目标bucket
     */
    public String uploadFile(File localFile, String fileName, String bucket) {
        try (
                InputStream inputStream = new FileInputStream(localFile)
        ){
            //上传本地文件并返回结果
            return this.uploadFile(inputStream, fileName, bucket);
        } catch (Exception e) {
            throw new OssApiException("[阿里云OSS] 文件上传失败！" + localFile, e);
        } finally {
            //关闭客户端
            this.shutdown();
        }
    }

    /**
     * 通过文件输入流上传文件
     *
     * @param inputStream 待上传的文件流
     * @param fileName    文件名:最终保存到云端的文件名
     * @param bucketName  需要上传到的目标bucket
     */
    public String uploadFile(InputStream inputStream, String fileName, String bucketName) {
        try {
            //存储空间不存在时抛出异常
            if (!this.client.doesBucketExist(bucketName)) {
                throw new OssApiException("[阿里云OSS] 无法上传文件！Bucket不存在：" + bucketName);
            }
            //上传文件并获取结果
            PutObjectResult result = this.client.putObject(bucketName, fileName, inputStream);
            //返回文件内容标识，文件内容不变etag值不变
            return result.getETag();
        } finally {
            //关闭客户端
            this.shutdown();
        }
    }

    /**
     * 关闭OSS Client
     */
    private void shutdown() {
        this.client.shutdown();
    }
}
