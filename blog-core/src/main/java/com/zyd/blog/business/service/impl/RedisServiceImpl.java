package com.zyd.blog.business.service.impl;

import com.alibaba.fastjson.JSON;
import com.zyd.blog.business.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis服务实现类
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Service
public class RedisServiceImpl implements RedisService {

    /** spring-data-redis中的核心操作类 */
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置值
     *
     * @param key 键值
     * @param value 实值
     * @param <T> 泛型
     */
    @Override
    public <T> void set(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置值
     *
     * @param key 键值
     * @param value 实值
     * @param expire 生命周期
     * @param timeUnit 时间单位
     * @param <T> 泛型
     */
    @Override
    public <T> void set(String key, T value, long expire, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, expire, timeUnit);
    }

    /**
     * 获取实值
     *
     * @param key 键值
     * @param <T> 泛型
     * @return 实值
     */
    @Override
    public <T> T get(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    /**
     * 设置生命周期
     * 时间单位默认为秒
     *
     * @param key 键值
     * @param expire 生命周期
     * @return 设置结果
     */
    @Override
    public boolean expire(String key, long expire) {
        return redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    /**
     * 删除
     *
     * @param key 键值
     */
    @Override
    public void del(String key) {
        redisTemplate.opsForValue().getOperations().delete(key);
    }

    /**
     * 批量删除
     *
     * @param keys 键值表
     */
    @Override
    public void delBatch(Set<String> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 批量删除
     * 删除键值前缀为keyPrefix的所有值
     *
     * @param keyPrefix 键值前缀
     */
    @Override
    public void delBatch(String keyPrefix) {
        // 以keyPrefix为前缀的键值集合
        Set<String> keys = this.keySet(keyPrefix + "*");
        // 集合不为空
        if (!CollectionUtils.isEmpty(keys)) {
            // 根据集合批量删除
            delBatch(keys);
        }
    }

    /**
     * 存储表
     *
     * @param key 键值
     * @param list 表
     * @param <T> 泛型
     */
    @Override
    public <T> void setList(String key, List<T> list) {
        String value = JSON.toJSONString(list);
        set(key, value);
    }

    /**
     * 存储表
     *
     * @param key 键值
     * @param list 表
     * @param expire 生命周期
     * @param timeUnit 时间单位
     * @param <T> 泛型
     */
    @Override
    public <T> void setList(String key, List<T> list, long expire, TimeUnit timeUnit) {
        // 将表转换为JSONString
        String value = JSON.toJSONString(list);
        // 存储
        set(key, value, expire, timeUnit);
    }

    /**
     * 取出表
     *
     * @param key 键值
     * @param clz 元素类型
     * @param <T> 泛型
     * @return 表
     */
    @Override
    public <T> List<T> getList(String key, Class<T> clz) {
        // 按键值取出List对应JSONString
        String json = get(key);
        // 实值不为空
        if (json != null) {
            // 将JSON还原为List clz为List存储的元素类型
            return JSON.parseArray(json, clz);
        }
        return null;
    }

    /**
     * 查询键值是否存在
     *
     * @param key 键值
     * @return 存在返回true 否则返回false
     */
    @Override
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 获取声明周期
     *
     * @param key 键值
     * @return 生命周期
     */
    @Override
    public long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 创建以keyPrefix为前缀的键值集合
     *
     * @param keyPrefix 前缀
     * @return 键值集合
     */
    @Override
    public Set<String> keySet(String keyPrefix) {
        return redisTemplate.keys(keyPrefix + "*");
    }
}
