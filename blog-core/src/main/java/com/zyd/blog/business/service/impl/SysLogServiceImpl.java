package com.zyd.blog.business.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.zyd.blog.business.entity.Log;
import com.zyd.blog.business.entity.User;
import com.zyd.blog.business.enums.LogLevelEnum;
import com.zyd.blog.business.enums.LogTypeEnum;
import com.zyd.blog.business.enums.PlatformEnum;
import com.zyd.blog.business.service.SysLogService;
import com.zyd.blog.business.util.WebSpiderUtils;
import com.zyd.blog.business.vo.LogConditionVO;
import com.zyd.blog.persistence.beans.SysLog;
import com.zyd.blog.persistence.mapper.SysLogMapper;
import com.zyd.blog.util.RequestUtil;
import com.zyd.blog.util.SessionUtil;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 日志服务实现类
 * @author yadong.zhang email:yadong.zhang0415(a)gmail.com
 * @version 1.0
 * @date 2018/01/09 17:50
 * @since 1.0
 */
@Service
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    private SysLogMapper sysLogMapper;

    /**
     * 分页查询
     * 使用PageHelper开源项目
     * @param vo 日志条件VO,包含分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<Log> findPageBreakByCondition(LogConditionVO vo) {
        // 设置分页参数，开启分页
        PageMethod.startPage(vo.getPageNumber(), vo.getPageSize());
        // 紧跟着的第一个数据查询会被分页
        List<SysLog> list = sysLogMapper.findPageBreakByCondition(vo);
        // 结果列表为空，则返回null
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        // 非空，构建业务实体列表
        List<Log> boList = new ArrayList<>();
        // 遍历数据实体,并转换为业务实体加入列表
        for (SysLog sysLog : list) {
            boList.add(new Log(sysLog));
        }
        // 用PageInfo包装结果集
        return new PageInfo<>(boList);
    }

    /**
     * 异步保存日志
     * @param platform 平台
     * @param bussinessName 业务操作
     */
    @Async
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void asyncSaveSystemLog(PlatformEnum platform, String bussinessName) {
        // 获取请求头中的User-Agent
        String ua = RequestUtil.getUa();
        // 新建一个log业务实体
        Log sysLog = new Log();
        // 设置日志级别为info
        sysLog.setLogLevel(LogLevelEnum.INFO);
        // 设置日志type、ip、referer、requestUrl、ua、spiderType、params属性
        sysLog.setType(platform.equals(PlatformEnum.WEB) ? LogTypeEnum.VISIT : LogTypeEnum.SYSTEM);
        sysLog.setIp(RequestUtil.getIp());
        sysLog.setReferer(RequestUtil.getReferer());
        sysLog.setRequestUrl(RequestUtil.getRequestUrl());
        sysLog.setUa(ua);
        sysLog.setSpiderType(WebSpiderUtils.parseUa(ua));
        sysLog.setParams(JSON.toJSONString(RequestUtil.getParametersMap()));
        // 利用session工具类，取出session中的user实体
        User user = SessionUtil.getUser();
        if (user != null) { // user非空，代表用户登录了，设置日志的userid和内容
            sysLog.setUserId(user.getId());
            sysLog.setContent(String.format("用户: [%s] | 操作: %s", user.getUsername(), bussinessName));
        } else { // user为空，代表访客浏览，设置日志的内容
            sysLog.setContent(String.format("访客: [%s] | 操作: %s", sysLog.getIp(), bussinessName));
        }

        try {
            /*
             * 使用开源项目UserAgentUtils
             * 通过request请求中的user-agent获取浏览器类型和客户端操作系统
             * 并填充到log相应属性中
             */
            UserAgent agent = UserAgent.parseUserAgentString(ua);
            sysLog.setBrowser(agent.getBrowser().getName());
            sysLog.setOs(agent.getOperatingSystem().getName());
            // 调用insert方法保存log到数据库
            this.insert(sysLog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入日志实体
     * @param entity 日志实体
     * @return 插入的日志实体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Log insert(Log entity) {
        // 验证日志实体非空
        Assert.notNull(entity, "Log不可为空！");
        // 设置更新和创建信息
        entity.setUpdateTime(new Date());
        entity.setCreateTime(new Date());
        // 调用SysLogMapper操作数据库，null的属性不会保存，会使用数据库默认值
        sysLogMapper.insertSelective(entity.getSysLog());
        return entity;
    }

    /**
     * 删除指定主键日志实体
     * @param primaryKey 主键
     * @return 删除成功/失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByPrimaryKey(Integer primaryKey) {
        // 调用SysLogMapper操作数据库
        return sysLogMapper.deleteByPrimaryKey(primaryKey) > 0;
    }

    /**
     * 更新日志，null属性值不会被更新到数据库
     * @param entity 日志
     * @return 更新成功/失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSelective(Log entity) {
        // 验证传入的日志非空
        Assert.notNull(entity, "Log不可为空！");
        // 设置更新时间
        entity.setUpdateTime(new Date());
        // 调用SysNoticeMapper操作数据库，null属性值不会被更新到数据库
        return sysLogMapper.updateByPrimaryKeySelective(entity.getSysLog()) > 0;
    }

    /**
     * 获取指定主键日志
     * @param primaryKey 主键
     * @return 日志
     */
    @Override
    public Log getByPrimaryKey(Integer primaryKey) {
        // 验证主键非空
        Assert.notNull(primaryKey, "PrimaryKey不可为空！");
        // 调用SysNoticeMapper操作数据库
        SysLog entity = sysLogMapper.selectByPrimaryKey(primaryKey);
        // 判断得到的实体非空，并转换为业务实体
        return null == entity ? null : new Log(entity);
    }
}
