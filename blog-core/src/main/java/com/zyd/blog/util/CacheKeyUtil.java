package com.zyd.blog.util;

import com.alibaba.fastjson.JSON;

import org.springframework.validation.support.BindingAwareModelMap;

/**
 * 缓存key相关的工具类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/5/25 10:23
 * @since 1.0
 */
public class CacheKeyUtil {
	private CacheKeyUtil() {
		throw new IllegalStateException();
	}

    /**
     * 获取方法参数组成的key
     *
     * @param params
     *         参数数组
     */
    public static String getMethodParamsKey(Object... params) {
        if (null == params || params.length == 0) {
            return "";
        }
        //定义创建变量“（”
        StringBuilder key = new StringBuilder("(");
        for (Object obj : params) {
            if (obj.getClass().equals(BindingAwareModelMap.class)) {
                continue;
            }
            key.append(JSON.toJSONString(obj).replace("\"", "'"));//进行字符替换
        }
        key.append(")");
        return key.toString();
    }

}
