package com.zyd.blog.business.enums;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @website https://www.zhyd.me
 * @version 1.0
 * @date 2018/4/16 16:26
 * @since 1.0
 */

/**
 * 枚举响应状态
 * @param SUCCESS:操作成功
 * @param ERROR:服务器未知错误
 * @param INVALLID_PARAMS：操作失败，无效的参数，请检查参数格式、类型是否正确
 * @param UPLOAD_FILE_ERROR：文件上传失败
 */
public enum ResponseStatus {

    SUCCESS(200, "操作成功！"),
    ERROR(500, "服务器未知错误！"),
    INVALID_PARAMS(500, "操作失败，无效的参数，请检查参数格式、类型是否正确！"),
    UPLOAD_FILE_ERROR(500, "文件上传失败！"),
    ;
    /**
     * 定义响应的code、消息
     */
    private Integer code;
    private String message;//消息

    /**
     * 初始化响应状态
     * @param code
     * @param message
     */
    ResponseStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据消息遍历所有的响应状态获取所对应的状态
     * @param message
     * @return
     */
    public static ResponseStatus getResponseStatus(String message) {
        for (ResponseStatus ut : ResponseStatus.values()) {
            if (ut.getMessage() == message) {
                return ut;
            }
        }
        return null;
    }

    /**
     * 获取响应code
     * @return
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 获取响应消息
     * @return
     */
    public String getMessage() {
        return message;
    }
}
