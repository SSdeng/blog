package com.zyd.blog.file;

import com.zyd.blog.file.entity.VirtualFile;
import com.zyd.blog.file.exception.LocalApiException;
import com.zyd.blog.file.util.FileUtil;
import com.zyd.blog.file.util.StreamUtil;
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

    public LocalApiClient() {
        super("Nginx文件服务器");
    }

    public LocalApiClient init(String url, String rootPath, String uploadType) {
        this.url = url;
        this.rootPath = rootPath;

        this.pathPrefix = StringUtils.isEmpty(uploadType) ? DEFAULT_PREFIX : uploadType.endsWith("/") ? uploadType : uploadType + "/";
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
        this.check();

        String key = FileUtil.generateTempFileName(imageUrl);
        this.createNewFileName(key, this.pathPrefix);
        Date startTime = new Date();

        String realFilePath = this.rootPath + this.newFileName;
        FileUtil.checkFilePath(realFilePath);
        try (InputStream uploadIs = StreamUtil.clone(is);
             InputStream fileHashIs = StreamUtil.clone(is);
             FileOutputStream fos = new FileOutputStream(realFilePath)) {//JDK1.7后的try with resources写法，用于对资源申请，确保异常时资源实时关闭
            FileCopyUtils.copy(uploadIs, fos);
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
        this.check();
        if (StringUtils.isEmpty(key)) {
            throw new LocalApiException("[" + this.storageType + "]删除文件失败：文件key为空");
        }
        File file = new File(this.rootPath + key);
        if (!file.exists()) {
            throw new LocalApiException("[" + this.storageType + "]删除文件失败：文件不存在[" + this.rootPath + key + "]");
        }
        try {
            return file.delete();
        } catch (Exception e) {
            throw new LocalApiException("[" + this.storageType + "]删除文件失败：" + e.getMessage());
        }
    }

    @Override
    public void check() {
        if (StringUtils.isEmpty(url) || StringUtils.isEmpty(rootPath)) {
            throw new LocalApiException("[" + this.storageType + "]尚未配置Nginx文件服务器，文件上传功能暂时不可用！");
        }
    }
}
