package com.zyd.blog.business.service;


import com.github.pagehelper.PageInfo;
import com.zyd.blog.business.entity.Log;
import com.zyd.blog.business.enums.PlatformEnum;
import com.zyd.blog.business.vo.LogConditionVO;
import com.zyd.blog.framework.object.AbstractService;

/**
 * 日志服务接口
 * @author yadong.zhang email:yadong.zhang0415(a)gmail.com
 * @version 1.0
 * @date 2018/01/09 17:40
 * @since 1.0
 */
public interface SysLogService extends AbstractService<Log, Integer> {

    /**
     * 分页查询
     * 使用PageHelper开源项目
     * @param vo 日志条件VO,包含分页参数
     * @return PageInfo
     */
    PageInfo<Log> findPageBreakByCondition(LogConditionVO vo);

    /**
     * 异步保存日志
     * @param platform 平台
     * @param bussinessName 业务操作
     */
    void asyncSaveSystemLog(PlatformEnum platform, String bussinessName);
}
