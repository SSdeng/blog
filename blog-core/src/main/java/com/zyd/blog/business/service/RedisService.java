package com.zyd.blog.business.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @website https://www.zhyd.me
 * @version 1.0
 * @date 2018/4/16 16:26
 * @since 1.0
 */
public interface RedisService {
    /**
     * 设置值
     *
     * @param key 键值
     * @param value 实值
     * @param <T> 泛型
     */
    <T> void set(String key, T value);

    /**
     * 设置值
     *
     * @param key 键值
     * @param value 实值
     * @param expire 生命周期
     * @param timeUnit 时间单位
     * @param <T> 泛型
     */
    <T> void set(String key, T value, long expire, TimeUnit timeUnit);

    /**
     * 获取实值
     *
     * @param key 键值
     * @param <T> 泛型
     * @return 实值
     */
    <T> T get(String key);

    /**
     * 设置生命周期
     * 时间单位默认为秒
     *
     * @param key 键值
     * @param expire 生命周期
     * @return 设置结果
     */
    boolean expire(String key, long expire);

    /**
     * 删除
     *
     * @param key 键值
     */
    void del(String key);

    /**
     * 批量删除
     *
     * @param keys 键值表
     */
    void delBatch(Set<String> keys);

    /**
     * 批量删除
     * 删除键值前缀为keyPrefix的所有值
     *
     * @param keyPrefix 键值前缀
     */
    void delBatch(String keyPrefix);

    /**
     * 存储表
     *
     * @param key 键值
     * @param list 表
     * @param <T> 泛型
     */
    <T> void setList(String key, List<T> list);

    /**
     * 存储表
     *
     * @param key 键值
     * @param list 表
     * @param expire 生命周期
     * @param timeUnit 时间单位
     * @param <T> 泛型
     */
    <T> void setList(String key, List<T> list, long expire, TimeUnit timeUnit);

    /**
     * 取出表
     *
     * @param key 键值
     * @param clz 元素类型
     * @param <T> 泛型
     * @return 表
     */
    <T> List<T> getList(String key, Class<T> clz);

    /**
     * 查询键值是否存在
     *
     * @param key 键值
     * @return 存在返回true 否则返回false
     */
    boolean hasKey(String key);

    /**
     * 获取声明周期
     *
     * @param key 键值
     * @return 生命周期
     */
    long getExpire(String key);

    /**
     * 创建以keyPrefix为前缀的键值集合
     *
     * @param keyPrefix 前缀
     * @return 键值集合
     */
    Set<String> keySet(String keyPrefix);
}
