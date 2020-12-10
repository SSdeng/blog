package com.zyd.blog.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyd.blog.business.annotation.RedisCache;
import com.zyd.blog.business.entity.Link;
import com.zyd.blog.business.enums.ConfigKeyEnum;
import com.zyd.blog.business.enums.LinkSourceEnum;
import com.zyd.blog.business.enums.TemplateKeyEnum;
import com.zyd.blog.business.service.MailService;
import com.zyd.blog.business.service.SysConfigService;
import com.zyd.blog.business.service.SysLinkService;
import com.zyd.blog.business.util.LinksUtil;
import com.zyd.blog.business.vo.LinkConditionVO;
import com.zyd.blog.framework.exception.ZhydLinkException;
import com.zyd.blog.persistence.beans.SysLink;
import com.zyd.blog.persistence.mapper.SysLinkMapper;
import com.zyd.blog.util.HtmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 友情链接服务实现类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Slf4j
@Service
public class SysLinkServiceImpl implements SysLinkService {

    @Autowired
    private SysLinkMapper sysLinkMapper;
    @Autowired
    private MailService mailService;
    @Autowired
    private SysConfigService configService;

    /**
     * 分页查询
     * 使用PageHelper开源项目
     * @param vo 友情链接条件VO,包含分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<Link> findPageBreakByCondition(LinkConditionVO vo) {
        // 设置分页参数，开启分页
        PageHelper.startPage(vo.getPageNumber(), vo.getPageSize());
        // 紧跟着的第一个数据查询会被分页
        List<SysLink> list = sysLinkMapper.findPageBreakByCondition(vo);
        // 结果列表为空，则返回null
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        // 非空，构建业务实体列表
        List<Link> boList = new ArrayList<>();
        // 遍历数据实体,并转换为业务实体加入列表
        for (SysLink sysLink : list) {
            boList.add(new Link(sysLink));
        }
        // 用PageInfo包装结果集
        PageInfo bean = new PageInfo<SysLink>(list);
        bean.setList(boList);
        return bean;
    }

    /**
     * 查询可在首页显示的友情链接列表
     *
     * @return 友情链接列表
     */
    @Override
    @RedisCache
    public List<Link> listOfIndex() {
        // 新建友情链接条件VO，设置status和homePageDisplay属性，以及一页大小设为100
        LinkConditionVO vo = new LinkConditionVO(1, 1);
        vo.setPageSize(100);
        // 调用分页查询函数，传入条件VO进行查询
        PageInfo<Link> pageInfo = this.findPageBreakByCondition(vo);
        // 需要判断是否为空
        return pageInfo == null ? null : pageInfo.getList();
    }

    /**
     * 查询可在内页显示的友情链接列表
     *
     * @return 友情链接列表
     */
    @Override
    @RedisCache
    public List<Link> listOfInside() {
        // 新建友情链接条件VO，设置status和homePageDisplay属性，以及一页大小设为100
        LinkConditionVO vo = new LinkConditionVO(1, 0);
        vo.setPageSize(100);
        // 调用分页查询函数，传入条件VO进行查询
        PageInfo<Link> pageInfo = this.findPageBreakByCondition(vo);
        // 需要判断是否为空
        return pageInfo == null ? null : pageInfo.getList();
    }

    /**
     * 查询已禁用的友情链接列表
     *
     * @return 友情链接列表
     */
    @Override
    @RedisCache
    public List<Link> listOfDisable() {
        // 新建友情链接条件VO，设置status和homePageDisplay属性，以及一页大小设为100
        LinkConditionVO vo = new LinkConditionVO(0, null);
        vo.setPageSize(100);
        // 调用分页查询函数，传入条件VO进行查询
        PageInfo<Link> pageInfo = this.findPageBreakByCondition(vo);
        // 需要判断是否为空
        return pageInfo == null ? null : pageInfo.getList();
    }

    /**
     * 分组获取所有连接
     * {indexList:首页显示,insideList:内页,disableList:禁用}
     *
     * @return 由分类和链接列表组成的Map
     */
    @Override
    @RedisCache
    public Map<String, List<Link>> listAllByGroup() {
        // 首页连接
        List<Link> listOfIndex = this.listOfIndex();
        // 内页连接
        List<Link> listOfInside = this.listOfInside();
        // 已禁用的连接
        List<Link> listOfDisable = this.listOfDisable();
        // 构建map，并将上面查询的结果加入其中
        Map<String, List<Link>> resultMap = new HashMap<>();
        resultMap.put("indexList", listOfIndex);
        resultMap.put("insideList", listOfInside);
        resultMap.put("disableList", listOfDisable);
        return resultMap;
    }

    /**
     * 自动添加友链
     *
     * @param link 友链
     * @return 添加成功/失败
     */
    @Override
    @RedisCache(flush = true)
    public boolean autoLink(Link link) throws ZhydLinkException {
        // 获取友链url，并根据url调用getOneByUrl（）查找数据库
        String url = link.getUrl();
        Link bo = getOneByUrl(url);
        // 若数据库中已存在该url，抛出异常，提示已添加
        if (bo != null) {
            throw new ZhydLinkException("本站已经添加过贵站的链接！");
        }
        // 获取配置信息实体
        Map config = configService.getConfigs();
        // 获取配置信息实体中的域名信息
        String domain = (String) config.get(ConfigKeyEnum.DOMAIN.getKey());
        // 如果该url未添加本站友情链接，抛出异常并提示
        if (!(LinksUtil.hasLinkByHtml(url, domain))
                && !LinksUtil.hasLinkByChinaz(url, domain)) {
            throw new ZhydLinkException("贵站暂未添加本站友情链接！请先添加本站友链后重新提交申请！");
        }
        // 设置友情链接来源属性为自动添加，添加状态为true
        link.setSource(LinkSourceEnum.AUTOMATIC);
        link.setStatus(true);
        // 如果友情链接邮箱非空，利用htmlUtil工具完成html格式向txt的转换，再设置友链邮箱
        if (!StringUtils.isEmpty(link.getEmail())) {
            link.setEmail(HtmlUtil.html2Text(link.getEmail()));
        }
        // 如果友情链接网站图标非空，利用htmlUtil工具完成html格式向txt的转换，再设置友链网站图标
        if (!StringUtils.isEmpty(link.getFavicon())) {
            link.setFavicon(HtmlUtil.html2Text(link.getFavicon()));
        }
        // 如果友情链接网站名非空，利用htmlUtil工具完成html格式向txt的转换，再设置友链网站名
        if (!StringUtils.isEmpty(link.getName())) {
            link.setName(HtmlUtil.html2Text(link.getName()));
        }
        // 如果友情链接url非空，利用htmlUtil工具完成html格式向txt的转换，再设置友链url
        if (!StringUtils.isEmpty(link.getUrl())) {
            link.setUrl(HtmlUtil.html2Text(link.getUrl()));
        }
        // 如果友情链接网站介绍非空，利用htmlUtil工具完成html格式向txt的转换，再设置友链网站介绍
        if (!StringUtils.isEmpty(link.getDescription())) {
            link.setDescription(HtmlUtil.html2Text(link.getDescription()));
        }
        // 调用insert（）方法插入友链
        this.insert(link);
        log.info("友联自动申请成功,开始发送邮件通知...");
        // 发送邮件通知
        mailService.send(link, TemplateKeyEnum.TM_LINKS);
        return true;
    }

    /**
     * 插入友链实体
     * @param entity 友链
     * @return 友链
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedisCache(flush = true)
    public Link insert(Link entity) {
        // 验证日志实体非空
        Assert.notNull(entity, "Link不可为空！");
        // 设置更新时间、创建时间、状态、主页展示属性
        entity.setUpdateTime(new Date());
        entity.setCreateTime(new Date());
        entity.setStatus(entity.isStatus());
        entity.setHomePageDisplay(entity.isHomePageDisplay());
        // 调用SysLogMapper操作数据库，null的属性不会保存，会使用数据库默认值
        sysLinkMapper.insertSelective(entity.getSysLink());
        return entity;
    }

    /**
     * 删除指定主键日志实体
     * @param primaryKey 主键
     * @return 删除成功/失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedisCache(flush = true)
    public boolean removeByPrimaryKey(Long primaryKey) {
        // 调用SysLogMapper操作数据库
        return sysLinkMapper.deleteByPrimaryKey(primaryKey) > 0;
    }

    /**
     * 更新友链，null属性值不会被更新到数据库
     * @param entity 友链
     * @return 更新成功/失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedisCache(flush = true)
    public boolean updateSelective(Link entity) {
        // 验证传入的友链非空
        Assert.notNull(entity, "Link不可为空！");
        // 设置更新时间、状态、主页展示属性
        entity.setUpdateTime(new Date());
        entity.setStatus(entity.isStatus());
        entity.setHomePageDisplay(entity.isHomePageDisplay());
        // 调用SysLogMapper操作数据库，null属性值不会被更新到数据库
        return sysLinkMapper.updateByPrimaryKeySelective(entity.getSysLink()) > 0;
    }

    /**
     * 获取指定主键友链
     * @param primaryKey 主键
     * @return 友链
     */
    @Override
    public Link getByPrimaryKey(Long primaryKey) {
        // 验证主键非空
        Assert.notNull(primaryKey, "PrimaryKey不可为空！");
        // 调用SysNoticeMapper操作数据库
        SysLink entity = sysLinkMapper.selectByPrimaryKey(primaryKey);
        // 判断得到的实体非空，并转换为业务实体
        return null == entity ? null : new Link(entity);
    }

    /**
     * 根据url获得一个友情链接实体
     * @param url url
     * @return 友情链接
     */
    @Override
    public Link getOneByUrl(String url) {
        // 新建友链数据实体，并设置url
        SysLink l = new SysLink();
        l.setUrl(url);
        // 调用SysNoticeMapper操作数据库
        l = sysLinkMapper.selectOne(l);
        // 判断得到的实体非空，并转换为业务实体
        return null == l ? null : new Link(l);
    }
}
