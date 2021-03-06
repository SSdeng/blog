package com.zyd.blog.business.service;


import com.github.pagehelper.PageInfo;
import com.zyd.blog.business.dto.SysNoticeDTO;
import com.zyd.blog.business.entity.Notice;
import com.zyd.blog.business.vo.NoticeConditionVO;
import com.zyd.blog.framework.object.AbstractService;

import java.util.List;

/**
 * 通知服务接口类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
public interface SysNoticeService extends AbstractService<Notice, Long> {

    /**
     * 分页查询
     * 使用PageHelper开源项目实现
     * @param vo 通知条件VO，包含分页参数
     * @return PageInfo
     */
    PageInfo<Notice> findPageBreakByCondition(NoticeConditionVO vo);

    /**
     * 获取已发布的通知列表
     *
     * @return 已发布的通知列表
     */
    List<SysNoticeDTO> listRelease();
}
