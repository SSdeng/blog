package com.zyd.blog.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zyd.blog.persistence.beans.BizFile;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author innodev java team
 * @version 1.0
 * @date 2018/12/14 09:23
 * @since 1.8
 *
 * 所有的实体类都包装了beans中对应的持久化中的数据实体，
 * beans中的数据实体大多都继承了AbstractDO，抽象数据对象中都有自己的id，创建时间，更新时间
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class File {//业务实体图片，包装了持久化中的数据实体BizFile
    private BizFile file;

    /**
     * 带参数的构造函数
     *
     * @param file
     */
    public File(BizFile file) {
        this.file = file;
    }

    /**
     * 构造函数
     */
    public File() {
    }

    /**
     * @return 返回图片
     */
    @JsonIgnore//在json序列化时将pojo中的一些属性忽略掉，标记在属性或者方法上，返回的json数据即不包含该属性。
    public BizFile getFile() {
        return file;
    }

    /**
     * 设置图片
     *
     * @param file
     * @return 返回图片
     */
    public File setFile(BizFile file) {
        this.file = file;
        return this;
    }

    /**
     * @return 返回文件这条记录对应的ID
     */
    public Long getId() {
        return this.file.getId();
    }

    /**
     * 设置图片的Id
     *
     * @param id
     * @return 图片
     */
    public File setId(Long id) {
        this.file.setId(id);
        return this;
    }

    /**
     * @return 返回使用这个头像的用户ID
     */
    public Long getUserId() {
        return this.file.getUserId();
    }

    /**
     * 设置使用图片的用户ID
     *
     * @param userId
     * @return 图片
     */
    public File setUserId(Long userId) {
        this.file.setUserId(userId);
        return this;
    }

    /**
     * @return 返回原本的文件名
     */
    public String getOriginalFileName() {
        return this.file.getOriginalFileName();
    }

    /**
     * 设置文件名
     *
     * @param originalFileName
     * @return 图片（文件）
     */
    public File setOriginalFileName(String originalFileName) {
        this.file.setOriginalFileName(originalFileName);
        return this;
    }

    /**
     * @return 返回图片的路径
     */
    public String getFilePath() {
        return this.file.getFilePath();
    }

    /**
     * 设置图片的路径
     *
     * @param filePath
     * @return 图片
     */
    public File setFilePath(String filePath) {
        this.file.setFilePath(filePath);
        return this;
    }

    /**
     * @return 返回图片的完整路径（带域名）
     */
    public String getFullFilePath() {
        return this.file.getFullFilePath();
    }

    /**
     * 设置图片的完整路径
     *
     * @param fullFilePath
     * @return 图片
     */
    public File setFullFilePath(String fullFilePath) {
        this.file.setFullFilePath(fullFilePath);
        return this;
    }

    /**
     * @return 返回图片的Hash值，计算图片是否传输成功？还是查找。。。
     */
    public String getFileHash() {
        return this.file.getFileHash();
    }

    /**
     * 设置图片的Hash
     *
     * @param fileHash
     * @return 图片
     */
    public File setFileHash(String fileHash) {
        this.file.setFileHash(fileHash);
        return this;
    }

    /**
     * @return 返回图片的上传格式
     */
    public String getUploadType() {
        return this.file.getUploadType();
    }

    /**
     * 设置图片的上传格式
     *
     * @param uploadType
     * @return 图片
     */
    public File setUploadType(String uploadType) {
        this.file.setUploadType(uploadType);
        return this;
    }

    /**
     * @return 返回图片的上传时间
     */
    public Date getUploadStartTime() {
        return this.file.getUploadStartTime();
    }

    /**
     * 设置图片的上传时间
     *
     * @param uploadStartTime
     * @return 图片
     */
    public File setUploadStartTime(Date uploadStartTime) {
        this.file.setUploadStartTime(uploadStartTime);
        return this;
    }

    /**
     * @return 返回上传的结束时间
     */
    public Date getUploadEndTime() {
        return this.file.getUploadEndTime();
    }

    /**
     * 设置图片的上传结束时间
     *
     * @param uploadEndTime
     * @return
     */
    public File setUploadEndTime(Date uploadEndTime) {
        this.file.setUploadEndTime(uploadEndTime);
        return this;
    }

    /**
     * @return 返回这条记录的创建时间，来源是继承了AbstractDO中的方法
     */
    public Date getCreateTime() {
        return this.file.getCreateTime();
    }

    /**
     * 设置这条记录的创建时间
     *
     * @param createTime
     * @return 图片
     */
    public File setCreateTime(Date createTime) {
        this.file.setCreateTime(createTime);
        return this;
    }

    /**
     * @return 返回这条记录的更新时间
     */
    public Date getUpdateTime() {
        return this.file.getUpdateTime();
    }

    /**
     * 设置这条记录的更新时间
     *
     * @param updateTime
     * @return 图片
     */
    public File setUpdateTime(Date updateTime) {
        this.file.setUpdateTime(updateTime);
        return this;
    }

    /**
     * @return 返回图片的大小
     */
    public Long getSize() {
        return this.file.getSize();
    }

    /**
     * 设置图片大小
     *
     * @param size
     * @return 图片
     */
    public File setSize(Long size) {
        this.file.setSize(size);
        return this;
    }

    /**
     * @return 返回图片格式的后缀
     */
    public String getSuffix() {
        return this.file.getSuffix();
    }

    /**
     * 设置图片的后缀
     *
     * @param suffix
     * @return 图片
     */
    public File setSuffix(String suffix) {
        this.file.setSuffix(suffix);
        return this;
    }

    /**
     * @return 返回图片的宽度
     */
    public Integer getWidth() {
        return this.file.getWidth();
    }

    /**
     * 设置图片的宽度
     *
     * @param width
     * @return 图片
     */
    public File setWidth(Integer width) {
        this.file.setWidth(width);
        return this;
    }

    /**
     * @return 返回图片的高度
     */
    public Integer getHeight() {
        return this.file.getHeight();
    }

    /**
     * 设置图片的高度
     *
     * @param height
     * @return 图片
     */
    public File setHeight(Integer height) {
        this.file.setHeight(height);
        return this;
    }

    /**
     * @return 返回图片的存储格式
     */
    public String getStorageType() {
        return this.file.getStorageType();
    }

    /**
     * 设置图片的存储格式
     *
     * @param storageTypeEnum
     * @return 图片
     */
    public File setStorageType(String storageTypeEnum) {
        this.file.setStorageType(storageTypeEnum);
        return this;
    }

}