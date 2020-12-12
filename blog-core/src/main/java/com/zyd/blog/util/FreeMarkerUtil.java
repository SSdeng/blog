package com.zyd.blog.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Freemarker模板操作工具类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/18 11:48
 * @since 1.0
 */
@Slf4j
public class FreeMarkerUtil {

    private static final String LT = "<";//定义LT为<
    private static final String LT_CHAR = "&lt;";//定义LT_CHAR为&lt
    private static final String GT = ">";//定义GT为>
    private static final String GT_CHAR = "&gt;";//定义GT_CHAR为&gt
    private static final String AMP = "&";//定义AMP为&
    private static final String AMP_CHAR = "&amp;";//定义AMP_CHAR为&amp
    private static final String APOS = "'";//定义APOS为‘
    private static final String APOS_CHAR = "&apos;";//定义APOS_CHAR为&apos
    private static final String QUOT = "&quot;";//定义QUOT为&quot
    private static final String QUOT_CHAR = "\"";//定义QUOT_CHAR为\

    /**
     * Template to String Method Note
     *
     * @param templateContent template content
     * @param map             tempate data map
     * @return
     */
    public static String template2String(String templateContent, Map<String, Object> map,
                                         boolean isNeedFilter) {
        if (StringUtils.isEmpty(templateContent)) {
            return null;
        }
        if (map == null) {
            map = new HashMap<>();
        }
        Map<String, Object> newMap = new HashMap<>(1);
         //设置键
        Set<String> keySet = map.keySet();
        //如果键大小大于0
        if (keySet.size() > 0) {
            for (String key : keySet) {
                //获取键
                Object o = map.get(key);
                if (o != null) {
                    //进行比较
                    if (o instanceof String) {
                        String value = o.toString();
                        value = value.trim();
                        if (isNeedFilter) {
                            value = filterXmlString(value);
                        }
                        newMap.put(key, value);
                    } else {
                        newMap.put(key, o);
                    }
                }
            }
        }
        Template t = null;
        try {
            // 设定freemarker对数值的格式化
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
            cfg.setNumberFormat("#");
            t = new Template("", new StringReader(templateContent), cfg);
            StringWriter writer = new StringWriter();
            t.process(newMap, writer);
            //返回字符写入
            return writer.toString();
        } catch (IOException e) {
            log.error("TemplateUtil -> template2String IOException.", e);
        } catch (TemplateException e) {
            log.error("TemplateUtil -> template2String TemplateException.", e);
        } finally {
            newMap.clear();
            newMap = null;
        }
        return null;
    }

    private static String filterXmlString(String str) {
        if (null == str) {
            return null;
        }
        str = str.replaceAll(LT, LT_CHAR);//用LT_CHAR替换LT
        str = str.replaceAll(GT, GT_CHAR);//用GT_CHAR替换GT
        str = str.replaceAll(AMP, AMP_CHAR);//用AMP_CHAR替换AMP
        str = str.replaceAll(APOS, APOS_CHAR);//用APOS_CHAR替换APOS
        str = str.replaceAll(QUOT, QUOT_CHAR);//用QUOT_CHAR替换QUOT
        return str;
    }
}
