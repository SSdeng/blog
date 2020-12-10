package com.zyd.blog.file;

import cn.hutool.core.date.DateUtil;
import com.zyd.blog.file.entity.VirtualFile;
import com.zyd.blog.file.exception.GlobalFileException;
import com.zyd.blog.file.exception.OssApiException;
import com.zyd.blog.file.exception.QiniuApiException;
import com.zyd.blog.file.util.FileUtil;
import com.zyd.blog.file.util.ImageUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;

/**
 * 基本API客户端
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2019/2/12 16:19
 * @since 1.8
 */
public abstract class BaseApiClient implements ApiClient {

    /**
     * 文件存储类型
     */
    protected String storageType;
    /**
     * 文件名
     */
    protected String newFileName;
    /**
     * 文件后缀
     */
    protected String suffix;

    public BaseApiClient(String storageType) {
        this.storageType = storageType;
    }

    /**
     * 上传文件
     *
     * @param file MultipartFile对象
     * @return 文件实体
     */
    @Override
    public VirtualFile uploadImg(MultipartFile file) {
        this.check();
        if (file == null) {
            throw new OssApiException("[" + this.storageType + "]文件上传失败：文件不可为空");
        }
        try {
            VirtualFile res = this.uploadImg(file.getInputStream(), file.getOriginalFilename());
            VirtualFile imageInfo = ImageUtil.getInfo(file);//获取图片信息
            return res.setSize(imageInfo.getSize())
                    .setOriginalFileName(file.getOriginalFilename())
                    .setWidth(imageInfo.getWidth())
                    .setHeight(imageInfo.getHeight());
        } catch (IOException e) {
            throw new GlobalFileException("[" + this.storageType + "]文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 上传文件
     *
     * @param file File对象
     * @return 文件实体对象
     */
    @Override
    public VirtualFile uploadImg(File file) {
        this.check();
        if (file == null) {
            throw new QiniuApiException("[" + this.storageType + "]文件上传失败：文件不可为空");
        }
        try {
            InputStream is = new BufferedInputStream(new FileInputStream(file));
            VirtualFile res = this.uploadImg(is, "temp" + FileUtil.getSuffix(file));
            VirtualFile imageInfo = ImageUtil.getInfo(file);//获取图片信息
            return res.setSize(imageInfo.getSize())
                    .setOriginalFileName(file.getName())
                    .setWidth(imageInfo.getWidth())
                    .setHeight(imageInfo.getHeight());
        } catch (FileNotFoundException e) {
            throw new GlobalFileException("[" + this.storageType + "]文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 设置文件名
     *
     * @param key 文件key
     * @param pathPrefix 路径前缀
     */
    void createNewFileName(String key, String pathPrefix) {
        this.suffix = FileUtil.getSuffix(key);
        if (!FileUtil.isPicture(this.suffix)) {
            throw new GlobalFileException("[" + this.storageType + "] 非法的图片文件[" + key + "]！目前只支持以下图片格式：[jpg, jpeg, png, gif, bmp]");
        }
        String fileName = DateUtil.format(new Date(), "yyyyMMddHHmmssSSS");//以上传时间命名文件
        this.newFileName = pathPrefix + (fileName + this.suffix);
    }

    /**
     * 检查API客户端配置
     */
    protected abstract void check();
}
