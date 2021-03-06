package com.zyd.blog.plugin.file;

import com.zyd.blog.business.entity.File;
import com.zyd.blog.business.entity.User;
import com.zyd.blog.business.enums.ConfigKeyEnum;
import com.zyd.blog.business.service.BizFileService;
import com.zyd.blog.business.service.SysConfigService;
import com.zyd.blog.file.AliyunOssApiClient;
import com.zyd.blog.file.ApiClient;
import com.zyd.blog.file.LocalApiClient;
import com.zyd.blog.file.QiniuApiClient;
import com.zyd.blog.file.entity.VirtualFile;
import com.zyd.blog.file.exception.GlobalFileException;
import com.zyd.blog.framework.exception.ZhydException;
import com.zyd.blog.framework.holder.SpringContextHolder;
import com.zyd.blog.persistence.beans.BizFile;
import com.zyd.blog.util.BeanConvertUtil;
import com.zyd.blog.util.SessionUtil;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 基础级文件上传
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2019/2/11 14:26
 * @since 1.8
 */
public class BaseFileUploader {
    /**
     * 获得api客户端
     * @param uploadType
     * @return
     */
    ApiClient getApiClient(String uploadType) {
        SysConfigService configService = SpringContextHolder.getBean(SysConfigService.class);
        Map<String, Object> config = configService.getConfigs();
        String storageType = null;//存储类型
        storageType = (String) config.get(ConfigKeyEnum.STORAGE_TYPE.getKey());
        if (StringUtils.isEmpty(storageType)) {
            throw new ZhydException("[文件服务]当前系统暂未配置文件服务相关的内容！");
        }

        ApiClient res = null;
        switch (storageType) {
            case "local":
                String localFileUrl = (String) config.get(ConfigKeyEnum.LOCAL_FILE_URL.getKey());
                String localFilePath = (String) config.get(ConfigKeyEnum.LOCAL_FILE_PATH.getKey());
                res = new LocalApiClient().init(localFileUrl, localFilePath, uploadType);
                break;
            case "qiniu":
                String accessKey = (String) config.get(ConfigKeyEnum.QINIU_ACCESS_KEY.getKey());
                String secretKey = (String) config.get(ConfigKeyEnum.QINIU_SECRET_KEY.getKey());
                String qiniuBucketName = (String) config.get(ConfigKeyEnum.QINIU_BUCKET_NAME.getKey());
                String baseUrl = (String) config.get(ConfigKeyEnum.QINIU_BASE_PATH.getKey());
                res = new QiniuApiClient().init(accessKey, secretKey, qiniuBucketName, baseUrl, uploadType);
                break;
            case "aliyun":
                String endpoint = (String) config.get(ConfigKeyEnum.ALIYUN_ENDPOINT.getKey());
                String accessKeyId = (String) config.get(ConfigKeyEnum.ALIYUN_ACCESS_KEY.getKey());
                String accessKeySecret = (String) config.get(ConfigKeyEnum.ALIYUN_ACCESS_KEY_SECRET.getKey());
                String url = (String) config.get(ConfigKeyEnum.ALIYUN_FILE_URL.getKey());
                String aliYunBucketName = (String) config.get(ConfigKeyEnum.ALIYUN_BUCKET_NAME.getKey());
                res = new AliyunOssApiClient().init(endpoint, accessKeyId, accessKeySecret, url, aliYunBucketName, uploadType);
                break;
            case "youpaiyun":
                break;
            default:
                break;
        }
        if (null == res) {
            throw new GlobalFileException("[文件服务]当前系统暂未配置文件服务相关的内容！");
        }
        return res;
    }

    /**
     * 保存文件
     * @param virtualFile
     * @param save
     * @param uploadType
     * @return
     */
    VirtualFile saveFile(VirtualFile virtualFile, boolean save, String uploadType) {
        if (save) {
            BizFileService fileService = SpringContextHolder.getBean(BizFileService.class);
            try {
                SysConfigService configService = SpringContextHolder.getBean(SysConfigService.class);
                Map<String, Object> config = configService.getConfigs();
                String storageType = (String) config.get(ConfigKeyEnum.STORAGE_TYPE.getKey());

                BizFile fileInfo = BeanConvertUtil.doConvert(virtualFile, BizFile.class);
                User sessionUser = SessionUtil.getUser();
                fileInfo.setUserId(null == sessionUser ? null : sessionUser.getId());
                fileInfo.setUploadType(uploadType);
                fileInfo.setStorageType(storageType);
                fileService.insert(new File(fileInfo));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return virtualFile;
    }

    /**
     * 删除文件
     * @param apiClient
     * @param filePath
     * @param uploadType
     * @return
     */
    boolean removeFile(ApiClient apiClient, String filePath, String uploadType) {
        BizFileService fileService = SpringContextHolder.getBean(BizFileService.class);
        File file = fileService.selectFileByPathAndUploadType(filePath, uploadType);
        String fileKey = filePath;
        if (null != file) {
            fileKey = file.getFilePath();
        }
        return apiClient.removeFile(fileKey);
    }

}
