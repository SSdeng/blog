package com.zyd.blog.business.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.zyd.blog.business.annotation.RedisCache;
import com.zyd.blog.business.consts.DateConst;
import com.zyd.blog.business.enums.ConfigKeyEnum;
import com.zyd.blog.business.enums.FileUploadType;
import com.zyd.blog.business.service.SysConfigService;
import com.zyd.blog.file.FileUploader;
import com.zyd.blog.file.entity.VirtualFile;
import com.zyd.blog.framework.property.AppProperties;
import com.zyd.blog.persistence.beans.SysConfig;
import com.zyd.blog.persistence.mapper.SysConfigMapper;
import com.zyd.blog.plugin.file.GlobalFileUploader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置服务实现类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Slf4j
@Service
public class SysConfigServiceImpl implements SysConfigService {

    @Autowired
    private SysConfigMapper sysConfigMapper;
    @Autowired
    private AppProperties properties;

    /**
     * 获取配置信息
     *
     * @return Map
     */
    @Override
    @RedisCache(enable = false)
    public Map<String, Object> getConfigs() {
        // 调用SysConfigMapper返回所有配置实体
        List<SysConfig> list = sysConfigMapper.selectAll();
        // 如果结果集为空，则返回null
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        String updateTimeKey = ConfigKeyEnum.UPDATE_TIME.getKey();
        // 创建返回的Map<String, Object>
        Map<String, Object> res = new HashMap<>();
        // 利用开源项目huTool提供的日期工具完成日期格式转换，设置系统最后一次更新时间为"2019-01-01 00:00:00"
        res.put(updateTimeKey, DateUtil.parse("2019-01-01 00:00:00", DateConst.YYYY_MM_DD_HH_MM_SS_EN));
        // 遍历配置实体
        list.forEach(sysConfig -> {
            // 将配置实体的ConfigKey属性、ConfigValue属性分别作为map的Key、Value放入Map中
            res.put(String.valueOf(sysConfig.getConfigKey()), sysConfig.getConfigValue());
            // 如果该配置的更新时间比现存的最后更新时间晚，则替换最后更新时间
            if (sysConfig.getUpdateTime().after(((Date) res.get(updateTimeKey)))) {
                res.put(updateTimeKey, sysConfig.getUpdateTime());
            }
        });
        // 取出存储方式
        String storageType = (String) res.get(ConfigKeyEnum.STORAGE_TYPE.getKey());
        String fsp = "fileStoragePath";
        // 如果存储方式为本地，则在map中加入value为本地文件存储路径，key为"fileStoragePath"
        if ("local".equalsIgnoreCase(storageType)) {
            res.put(fsp, res.get(ConfigKeyEnum.LOCAL_FILE_URL.getKey()));
        } else if ("qiniu".equalsIgnoreCase(storageType)) {
            // 如果存储方式为七牛，则在map中加入value为七牛云cdn域名，key为"fileStoragePath"
            res.put(fsp, res.get(ConfigKeyEnum.QINIU_BASE_PATH.getKey()));
        } else if ("aliyun".equalsIgnoreCase(storageType)) {
            // 如果存储方式为阿里云，则在map中加入value为阿里云Bucket域名，key为"fileStoragePath"
            res.put(fsp, res.get(ConfigKeyEnum.ALIYUN_FILE_URL.getKey()));
        }
        return res;
    }

    /**
     * 添加/修改文件的配置项
     * @param key  key
     * @param file 微信收款码
     */
    @Override
    @RedisCache(flush = true, enable = false)
    public void saveFile(String key, MultipartFile file) {
        if (key == null) {
            // 传来的key为空，什么也不干
            return;
        }
        if (file != null) { // 传来的微信收款码文件非空，则上传它
            // 使用File模块中的FileUploader进行文件上传
            FileUploader uploader = new GlobalFileUploader();
            VirtualFile virtualFile = uploader.upload(file, FileUploadType.QRCODE.getPath(), true);
            // 调用SysConfigMapper，将该该文件的路径写入配置信息并更新或保存数据库的信息
            this.saveConfig(key, virtualFile.getFullFilePath());
        }
    }

    /**
     * 添加/修改单个
     * @param key   key
     * @param value value
     */
    @Override
    @RedisCache(flush = true, enable = false)
    public void saveConfig(String key, String value) {
        if (!StringUtils.isEmpty(key)) {
            // 当传入的key非空时
            SysConfig config = null;
            if (null == (config = this.getByKey(key))) {
                // 数据库不存在该配置实体，新建一个配置实体，设置相应属性，调用SysConfigMapper写入数据库
                config = new SysConfig();
                config.setConfigKey(key);
                config.setConfigValue(value);
                config.setCreateTime(new Date());
                config.setUpdateTime(new Date());
                this.sysConfigMapper.insert(config);
            } else {
                // 数据库存在该配置，更新config属性，调用SysConfigMapper进行更新
                config.setConfigKey(key);
                config.setConfigValue(value);
                config.setUpdateTime(new Date());
                this.sysConfigMapper.updateByPrimaryKeySelective(config);
            }
        }
    }

    /**
     * 获取单个配置
     * @param key key
     * @return 配置
     */
    @Override
    @RedisCache(enable = false)
    public SysConfig getByKey(String key) {
        if (StringUtils.isEmpty(key)) {
            // key为空，返回null
            return null;
        }
        // 非空，利用SysConfigMapper的selectOne方法获取单个配置
        SysConfig sysConfig = new SysConfig();
        sysConfig.setConfigKey(key);
        return this.sysConfigMapper.selectOne(sysConfig);
    }

    /**
     * 添加/修改系统配置
     *
     * @param configs 所有的配置项
     */
    @Override
    @RedisCache(flush = true, enable = false)
    public void saveConfig(Map<String, String> configs) {
        // map非空，则遍历传入的配置项，并调用saveConfig保存配置项
        if (!CollectionUtils.isEmpty(configs)) {
            configs.forEach(this::saveConfig);
        }
    }

    /**
     * 获取网站详情
     */
    @Override
    public Map<String, Object> getSiteInfo() {
        // 调用SysConfigMapper操作数据库获取网站详情Map
        Map<String, Object> siteInfo = sysConfigMapper.getSiteInfo();
        // 如果结果集非空
        if (!CollectionUtils.isEmpty(siteInfo)) {
            Date installdate = null;
            // 获取网站安装时间配置信息
            SysConfig config = this.getByKey(ConfigKeyEnum.INSTALLDATE.getKey());
            // 如果配置信息为null或value为null，则使用默认建站日期2019-01-01
            if (null == config || StringUtils.isEmpty(config.getConfigValue())) {
                // 默认建站日期为2019-01-01
                installdate = Date.from(LocalDate.of(2019, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            } else {
                // 否则取出数据库存的值，并格式化
                installdate = DateUtil.parse(config.getConfigValue(), DatePattern.NORM_DATETIME_PATTERN);
            }
            long between = 1;
            // 如果安装时间不晚于当前时间，则记录安装时间与当前时间相差多少天
            if (!installdate.after(new Date())) {
                between = DateUtil.between(installdate, new Date(), DateUnit.DAY);
            }
            // map中加入key为"installdate"，value为相差时间的entry
            siteInfo.put("installdate", between < 1 ? 1 : between);
        }
        return siteInfo;
    }

    /**
     * 返回 Spider
     * @return spider信息
     */
    @Override
    @RedisCache(enable = false)
    public String getSpiderConfig() {
        // 获取key为spider的配置信息
        SysConfig config = this.getByKey("spiderConfig");
        // 得到的config为空或值为空，则返回”{}“，否则返回数据库中的值
        if (config == null) {
            return "{}";
        }
        return StringUtils.isEmpty(config.getConfigValue()) ? "{}" : config.getConfigValue();
    }

    /**
     * 获取随机的用户头像
     * @return 用户头像列表
     */
    @Override
    public List<String> getRandomUserAvatar() {
        // 获取key为defaultUserAvatar的配置信息
        SysConfig config = this.getByKey("defaultUserAvatar");
        // 得到的config为空，则返回null
        if (config == null) {
            return null;
        }
        try {
            // 否则利用alibaba的fastJson，将值转换为JSON数组格式返回
            return JSON.parseArray(config.getConfigValue(), String.class);
        } catch (Exception e) {
            log.error("配置项无效！defaultUserAvatar = [" + config.getConfigValue() + "]");
        }
        return null;
    }
}
