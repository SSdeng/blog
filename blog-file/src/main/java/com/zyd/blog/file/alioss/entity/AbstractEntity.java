package com.zyd.blog.file.alioss.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 抽象实体，其他实体的父类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2017/7/12 10:29
 * @since 1.8
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractEntity {
    private String bucketName;

    protected AbstractEntity() {
    }

    protected AbstractEntity(String bucketName) {
        this.bucketName = bucketName;
    }
}
