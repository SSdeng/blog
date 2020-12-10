package com.zyd.blog.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zyd.blog.business.entity.User;
import com.zyd.blog.business.enums.CommentStatusEnum;
import com.zyd.blog.business.enums.ExtraCommentTypeEnum;
import com.zyd.blog.persistence.beans.BizArticle;
import com.zyd.blog.persistence.beans.BizComment;
import com.zyd.blog.util.HtmlUtil;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 *
 * 所有的实体类都包装了beans中对应的持久化中的数据实体，
 * beans中的数据实体大多都继承了AbstractDO，抽象数据对象中都有自己的id，创建时间，更新时间
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment {//业务实体评论，包装了持久化中的数据实体BizComment
    private BizComment bizComment;

    /**
     * 构造函数
     */
    public Comment() {
        this.bizComment = new BizComment();
    }

    /**
     * 带参数的构造函数
     *
     * @param bizComment
     */
    public Comment(BizComment bizComment) {
        this.bizComment = bizComment;
    }

    /**
     * @return 返回持久化中的数据实体BizComment评论
     */
    @JsonIgnore//在json序列化时将pojo中的一些属性忽略掉，标记在属性或者方法上，返回的json数据即不包含该属性。
    public BizComment getBizComment() {
        return this.bizComment;
    }

    /**
     * @return 返回这条记录的ID
     */
    public Long getId() {
        return this.bizComment.getId();
    }

    /**
     * 设置这条记录的Id
     *
     * @param id
     */
    public void setId(Long id) {
        this.bizComment.setId(id);
    }

    /**
     * @return 返回评论的唯一编号
     */
    public Long getSid() {
        return this.bizComment.getSid();
    }

    /**
     * 设置这条评论的sid,从ExtraCommentTypeEnum中根据sid的值返回对应的url
     *
     * @param sid
     */
    public void setSid(Long sid) {
        this.bizComment.setSid(sid);
    }

    /**
     * 获取评论文章的地址
     *
     * @return
     */
    public String getSourceUrl() {
        Long sid = getSid();
        ExtraCommentTypeEnum extraCommentType = ExtraCommentTypeEnum.getBySid(sid);
        if (null == extraCommentType) {
            return "";
        }
        if (extraCommentType.equals(ExtraCommentTypeEnum.ARTICLE)) {
            return extraCommentType.getUrl() + sid;
        }
        return extraCommentType.getUrl();
    }

    /**
     * @return 返回评论文章的标题
     */
    public String getArticleTitle() {
        BizArticle article = this.getArticle();
        String title = null == article ? null : article.getTitle();
        if (title == null) {
            Long sid = getSid();
            ExtraCommentTypeEnum extraCommentType = ExtraCommentTypeEnum.getBySid(sid);
            title = extraCommentType.getTitle();
        }
        return title;
    }

    /**
     * @return 返回用户Id
     */
    public Long getUserId() {
        return this.bizComment.getUserId();
    }

    /**
     * 设置用户Id
     *
     * @param userId
     */
    public void setUserId(Long userId) {
        this.bizComment.setUserId(userId);
    }

    /**
     * @return 返回回复评论ID
     */
    public Long getPid() {
        return this.bizComment.getPid();
    }

    /**
     * 设置回复评论的ID
     *
     * @param pid
     */
    public void setPid(Long pid) {
        this.bizComment.setPid(pid);
    }

    /**
     * @return 返回评论者的QQ
     */
    public String getQq() {
        return this.bizComment.getQq();
    }

    /**
     * 设置评论者的QQ
     *
     * @param qq
     */
    public void setQq(String qq) {
        this.bizComment.setQq(qq);
    }

    /**
     * @return 返回评论者的昵称
     */
    public String getNickname() {
        return this.bizComment.getNickname();
    }

    /**
     * 设置评论者的昵称
     *
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.bizComment.setNickname(nickname);
    }

    /**
     * @return 返回头像
     */
    public String getAvatar() {
        return this.bizComment.getAvatar();
    }

    /**
     * 设置评论者的头像
     *
     * @param avatar
     */
    public void setAvatar(String avatar) {
        this.bizComment.setAvatar(avatar);
    }

    /**
     * @return 返回评论者的邮箱
     */
    public String getEmail() {
        return this.bizComment.getEmail();
    }

    /**
     * 设置评论者的邮箱
     *
     * @param email
     */
    public void setEmail(String email) {
        this.bizComment.setEmail(email);
    }

    /**
     * @return 返回提交评论时填写的网址
     */
    public String getUrl() {
        return this.bizComment.getUrl();
    }

    /**
     * 设置评论提交前填入的网址
     *
     * @param url
     */
    public void setUrl(String url) {
        this.bizComment.setUrl(url);
    }

    /**
     * @return 返回评论的状态待审核、审核通过、审核失败
     */
    public String getStatus() {
        return this.bizComment.getStatus();
    }

    /**
     * 设置评论的审核状态
     *
     * @param status
     */
    public void setStatus(String status) {
        this.bizComment.setStatus(status);
    }

    /**
     * 未使用
     *
     * @return 评论的审核状态
     */
    public String getStatusDesc() {
        return CommentStatusEnum.valueOf(this.bizComment.getStatus()).getDesc();
    }

    /**
     * @return 返回评论者的IP地址
     */
    public String getIp() {
        return this.bizComment.getIp();
    }

    /**
     * 设置评论者的IP地址
     *
     * @param ip
     */
    public void setIp(String ip) {
        this.bizComment.setIp(ip);
    }

    /**
     * 未使用
     *
     * @return 返回评论者的位置横坐标
     */
    public String getLng() {
        return this.bizComment.getLng();
    }

    /**
     * 设置评论者的位置横坐标
     *
     * @param lng
     */
    public void setLng(String lng) {
        this.bizComment.setLng(lng);
    }

    /**
     * 未使用
     *
     * @return 返回评论者的位置纵坐标
     */
    public String getLat() {
        return this.bizComment.getLat();
    }

    /**
     * 设置评论者的位置的纵坐标
     *
     * @param lat
     */
    public void setLat(String lat) {
        this.bizComment.setLat(lat);
    }

    /**
     * @return 返回评论者的地址信息，可为空
     */
    public String getAddress() {
        return this.bizComment.getAddress();
    }

    /**
     * 设置评论者的位置信息
     *
     * @param address
     */
    public void setAddress(String address) {
        this.bizComment.setAddress(address);
    }

    /**
     * @return 返回评论者的设备信息
     */
    public String getOs() {
        return this.bizComment.getOs();
    }

    /**
     * 设置评论者的设备信息
     *
     * @param os
     */
    public void setOs(String os) {
        this.bizComment.setOs(os);
    }

    /**
     * 未使用
     *
     * @return 返回设备的简短名称
     */
    public String getOsShortName() {
        return this.bizComment.getOsShortName();
    }

    /**
     * 设置设备的简短名称，未使用
     *
     * @param osShortName
     */
    public void setOsShortName(String osShortName) {
        this.bizComment.setOsShortName(osShortName);
    }

    /**
     * @return 返回评论者的游览器信息
     */
    public String getBrowser() {
        return this.bizComment.getBrowser();
    }

    /**
     * 设置评论者的浏览器信息
     *
     * @param browser
     */
    public void setBrowser(String browser) {
        this.bizComment.setBrowser(browser);
    }

    /**
     * 未使用
     *
     * @return 返回评论者浏览器的短名
     */
    public String getBrowserShortName() {
        return this.bizComment.getBrowserShortName();
    }

    /**
     * 设置评论者浏览器的短名，未使用
     *
     * @param browserShortName
     */
    public void setBrowserShortName(String browserShortName) {
        this.bizComment.setBrowserShortName(browserShortName);
    }

    /**
     * @return 返回评论的内容
     */
    public String getContent() {
        return this.bizComment.getContent();
    }

    /**
     * 设置评论的内容
     *
     * @param content
     */
    public void setContent(String content) {
        this.bizComment.setContent(content);
    }

    /**
     * 简短内容
     *
     * @return
     */
    public String getBriefContent() {
        String content = getContent();
        if (!StringUtils.isEmpty(content)) {
            content = HtmlUtil.html2Text(content);
            if (content.length() > 15) {
                content = content.substring(0, 15) + "...";
            }
        }
        return content;
    }

    /**
     * @return
     */
    public String getRemark() {
        return this.bizComment.getRemark();
    }

    /**
     *
     *
     * @param remark
     */
    public void setRemark(String remark) {
        this.bizComment.setRemark(remark);
    }

    /**
     * @return 返回评论的点赞数
     */
    public Integer getSupport() {
        return this.bizComment.getSupport();
    }

    /**
     * 返回评论的点赞数
     *
     * @param support
     */
    public void setSupport(Integer support) {
        this.bizComment.setSupport(support);
    }

    /**
     * @return 返回评论的踩数
     */
    public Integer getOppose() {
        return this.bizComment.getOppose();
    }

    /**
     * 设置评论的踩数
     *
     * @param oppose
     */
    public void setOppose(Integer oppose) {
        this.bizComment.setOppose(oppose);
    }

    /**
     * @return 返回这条记录的创建时间，来源是继承了AbstractDO中的方法
     */
    public Date getCreateTime() {
        return this.bizComment.getCreateTime();
    }

    /**
     * 设置这条记录的创建时间
     *
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.bizComment.setCreateTime(createTime);
    }

    /**
     * @return 返回这条记录的更新时间，来源是继承了AbstractDO中的方法
     */
    public Date getUpdateTime() {
        return this.bizComment.getUpdateTime();
    }

    /**
     * 设置这条记录的更新时间
     *
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.bizComment.setUpdateTime(updateTime);
    }

    /**
     * @return 返回上一级评论
     */
    public BizComment getParent() {
        return this.bizComment.getParent();
    }

    /**
     * 设置上一级评论
     *
     * @param parent
     */
    public void setParent(BizComment parent) {
        this.bizComment.setParent(parent);
    }

    /**
     * @return 返回评论的文章
     */
    public BizArticle getArticle() {
        return this.bizComment.getArticle();
    }

    /**
     * 带设置文章参数的返回评论的文章
     *
     * @param article
     */
    public void getArticle(BizArticle article) {
        this.bizComment.setArticle(article);
    }

    /**
     * @return 返回评论的用户
     */
    public User getUser() {
        return null == this.bizComment.getUser() ? null : new User(this.bizComment.getUser());
    }

}

