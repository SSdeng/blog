package com.zyd.blog.business.enums;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */

/**
 * 定义文件上传类型的路径包括
 * @param COMMON oneblog/
 * @param QRCODE oneblog/qrcode/
 * @param SIMPLE oneblog/article/
 * @param COVER_IMAGE oneblog/cover/
 */
public enum FileUploadType {
    COMMON("oneblog/"),
    QRCODE("oneblog/qrcode/"),
    SIMPLE("oneblog/article/"),
    COVER_IMAGE("oneblog/cover/");

    private String path;

    FileUploadType(String path) {
        this.path = path;
    }

    /**
     * 获取类型路径
     * @return path
     */
    public String getPath() {
        return path;
    }
}
