package com.zyd.blog.business.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.zyd.blog.business.annotation.RedisCache;
import com.zyd.blog.business.dto.BizCommentDTO;
import com.zyd.blog.business.entity.Comment;
import com.zyd.blog.business.entity.User;
import com.zyd.blog.business.enums.CommentStatusEnum;
import com.zyd.blog.business.enums.ConfigKeyEnum;
import com.zyd.blog.business.enums.TemplateKeyEnum;
import com.zyd.blog.business.service.BizCommentService;
import com.zyd.blog.business.service.MailService;
import com.zyd.blog.business.service.SysConfigService;
import com.zyd.blog.business.vo.CommentConditionVO;
import com.zyd.blog.framework.exception.ZhydCommentException;
import com.zyd.blog.framework.holder.RequestHolder;
import com.zyd.blog.persistence.beans.BizComment;
import com.zyd.blog.persistence.beans.SysConfig;
import com.zyd.blog.persistence.mapper.BizCommentMapper;
import com.zyd.blog.util.*;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 评论
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Slf4j
@Service
public class BizCommentServiceImpl implements BizCommentService {

    @Autowired
    private BizCommentMapper bizCommentMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private MailService mailService;

    @Autowired
    private SysConfigService configService;

    /**
     * 分页查询
     *
     * @param vo 评论状态规定
     * @return 符合条件的评论列表
     */
    @Override
    public PageInfo<Comment> findPageBreakByCondition(CommentConditionVO vo) {
        // 按vo属性分页
        PageMethod.startPage(vo.getPageNumber(), vo.getPageSize());
        // 按vo属性查询
        List<BizComment> list = bizCommentMapper.findPageBreakByCondition(vo);
        // 转换列表类型
        List<Comment> boList = this.getComments(list);
        // 空表
        if (boList == null) {
            return null;
        }
        // 封装bolist到PageInfo对象实现分页
        return new PageInfo<>(boList);
    }

    /**
     * 分页查询
     * 返回Map映射：
     * commentList 对应 评论列表
     * total 对应 评论总数
     * hasNextPage 对应 是否有下一页
     * nextPage 对应 下一页
     *
     * @param vo 评论状态规定
     * @return Map映射
     */
    @Override
    public Map<String, Object> list(CommentConditionVO vo) {
        // 按vo中规定 分页查询
        PageInfo<Comment> pageInfo = findPageBreakByCondition(vo);
        // 创建存储结果的Map对象
        Map<String, Object> map = new HashMap<>();
        // 查询结果不为空
        if (pageInfo != null) {
            // 根据pageInfo中list创建存储BizCommentDTO类的新表 并存入map
            map.put("commentList", convert2DTO(pageInfo.getList()));
            // 从pageInfo获取total 存入map
            map.put("total", pageInfo.getTotal());
            // 从pageInfo获取是否有下一页 存入map
            map.put("hasNextPage", pageInfo.isHasNextPage());
            // 从pageInfo获取下一页 存入map
            map.put("nextPage", pageInfo.getNextPage());
        }
        // 返回构建好的map
        return map;
    }

    /**
     * 将list中Comment类转换为BizCommentDTO类
     * 返回新表
     *
     * @param list 存储Comment类的旧表
     * @return 存储BizCommentDTO的新表
     */
    private List<BizCommentDTO> convert2DTO(List<Comment> list) {
        // list为空
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        // 创建新表
        List<BizCommentDTO> dtoList = new LinkedList<>();
        // 遍历list
        for (Comment comment : list) {
            // 将comment转换为BizCommentDTO
            BizCommentDTO dto = BeanConvertUtil.doConvert(comment, BizCommentDTO.class);
            // 将comment的parent转换为BizCommentDTO类 并为置dto的parent
            dto.setParentDTO(BeanConvertUtil.doConvert(comment.getParent(), BizCommentDTO.class));
            // comment的user不为空
            if (null != comment.getUser()) {
                // 记录用户类型
                dto.setUserType(comment.getUser().getUserTypeEnum());
            }
            // 将dto加入新表
            dtoList.add(dto);
        }
        // 返回新表
        return dtoList;
    }

    /**
     * admin发表评论
     *
     * @param comment 评论
     * @throws ZhydCommentException
     */
    @Override
    @RedisCache(flush = true)
    public void commentForAdmin(Comment comment) {
        // 获取系统配置
        Map<String, Object> config = configService.getConfigs();
        // 从session获取当前user
        User user = SessionUtil.getUser();
        // 记录qq
        comment.setQq(user.getQq());
        // 记录邮箱
        comment.setEmail(user.getEmail());
        // 记录昵称
        comment.setNickname(user.getNickname());
        // 记录头像
        comment.setAvatar(user.getAvatar());
        // 记录博客url
        comment.setUrl((String) config.get(ConfigKeyEnum.SITE_URL.getKey()));
        // 记录用户id
        comment.setUserId(user.getId());
        // 记录状态为审核通过
        comment.setStatus(CommentStatusEnum.APPROVED.toString());
        // 设定Pid为原Id
        comment.setPid(comment.getId());
        // 将Id设为空
        comment.setId(null);
        // 发表评论
        this.comment(comment);
    }

    /**
     * 发表评论
     *
     * @param comment 评论
     * @return 更新后的comment
     */
    @Override
    @RedisCache(flush = true)
    public Comment comment(Comment comment) {
        // 获取允许匿名评论的配置
        SysConfig sysConfig = configService.getByKey(ConfigKeyEnum.ANONYMOUS.getKey());
        boolean anonymous = true;
        if (null != sysConfig) {
            // 查看配置是否允许匿名评论
            anonymous = "1".equals(sysConfig.getConfigValue());
        }

        // 非匿名且用户未登录
        if (!anonymous && !SessionUtil.isLogin()) {
            // 拒绝用户评论
            throw new ZhydCommentException("站长已关闭匿名评论，请先登录！");
        }

        // 过滤文本内容，防止xss
        this.filterContent(comment);

        // 已登录且非匿名，使用当前登录用户的信息评论
        if (SessionUtil.isLogin()) {
            // comment存入登录用户信息
            this.setCurrentLoginUserInfo(comment);
        // 未登录 匿名评论
        } else {
            // comment存入匿名用户信息
            this.setCurrentAnonymousUserInfo(comment);
        }

        // 用户没有头像时 使用随机默认的头像
        if (StringUtils.isEmpty(comment.getAvatar())) {
            // 获取随机头像表
            List<String> avatars = configService.getRandomUserAvatar();
            // 头像列表不为空
            if (!CollectionUtils.isEmpty(avatars)) {
                // 打乱列表顺序
                Collections.shuffle(avatars);
                // 随机数字选取头像
                int randomIndex = new Random().nextInt(avatars.size());
                comment.setAvatar(avatars.get(randomIndex));
            }
        }

        // 评论状态为空
        if (StringUtils.isEmpty(comment.getStatus())) {
            // 将评论状态设为“正在审核”
            comment.setStatus(CommentStatusEnum.VERIFYING.toString());
        }

        // set当前评论者的设备信息
        this.setCurrentDeviceInfo(comment);

        // set当前评论者的位置信息
        this.setCurrentLocation(comment);

        // 保存到数据库
        ((BizCommentServiceImpl) AopContext.currentProxy()).insert(comment);

        // 发送邮件通知
        this.sendEmail(comment);
        // 返回更新后的comment
        return comment;
    }

    /**
     * 过滤评论内容
     *
     * @param comment 评论
     */
    private void filterContent(Comment comment) {
        // 获取评论内容
        String content = comment.getContent();
        // 评论内容为空
        if (StringUtils.isEmpty(content) || "\n".equals(content)) {
            throw new ZhydCommentException("说点什么吧");
        }
        // 过滤非法属性和无用的空标签
        if (!XssKillerUtil.isValid(content) || !XssKillerUtil.isValid(comment.getAvatar())) {
            throw new ZhydCommentException("请不要使用特殊标签");
        }
        // 剔除多余属性、标签
        content = XssKillerUtil.clean(content.trim()).replaceAll("(<p><br></p>)|(<p></p>)", "");
        // 过滤后 content为空
        if (StringUtils.isEmpty(content) || "\n".equals(content)) {
            throw new ZhydCommentException("说点什么吧");
        }
        // 将过滤后的content放回comment
        comment.setContent(content);
    }

    /**
     * 保存当前匿名用户的信息
     *
     * @param comment 评论
     */
    private void setCurrentAnonymousUserInfo(Comment comment) {
        // 昵称为空
        if (StringUtils.isEmpty(comment.getNickname())) {
            throw new ZhydCommentException("必须输入昵称");
        }
        // 将comment中html格式的数据转换为text
        comment.setNickname(HtmlUtil.html2Text(comment.getNickname()));
        comment.setQq(HtmlUtil.html2Text(comment.getQq()));
        comment.setAvatar(HtmlUtil.html2Text(comment.getAvatar()));
        comment.setEmail(HtmlUtil.html2Text(comment.getEmail()));
        comment.setUrl(HtmlUtil.html2Text(comment.getUrl()));
    }

    /**
     * 保存当前登录用户的信息
     *
     * @param comment 评论
     */
    private void setCurrentLoginUserInfo(Comment comment) {
        // 从session获取user
        User loginUser = SessionUtil.getUser();
        // 将html格式的数据转为text
        // 将comment的值设定为loginUser存储的值
        comment.setNickname(HtmlUtil.html2Text(loginUser.getNickname()));
        comment.setQq(HtmlUtil.html2Text(loginUser.getQq()));
        comment.setAvatar(HtmlUtil.html2Text(loginUser.getAvatar()));
        comment.setEmail(HtmlUtil.html2Text(loginUser.getEmail()));
        comment.setUrl(HtmlUtil.html2Text(loginUser.getBlog()));
        comment.setUserId(loginUser.getId());
    }

    /**
     * 保存当前评论时的设备信息
     *
     * @param comment 评论
     */
    private void setCurrentDeviceInfo(Comment comment) {
        // 获取httpHeader中的User-Agent
        String ua = RequestUtil.getUa();
        // 从字符串中提取信息 构建UserAgent对象
        UserAgent agent = UserAgent.parseUserAgentString(ua);
        // 获取浏览器
        Browser browser = agent.getBrowser();
        // 记录浏览器名
        String browserInfo = browser.getName();
        // 记录浏览器版本
        Version version = agent.getBrowserVersion();
        // 版本不为空
        if (version != null) {
            // 用浏览器+版本号构成浏览器信息
            browserInfo += " " + version.getVersion();
        }
        // 记录浏览器信息
        comment.setBrowser(browserInfo);
        // 获取操作系统
        OperatingSystem os = agent.getOperatingSystem();
        // 记录操作系统
        comment.setOs(os.getName());
        // 记录Ip地址
        comment.setIp(RequestUtil.getIp());
    }

    /**
     * 保存当前评论时的位置信息
     *
     * @param comment 评论
     */
    private void setCurrentLocation(Comment comment) {
        // 获取系统配置
        Map<String, Object> config = configService.getConfigs();
        try {
            // 向百度api_ak发送请求 查询Ip地址对应实际地址 获取返回的json信息
            String locationJson = RestClientUtil.get(UrlBuildUtil.getLocationByIp(comment.getIp(), (String) config.get(ConfigKeyEnum.BAIDU_API_AK.getKey())));
            // 从json中提取报含位置信息的JSONObject
            JSONObject locationContent = JSON.parseObject(locationJson).getJSONObject("content");
            // 获取位置点信息
            JSONObject point = locationContent.getJSONObject("point");
            // 设置地址纬度
            comment.setLat(point.getString("y"));
            // 设置地址经度
            comment.setLng(point.getString("x"));

            // JSON对象中含有详细地址信息
            if (locationContent.containsKey("address_detail")) {
                // 提取详细地址JSONObject
                JSONObject addressDetail = locationContent.getJSONObject("address_detail");
                // 获取城市地址
                String city = addressDetail.getString("city");
                // 获取区地址
                String district = addressDetail.getString("district");
                // 获取街道地址
                String street = addressDetail.getString("street");
                // 组合地址信息
                String address = addressDetail.getString("province") + (StringUtils.isEmpty(city) ? "" : city) +
                        (StringUtils.isEmpty(district) ? "" : district) +
                        (StringUtils.isEmpty(street) ? "" : street);
                // 记录地址
                comment.setAddress(address);
            }
        } catch (Exception e) {
            log.error("获取地址失败", e);
        }
        // 地址属性值为空
        if (StringUtils.isEmpty(comment.getAddress())) {
            // 地址设为“未知”
            comment.setAddress("未知");
        }
    }

    /**
     * 给被评论用户发送邮件
     *
     * @param comment 评论
     */
    private void sendEmail(Comment comment) {
        // 发送邮件通知，此处如发生异常不应阻塞当前的业务流程
        // 可以进行日志记录等操作
        try {
            // comment有合法pid
            if (null != comment.getPid() && 0 != comment.getPid()) {
                // 给被评论的用户发送通知
                Comment commentDB = this.getByPrimaryKey(comment.getPid());
                mailService.send(commentDB, TemplateKeyEnum.TM_COMMENT_REPLY, false);
            // 发送给管理员
            } else {
                mailService.sendToAdmin(comment);
            }
        } catch (Exception e) {
            log.error("发送评论通知邮件时发生异常", e);
        }
    }

    /**
     * 查询近期评论
     *
     * @param pageSize 分页大小
     * @return 近期评论列表
     */
    @Override
    @RedisCache
    public List<Comment> listRecentComment(int pageSize) {
        // 搜索条件控制类
        CommentConditionVO vo = new CommentConditionVO();
        // 设定分页大小
        vo.setPageSize(pageSize);
        // 设置评论状态为“审核通过”
        vo.setStatus(CommentStatusEnum.APPROVED.toString());
        // 分页
        PageMethod.startPage(vo.getPageNumber(), vo.getPageSize());
        // 查找
        List<BizComment> list = bizCommentMapper.findPageBreakByCondition(vo);
        // 返回结果
        return getComments(list);
    }

    /**
     * List<BizComment> -> List<Comment>
     *
     * @param list 存储BizComment类的列表
     * @return 存储Comment类的列表
     */
    private List<Comment> getComments(List<BizComment> list) {
        // list为空
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        // 拷贝
        List<Comment> boList = new ArrayList<>();
        for (BizComment bizComment : list) {
            // 将bizComment类向上转化为父类Comment
            boList.add(new Comment(bizComment));
        }
        // 返回新表
        return boList;
    }

    /**
     * 查询未审核的评论
     *
     * @param pageSize 分页大小
     * @return 未审核的评论列表
     */
    @Override
    public List<Comment> listVerifying(int pageSize) {
        // 搜索条件控制类
        CommentConditionVO vo = new CommentConditionVO();
        // 设定分页大小
        vo.setPageSize(pageSize);
        // 设置评论状态为“正在审核”
        vo.setStatus(CommentStatusEnum.VERIFYING.toString());
        // 分页搜索
        PageInfo<Comment> pageInfo = findPageBreakByCondition(vo);
        // 不为空则返回评论列表
        return null == pageInfo ? null : pageInfo.getList();
    }

    /**
     * 点赞
     *
     * @param id 评论id
     */
    @Override
    @RedisCache(flush = true)
    public void doSupport(Long id) {
        // 创建点赞操作key = 点赞者ip + ‘_doSupport_’ + 评论id
        String key = IpUtil.getRealIp(RequestHolder.getRequest()) + "_doSupport_" + id;
        // 获取缓存值
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        // 缓存中存有该操作key
        if ((boolean) redisTemplate.hasKey(key)) {
            // 同一ip对同一评论一小时内不能多次点赞
            throw new ZhydCommentException("一个小时只能点一次赞哈~");
        }
        // 执行点赞操作
        bizCommentMapper.doSupport(id);
        // 记录该点赞操作
        operations.set(key, id, 1, TimeUnit.HOURS);
    }

    /**
     * 点踩
     *
     * @param id 评论id
     */
    @Override
    @RedisCache(flush = true)
    public void doOppose(Long id) {
        // 创建点踩操作key = 点踩者ip + ‘_doOppose_’ + 评论id
        String key = IpUtil.getRealIp(RequestHolder.getRequest()) + "_doOppose_" + id;
        // 获取缓存值
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        // 缓存中存有该操作key
        if ((boolean) redisTemplate.hasKey(key)) {
            // 同一ip对同一评论一小时内不能多次点踩
            throw new ZhydCommentException("一个小时只能踩一次哈~又没什么深仇大恨");
        }
        // 执行点踩操作
        bizCommentMapper.doOppose(id);
        // 记录该点赞操作
        operations.set(key, id, 1, TimeUnit.HOURS);
    }

    /**
     * 将评论上传到数据库
     *
     * @param entity 评论实体
     * @return 更新后的entity
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedisCache(flush = true)
    public Comment insert(Comment entity) {
        // entity不可为空
        Assert.notNull(entity, "Comment不可为空！");
        // 记录更新时间
        entity.setUpdateTime(new Date());
        // 记录创建时间
        entity.setCreateTime(new Date());
        // 插入数据库
        bizCommentMapper.insertSelective(entity.getBizComment());
        // 返回更新后的entity
        return entity;
    }

    /**
     * 按主键删除评论
     *
     * @param primaryKey 评论主键
     * @return 删除结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedisCache(flush = true)
    public boolean removeByPrimaryKey(Long primaryKey) {
        return bizCommentMapper.deleteByPrimaryKey(primaryKey) > 0;
    }

    /**
     * 更新评论
     *
     * @param entity 评论实体
     * @return 更新结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedisCache(flush = true)
    public boolean updateSelective(Comment entity) {
        // 实体不可为空
        Assert.notNull(entity, "Comment不可为空！");
        // 记录更新时间
        entity.setUpdateTime(new Date());
        // 更新数据库 返回结果
        return bizCommentMapper.updateByPrimaryKeySelective(entity.getBizComment()) > 0;
    }

    /**
     * 按主键获取评论实体
     *
     * @param primaryKey 评论主键
     * @return 对应评论对象
     */
    @Override
    public Comment getByPrimaryKey(Long primaryKey) {
        // 主键参数不可为空
        Assert.notNull(primaryKey, "PrimaryKey不可为空！");
        // 按主键获取评论对象
        BizComment entity = bizCommentMapper.getById(primaryKey);
        // 不为空则返回转换为Comment类的对象
        return null == entity ? null : new Comment(entity);
    }
}
