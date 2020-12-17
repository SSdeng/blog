package com.zyd.blog.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.zyd.blog.business.dto.SysNoticeDTO;
import com.zyd.blog.business.entity.Notice;
import com.zyd.blog.business.enums.NoticeStatusEnum;
import com.zyd.blog.business.service.SysNoticeService;
import com.zyd.blog.business.vo.NoticeConditionVO;
import com.zyd.blog.persistence.beans.SysNotice;
import com.zyd.blog.persistence.mapper.SysNoticeMapper;
import com.zyd.blog.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 通知服务实现类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Service
public class SysNoticeServiceImpl implements SysNoticeService {

    @Autowired
    private SysNoticeMapper sysNoticeMapper;

    /**
     * 分页查询
     * 使用PageHelper开源项目实现
     * @param vo 通知条件VO，包含分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<Notice> findPageBreakByCondition(NoticeConditionVO vo) {
        // 设置分页参数，开启分页
        PageMethod.startPage(vo.getPageNumber(), vo.getPageSize());
        // 紧跟着的第一个数据查询会被分页
        List<SysNotice> list = sysNoticeMapper.findPageBreakByCondition(vo);
        // 结果列表为空，则返回null
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        // 遍历，数据实体转换为业务实体
        List<Notice> boList = new ArrayList<>();
        for (SysNotice sysNotice : list) {
            boList.add(new Notice(sysNotice));
        }
        // 用PageInfo对查询结果进行包装
        return new PageInfo<>(boList);
    }

    /**
     * 获取已发布的通知列表
     *
     * @return 已发布的通知详情列表
     */
    @Override
    public List<SysNoticeDTO> listRelease() {
        // 构建条件vo，设置vo的status属性为release
        NoticeConditionVO vo = new NoticeConditionVO();
        vo.setStatus(NoticeStatusEnum.RELEASE.toString());
        // 根据条件vo，调用SysNoticeMapper操作数据库获取符合的通知
        List<SysNotice> list = sysNoticeMapper.findPageBreakByCondition(vo);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        // 将已发布的通知列表转换为通知详情列表
        List<SysNoticeDTO> boList = new ArrayList<>();
        for (SysNotice sysNotice : list) {
            boList.add(BeanConvertUtil.doConvert(sysNotice, SysNoticeDTO.class));
        }
        return boList;
    }

    /**
     * 插入单个通知实体
     * @param entity 待插入通知
     * @return 插入的通知
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Notice insert(Notice entity) {
        // 验证传入的通知非空
        Assert.notNull(entity, "Notice不可为空！");
        // 设置更新和创建信息
        entity.setUpdateTime(new Date());
        entity.setCreateTime(new Date());
        // 调用SysNoticeMapper插入实体，null的属性不会保存，会使用数据库默认值
        sysNoticeMapper.insertSelective(entity.getSysNotice());
        return entity;
    }

    /**
     * 删除指定主键的通知
     * @param primaryKey 主键
     * @return 删除成功/失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByPrimaryKey(Long primaryKey) {
        // 调用SysNoticeMapper操作数据库
        return sysNoticeMapper.deleteByPrimaryKey(primaryKey) > 0;
    }

    /**
     * 更新通知，null值不会被更新到数据库
     * @param entity 新通知
     * @return 更新成功/失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSelective(Notice entity) {
        // 验证传入的通知非空
        Assert.notNull(entity, "Notice不可为空！");
        // 设置更新时间
        entity.setUpdateTime(new Date());
        // 调用SysNoticeMapper操作数据库，null值不会被更新到数据库
        return sysNoticeMapper.updateByPrimaryKeySelective(entity.getSysNotice()) > 0;
    }

    /**
     * 获取指定主键通知
     * @param primaryKey 主键
     * @return 通知
     */
    @Override
    public Notice getByPrimaryKey(Long primaryKey) {
        // 验证主键非空
        Assert.notNull(primaryKey, "PrimaryKey不可为空！");
        // 调用SysNoticeMapper操作数据库
        SysNotice entity = sysNoticeMapper.selectByPrimaryKey(primaryKey);
        // 判断得到的实体非空，并转换为业务实体
        return null == entity ? null : new Notice(entity);
    }
}
