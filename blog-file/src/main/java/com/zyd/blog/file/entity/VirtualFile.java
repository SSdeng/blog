package com.zyd.blog.file.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 上传文件实体类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @date 2018/12/7 14:49
 * @since 1.8
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class VirtualFile {
    /**
     * 文件大小
     */
    public Long size;
    /**
     * 文件后缀（Suffix）
     */
    public String suffix;
    /**
     * 图片文件的宽
     */
    public Integer width;
    /**
     * 图片文件的高
     */
    public Integer height;
    /**
     * 文件hash
     */
    private String fileHash;
    /**
     * 文件路径 （不带域名）
     */
    private String filePath;
    /**
     * 文件全路径 （带域名）
     */
    private String fullFilePath;
    /**
     * 原始文件名
     */
    private String originalFileName;
    /**
     * 文件上传开始的时间
     */
    private Date uploadStartTime;
    /**
     * 文件上传结束的时间
     */
    private Date uploadEndTime;

    /**
     * 设置文件hash,返回设置后的文件实体
     *
     * @param fileHash
     * @return
     */
    public VirtualFile setFileHash(String fileHash) {
        this.fileHash = fileHash;
        return this;
    }

    /**
     * 设置文件路径（不带域名）,返回设置后的文件实体
     *
     * @param filePath
     * @return
     */
    public VirtualFile setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    /**
     * 设置文件全路径（带域名）,返回设置后的文件实体
     *
     * @param fullFilePath
     * @return
     */
    public VirtualFile setFullFilePath(String fullFilePath) {
        this.fullFilePath = fullFilePath;
        return this;
    }

    /**
     * 设置原始文件名,返回设置后的文件实体
     *
     * @param originalFileName
     * @return
     */
    public VirtualFile setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
        return this;
    }

    /**
     * 设置文件上传开始时间
     *
     * @param uploadStartTime
     * @return
     */
    public VirtualFile setUploadStartTime(Date uploadStartTime) {
        this.uploadStartTime = uploadStartTime;
        return this;
    }

    /**
     * 设置文件上传结束时间
     *
     * @param uploadEndTime
     * @return
     */
    public VirtualFile setUploadEndTime(Date uploadEndTime) {
        this.uploadEndTime = uploadEndTime;
        return this;
    }

    /**
     * 计算文件上传所花费时间
     *
     * @return 文件上传所用时间
     */
    public long getUseTime() {
        Date uploadEndTime = this.getUploadEndTime();
        Date uploadStartTime = this.getUploadStartTime();
        if (null == uploadStartTime || null == uploadEndTime) {
            return -1;
        }
        return uploadEndTime.getTime() - uploadStartTime.getTime();
    }

    /**
     * 设置文件大小
     *
     * @param size
     * @return
     */
    public VirtualFile setSize(long size) {
        this.size = size;
        return this;
    }

    /**
     * 设置文件后缀
     *
     * @param suffix
     * @return
     */
    public VirtualFile setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    /**
     * 设置图片文件的宽
     *
     * @param width
     * @return
     */
    public VirtualFile setWidth(int width) {
        this.width = width;
        return this;
    }

    /**
     * 设置图片文件的高
     *
     * @param height
     * @return
     */
    public VirtualFile setHeight(int height) {
        this.height = height;
        return this;
    }
}
