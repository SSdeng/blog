package com.zyd.blog.framework.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import java.time.Duration;


/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    /**
     * 缓存数据时Key的生成器，可以依据业务和技术场景自行定制
     *
     * @return 重载generate方法后的KeyGenerator
     */
    @Bean
    @Override
    @Deprecated
    public KeyGenerator keyGenerator() {

        //实现KeyGenerator中的generate方法，自定义方法返回key
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            //类名+方法名
            sb.append(target.getClass().getName());
            sb.append(".").append(method.getName());

            //再加入多个params中不确定个数的object
            for (Object obj : params) {
                sb.append(obj);
            }

            //将StringBuilder转化为String并返回
            return sb.toString();
        };
    }


    /**
     * 根据factory中的数据，设置并生成RedisCacheManager
     * @param factory 线程安全的redis连接工厂，用于存储redis的连接信息
     * @return Spring的中央缓存管理器SPI
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {

        //redis缓存管理器第一次写入时创建缓存
        //RedisCacheWriter 封装了对于redis的简单操作
        //RedisCacheWriter.nonLockingRedisCacheWriter 返回一个未上锁（缓存锁）的RedisCacheWriter
        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(factory))

                //cacheDefaults 定义一个默认的RedisCacheConfiguration应用于动态创建的RedisCache,就是使用RedisConfiguration的设置来配置一个RedisCacheWriter
                //RedisCacheConfiguration 封装了RedisCache的设置信息
                //defaultCacheConfig 设置RedisCacheConfiguration中的参数为默认值
                //entryTtl 设置RedisCacheConfiguration的Ttl，即缓存的有效时间
                //Duration 对象表示两个Instant间的一段时间
                //设置Duration时间为30天
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(30)))
                //启用RedisCache使缓存放入/收回操作与正在进行的Spring管理事务同步。
                .transactionAware()
                //根据前面的设置信息建造RedisCacheManager
                .build();
    }

    /**
     * 生成RedisTemplate，并根据factory对RedisTemplate进行设置
     * @param factory 线程安全的redis连接工厂，用于存储redis的连接信息
     * @return 对redis连接池高度封装的RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {

        //生成RedisTemplate
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        //设置连接的工厂信息
        template.setConnectionFactory(factory);

        //ObjectMapper 提供将Java对象转化为Json的方法
        ObjectMapper om = new ObjectMapper();

        //设置ObjectMapper 这将使所有成员字段无需进一步注释即可序列化，而不仅仅是公共字段
        //PropertyAccessor.ALL 设置所有访问对象都受影响
        //接收所有成员的修改请求
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        //设置使用默认类型
        //对于所有不是final的对象都使用默认类型
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        //使用GenericJackson2JsonRedisSerializer对进入redis的数据进行序列化
        //设置自定义的ObjectMapper控制Json的序列化
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(om);

        //设置RedisTemplate的Value序列化方式
        template.setValueSerializer(jackson2JsonRedisSerializer);

        //设置RedisTemplate的Key的序列化方式
        template.setKeySerializer(jackson2JsonRedisSerializer);

        //对RedisTemplate未设置的部分使用默认设置
        template.afterPropertiesSet();

        //返回RedisTemplate
        return template;
    }
}
