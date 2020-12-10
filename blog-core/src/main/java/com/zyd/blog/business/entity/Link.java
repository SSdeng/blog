package com.zyd.blog.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zyd.blog.business.enums.LinkSourceEnum;
import com.zyd.blog.persistence.beans.SysLink;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @website https://www.zhyd.me
 * @version 1.0
 * @date 2018/4/16 16:26
 * @since 1.0
 *
 * 所有的实体类都包装了beans中对应的持久化中的数据实体，
 * beans中的数据实体大多都继承了AbstractDO，抽象数据对象中都有自己的id，创建时间，更新时间
 */
public class Link {//业务实体链接，包装了持久化中的数据实体SysLink
    private SysLink sysLink;

    /**
     * 构造函数
     */
    public Link() {
        this.sysLink = new SysLink();
    }

    /**
     * 带参数的构造函数
     *
     * @param sysLink
     */
    public Link(SysLink sysLink) {
        this.sysLink = sysLink;
    }

    /**
     * @return 返回链接
     */
    @JsonIgnore//在json序列化时将pojo中的一些属性忽略掉，标记在属性或者方法上，返回的json数据即不包含该属性。
    public SysLink getSysLink() {
        return this.sysLink;
    }

    /**
     * @return 返回链接的ID
     */
    public Long getId() {
        return this.sysLink.getId();
    }

    /**
     *
     * 设置链接的ID
     * @param id
     */
    public void setId(Long id) {
        this.sysLink.setId(id);
    }

    /**
     * @return 返回友情链接
     */
    @NotNull(message = "站点地址不能为空")
    @Pattern(regexp = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]", message = "不合法的地址")
    public String getUrl() {
        return this.sysLink.getUrl();
    }

    /**
     * 设置友情链接
     *
     * @param url
     */
    public void setUrl(String url) {
        this.sysLink.setUrl(url);
    }

    /**
     * @return 返回站点的名称
     */
    @NotNull(message = "站点名称不能为空")
    @Length(max = 15, min = 1, message = "站点名称长度建议保持在15个字符以内")
    public String getName() {
        return this.sysLink.getName();
    }

    /**
     * 设置站点的名称
     *
     * @param name
     */
    public void setName(String name) {
        this.sysLink.setName(name);
    }

    /**
     * @return 返回站点的描述
     */
    @NotNull(message = "站点描述不能为空")
    @Length(max = 30, min = 1, message = "站点描述这么长，亲你是想参加作文比赛么？^_^")
    public String getDescription() {
        return this.sysLink.getDescription();
    }

    /**
     * 设置站点的描述
     *
     * @param description
     */
    public void setDescription(String description) {
        this.sysLink.setDescription(description);
    }

    /**
     * @return 返回邮箱
     */
    public String getEmail() {
        return this.sysLink.getEmail();
    }

    /**
     * 设置邮箱
     *
     * @param email
     */
    public void setEmail(String email) {
        this.sysLink.setEmail(email);
    }

    /**
     * @return 返回QQ
     */
    public String getQq() {
        return this.sysLink.getQq();
    }

    /**
     * 设置QQ
     *
     * @param qq
     */
    public void setQq(String qq) {
        this.sysLink.setQq(qq);
    }

    /**
     * @return 返回链接图标
     */
    public String getFavicon() {
        return this.sysLink.getFavicon();
    }

    /**
     * 设置链接图标
     *
     * @param favicon
     */
    public void setFavicon(String favicon) {
        this.sysLink.setFavicon(favicon);
    }

    /**
     * @return 返回链接是否为空
     */
    public Boolean isStatus() {
        Boolean value = this.sysLink.getStatus();
        return value != null ? value : false;
    }

    /**
     * 设置状态
     *
     * @param status
     */
    public void setStatus(Boolean status) {
        this.sysLink.setStatus(status);
    }

    /**
     * @return 返回链接添加来源
     */
    @JsonIgnore//在json序列化时将pojo中的一些属性忽略掉，标记在属性或者方法上，返回的json数据即不包含该属性。
    public LinkSourceEnum getSourceEnum() {
        if (StringUtils.isEmpty(getSource())) {
            return LinkSourceEnum.OTHER;
        }
        return LinkSourceEnum.valueOf(getSource());
    }

    /**
     * @return 返回来源
     */
    public String getSource() {
        return this.sysLink.getSource();
    }

    /**
     * 设置来源
     *
     * @param source
     */
    public void setSource(LinkSourceEnum source) {
        if (null == source) {
            source = LinkSourceEnum.ADMIN;
        }
        this.sysLink.setSource(source.toString());
    }

    /**
     * String方式设置来源
     *
     * @param source
     */
    public void setSource(String source) {
        this.sysLink.setSource(source);
    }

    /**
     * @return 友情链接是否主页显示
     */
    public Boolean isHomePageDisplay() {
        Boolean value = this.sysLink.getHomePageDisplay();
        return value != null ? value : false;
    }

    /**
     * 设置主页显示
     *
     * @param homePageDisplay
     */
    public void setHomePageDisplay(Boolean homePageDisplay) {
        this.sysLink.setHomePageDisplay(homePageDisplay);
    }


    public String getRemark() {
        return this.sysLink.getRemark();
    }

    public void setRemark(String remark) {
        this.sysLink.setRemark(remark);
    }

    /**
     * @return 返回这条记录创建的时间，来源是继承了AbstractDO中的方法
     */
    public Date getCreateTime() {
        return this.sysLink.getCreateTime();
    }

    /**
     * 设置这条记录创建的时间
     *
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.sysLink.setCreateTime(createTime);
    }

    /**
     * @return 返回者条记录更新的时间
     */
    public Date getUpdateTime() {
        return this.sysLink.getUpdateTime();
    }

    /**
     * 设置这条记录更新的时间
     *
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.sysLink.setUpdateTime(updateTime);
    }

}

