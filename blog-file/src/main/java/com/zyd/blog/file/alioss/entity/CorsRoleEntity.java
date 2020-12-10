package com.zyd.blog.file.alioss.entity;

import lombok.Data;

import java.util.ArrayList;

/**
 * 跨域资源共享实体
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2017/7/12 10:29
 * @since 1.8
 */
@Data
public class CorsRoleEntity extends AbstractEntity {

    private ArrayList<String> allowedOrigin; // 指定允许跨域请求的来源
    private ArrayList<String> allowedMethod; // 指定允许的跨域请求方法(GET/PUT/DELETE/POST/HEAD)
    private ArrayList<String> allowedHeader; // 控制在OPTIONS预取指令中Access-Control-Request-Headers头中指定的header是否允许
    private ArrayList<String> exposedHeader; // 指定允许用户从应用程序中访问的响应头
    private int maxAgeSeconds; // 指定浏览器对特定资源的预取(OPTIONS)请求返回结果的缓存时间,单位为秒

    public CorsRoleEntity(String bucket) {
        super(bucket);
        this.maxAgeSeconds = 300;
    }

    /**
     * 设置允许跨域请求来源
     *
     * @param allowedOrigin
     * @return
     */
    public CorsRoleEntity setAllowedOrigin(ArrayList<String> allowedOrigin) {
        this.allowedOrigin = allowedOrigin;
        return this;
    }

    /**
     * 设置允许跨域请求方法(GET/PUT/DELETE/POST/HEAD)
     *
     * @param allowedMethod
     * @return
     */
    public CorsRoleEntity setAllowedMethod(ArrayList<String> allowedMethod) {
        this.allowedMethod = allowedMethod;
        return this;
    }

    /**
     * 设置在OPTIONS预取指令中Access-Control-Request-Headers头中指定的header是否允许
     *
     * @param allowedHeader
     * @return
     */
    public CorsRoleEntity setAllowedHeader(ArrayList<String> allowedHeader) {
        this.allowedHeader = allowedHeader;
        return this;
    }

    /**
     * 设置允许用户从应用程序中访问的响应头
     *
     * @param exposedHeader
     * @return
     */
    public CorsRoleEntity setExposedHeader(ArrayList<String> exposedHeader) {
        this.exposedHeader = exposedHeader;
        return this;
    }

    /**
     * 设置浏览器对特定资源的预取(OPTIONS)请求返回结果的缓存时间
     *
     * @param maxAgeSeconds
     */
    public void setMaxAgeSeconds(int maxAgeSeconds) {
        this.maxAgeSeconds = maxAgeSeconds;
    }

}
