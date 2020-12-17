package com.zyd.blog.util;

import com.zyd.blog.framework.exception.ZhydException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/18 11:48
 * @since 1.0
 */
public class BeanConvertUtil {
	private BeanConvertUtil() {
		throw new IllegalStateException();
	}

    /**
     * sourceList --> targetList 转换
     *
     * @param sourceArray
     * @param target
     * @param <T>
     * @return
     */
    public static <T> List<T> doConvert(List<?> sourceArray, Class<T> target) {
        if (CollectionUtils.isEmpty(sourceArray) || null == target) {
            return Collections.emptyList();
        }
        return sourceArray.stream().map(bo -> doConvert(bo, target)).collect(Collectors.toList());
    }

    /**
     * source --> target 的转换
     *
     * @param source 被转换的对象
     * @param target 转换成的对象
     * @param <T>
     * @return
     */
    public static <T> T doConvert(Object source, Class<T> target) {
        if (null == source || null == target) {
            return null;
        }
        try {
            //复制资源属性
            T t = target.newInstance();
            BeanUtils.copyProperties(source, t);
            return t;
        } catch (InstantiationException e) {
            throw new ZhydException(target + " - 可能为一个抽象类、接口、数组类、基本类型或者该类缺少无参构造方法！", e);
        } catch (IllegalAccessException e) {
            throw new ZhydException(target + " - 该类或其构造方法是不可访问的，或该类缺少无参构造方法！", e);
        } catch (FatalBeanException e) {
            throw new ZhydException(target + " - 序列化失败！", e);
        }
    }

    /**
     * source --> target 的转换，只复制不为空null的属性
     *
     * @param source 被转换的对象
     * @param target 转换成的对象
     * @param <T>
     * @return
     */
    public static <T> T doConvert(Object source, Object target, Class<T> clazz) {
        if (null == source || null == target) {
            return null;
        }
        //复制资源到目的地属性
        CustomBeanUtils.copyProperties(source, target);
        if (clazz.equals(target.getClass())) {
            return (T) target;
        }
        throw new ClassCastException(target.getClass() + " cannot be cast to " + clazz);
    }

    /**
     * 只复制不为空的属性
     */
    private static class CustomBeanUtils extends BeanUtils {
        public static void copyProperties(Object source, Object target) throws BeansException {
            copyProperties(source, target, null, (String[]) null);
        }

        private static void copyProperties(Object source, Object target, @Nullable Class<?> editable, @Nullable String... ignoreProperties) throws BeansException {
            Assert.notNull(source, "Source must not be null");//断言源资源不为空
            Assert.notNull(target, "Target must not be null");//断言目的资源不为空
            Class<?> actualEditable = target.getClass();
            if (editable != null) {
                if (!editable.isInstance(target)) {
                    throw new IllegalArgumentException("Target class [" + target.getClass().getName() + "] not assignable to Editable class [" + editable.getName() + "]");
                }

                actualEditable = editable;
            }
            //定义属性描述
            PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
            List<String> ignoreList = ignoreProperties != null ? Arrays.asList(ignoreProperties) : null;
            PropertyDescriptor[] var7 = targetPds;
            int var8 = targetPds.length;
            /**
             * 进行不同类型属性之间的转换
             */
            for (int var9 = 0; var9 < var8; ++var9) {
                PropertyDescriptor targetPd = var7[var9];
                Method writeMethod = targetPd.getWriteMethod();
                if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
                    PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                    if (sourcePd != null) {
                        Method readMethod = sourcePd.getReadMethod();
                        if (readMethod != null && ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                            try {
                                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                    readMethod.setAccessible(true);
                                }

                                Object value = readMethod.invoke(source);
                                // 只copy不为null的值
                                if (null != value) {
                                    if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                        writeMethod.setAccessible(true);
                                    }

                                    writeMethod.invoke(target, value);
                                }
                            } catch (Exception var15) {
                                throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", var15);
                            }
                        }
                    }
                }
            }

        }
    }

}
