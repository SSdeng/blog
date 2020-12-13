package com.zyd.blog.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zyd.blog.business.enums.*;
import com.zyd.blog.framework.object.AbstractBO;
import com.zyd.blog.persistence.beans.SysUser;
import com.zyd.blog.util.PasswordUtil;
import org.springframework.util.StringUtils;

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
public class User extends AbstractBO {//业务实体User，包装了持久化中的数据实体SysUser
    private SysUser sysUser;

    /**
     * 构造函数
     */
    public User() {
        this.sysUser = new SysUser();
    }

    /**
     * 带参数的构造函数
     *
     * @param sysUser
     */
    public User(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    /**
     *构造函数
     *
     * @param loginname
     * @param password
     */
    public User(String loginname, String password) {
        this();
        setUsername(loginname);
        if (!StringUtils.isEmpty(password)) {
            try {
                setPassword(PasswordUtil.encrypt(password, loginname));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return 返回用户
     */
    @JsonIgnore//在json序列化时将pojo中的一些属性忽略掉，标记在属性或者方法上，返回的json数据即不包含该属性。
    public SysUser getSysUser() {
        return this.sysUser;
    }

    /**
     * @return 返回用户ID
     */
    public Long getId() {
        return this.sysUser.getId();
    }

    /**
     * 设置用户ID
     *
     * @param id
     */
    public void setId(Long id) {
        this.sysUser.setId(id);
    }

    /**
     * @return 返回用户昵称
     */
    public String getNickname() {
        return this.sysUser.getNickname();
    }

    /**
     * 设置用户昵称
     *
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.sysUser.setNickname(nickname);
    }

    /**
     * @return 返回用户手机号码
     */
    public String getMobile() {
        return this.sysUser.getMobile();
    }

    /**
     * 设置用户的手机号码
     *
     * @param mobile
     */
    public void setMobile(String mobile) {
        this.sysUser.setMobile(mobile);
    }

    /**
     * @return 返回用户名
     */
    public String getUsername() {
        return this.sysUser.getUsername();
    }

    /**
     * 设置用户名
     *
     * @param username
     */
    public void setUsername(String username) {
        this.sysUser.setUsername(username);
    }

    /**
     * @return 返回用户的密码
     */
    public String getPassword() {
        return this.sysUser.getPassword();
    }

    /**
     * 设置用户的密码
     *
     * @param password
     */
    public void setPassword(String password) {
        this.sysUser.setPassword(password);
    }

    /**
     * @return 返回用户的邮箱
     */
    public String getEmail() {
        return this.sysUser.getEmail();
    }

    /**
     * 设置用户的邮箱
     *
     * @param email
     */
    public void setEmail(String email) {
        this.sysUser.setEmail(email);
    }

    /**
     * @return 返回用户的QQ
     */
    public String getQq() {
        return this.sysUser.getQq();
    }

    /**
     * 设置用户的QQ
     *
     * @param qq
     */
    public void setQq(String qq) {
        this.sysUser.setQq(qq);
    }

    /**
     * @return 返回用户的生日
     */
    public Date getBirthday() {
        return this.sysUser.getBirthday();
    }

    /**
     * 设置用户的生日
     *
     * @param birthday
     */
    public void setBirthday(Date birthday) {
        this.sysUser.setBirthday(birthday);
    }

    /**
     * @return 返回用户的性别
     */
    public Integer getGender() {
        return this.sysUser.getGender();
    }

    /**
     * 设置用户的性别
     *
     * @param gender
     */
    public void setGender(UserGenderEnum gender) {
        if (gender != null && gender.getCode() != -1) {
            this.sysUser.setGender(gender.getCode());
        }
    }

    /**
     * 设置用户的性别
     *
     * @param gender
     */
    public void setGender(Integer gender) {
        this.sysUser.setGender(gender);
    }

    /**
     * @return 返回用户图标
     */
    public String getAvatar() {
        return this.sysUser.getAvatar();
    }

    /**
     * 设置用户图标
     *
     * @param avatar
     */
    public void setAvatar(String avatar) {
        this.sysUser.setAvatar(avatar);
    }

    /**
     * @return 返回用户账号隐私程度？公开：不公开
     */
    public Integer getPrivacy() {
        return this.sysUser.getPrivacy();
    }

    /**
     * 设置用户隐私程度
     *
     * @param privacy
     */
    public void setPrivacy(Integer privacy) {
        this.sysUser.setPrivacy(privacy);
    }

    /**
     * @return 返回用户隐私程度枚举类型
     */
    public UserPrivacyEnum getUserPrivacyEnum() {
        return UserPrivacyEnum.get(getPrivacy());
    }

    /**
     * @return 返回通知
     */
    public Integer getNotification() {
        return this.sysUser.getNotification();
    }

    /**
     * 设置通知
     *
     * @param userNotificationEnum
     */
    public void setNotification(UserNotificationEnum userNotificationEnum) {
        if (null != userNotificationEnum) {
            setNotification(userNotificationEnum.getCode());
        }
    }

    /**
     * 设置通知
     *
     * @param notification
     */
    public void setNotification(Integer notification) {
        this.sysUser.setNotification(notification);
    }

    /**
     * @return 返回通知的枚举类型
     */
    public UserNotificationEnum getUserNotificationEnum() {
        return UserNotificationEnum.get(getNotification());
    }

    /**
     * @return 返回用户的类型
     */
    public String getUserType() {
        return this.sysUser.getUserType();
    }

    /**
     * 设置用户类型
     *
     * @param userTypeEnum
     */
    public void setUserType(UserTypeEnum userTypeEnum) {
        if (null != userTypeEnum) {
            setUserType(userTypeEnum.toString());
        }
    }

    /**
     * 设置用户类型
     *
     * @param userType
     */
    public void setUserType(String userType) {
        this.sysUser.setUserType(userType);
    }

    /**
     * @return 返回用户类型枚举类型
     */
    public UserTypeEnum getUserTypeEnum() {
        return UserTypeEnum.getByType(this.sysUser.getUserType());
    }

    /**
     * @return 返回用户所属的公司
     */
    public String getCompany() {
        return this.sysUser.getCompany();
    }

    /**
     * 设置用户所属的公司
     *
     * @param company
     */
    public void setCompany(String company) {
        this.sysUser.setCompany(company);
    }

    /**
     * @return 返回用户的博客
     */
    public String getBlog() {
        return this.sysUser.getBlog();
    }

    /**
     * 设置用户的博客
     *
     * @param blog
     */
    public void setBlog(String blog) {
        this.sysUser.setBlog(blog);
    }

    /**
     * @return 返回用户所在地
     */
    public String getLocation() {
        return this.sysUser.getLocation();
    }

    /**
     * 设置用户所在地
     *
     * @param location
     */
    public void setLocation(String location) {
        this.sysUser.setLocation(location);
    }

    public String getSource() {
        return this.sysUser.getSource();
    }

    public void setSource(String source) {
        this.sysUser.setSource(source);
    }

    /**
     * 设置用户的统一识别码
     *
     * @param uuid
     */
    public void setUuid(String uuid) {
        this.sysUser.setUuid(uuid);
    }

    /**
     * @return 返回用户的统一识别码
     */
    public String getUuid() {
        return this.sysUser.getUuid();
    }

    /**
     * @return 返回用户的分数
     */
    public Integer getScore() {
        return this.sysUser.getScore();
    }

    /**
     * 设置用户的分数
     *
     * @param score
     */
    public void setScore(Integer score) {
        this.sysUser.setScore(score);
    }

    /**
     * @return 返回用户的经历
     */
    public Integer getExperience() {
        return this.sysUser.getExperience();
    }

    /**
     * 设置用户的经历
     *
     * @param experience
     */
    public void setExperience(Integer experience) {
        this.sysUser.setExperience(experience);
    }

    /**
     * @return 返回用户的原IP
     */
    public String getRegIp() {
        return this.sysUser.getRegIp();
    }

    /**
     * 设置用户的原IP
     *
     * @param regIp
     */
    public void setRegIp(String regIp) {
        this.sysUser.setRegIp(regIp);
    }

    /**
     * @return 返回用户上次登陆的IP
     */
    public String getLastLoginIp() {
        return this.sysUser.getLastLoginIp();
    }

    /**
     * 设置用户上次登陆的IP
     *
     * @param lastLoginIp
     */
    public void setLastLoginIp(String lastLoginIp) {
        this.sysUser.setLastLoginIp(lastLoginIp);
    }

    /**
     * @return 返回用户上次登陆的时间
     */
    public Date getLastLoginTime() {
        return this.sysUser.getLastLoginTime();
    }

    /**
     * 设置用户上次登陆的时间
     *
     * @param lastLoginTime
     */
    public void setLastLoginTime(Date lastLoginTime) {
        this.sysUser.setLastLoginTime(lastLoginTime);
    }

    /**
     * @return 返回用户登陆的次数
     */
    public Integer getLoginCount() {
        return this.sysUser.getLoginCount();
    }

    /**
     * 设置用户登陆的次数
     *
     * @param loginCount
     */
    public void setLoginCount(Integer loginCount) {
        this.sysUser.setLoginCount(loginCount);
    }

    public String getRemark() {
        return this.sysUser.getRemark();
    }

    public void setRemark(String remark) {
        this.sysUser.setRemark(remark);
    }

    /**
     * @return 返回用户的状态
     */
    public Integer getStatus() {
        return this.sysUser.getStatus();
    }

    /**
     * 设置用户的状态
     *
     * @param status
     */
    public void setStatus(Integer status) {
        this.sysUser.setStatus(status);
    }

    /**
     * @return 返回用户的状态枚举类型
     */
    public UserStatusEnum getStatusEnum() {
        return UserStatusEnum.get(this.sysUser.getStatus());
    }

    /**
     * @return 返回用户的创建时间
     */
    public Date getCreateTime() {
        return this.sysUser.getCreateTime();
    }

    /**
     * 设置用户的创建时间
     *
     * @param regTime
     */
    public void setCreateTime(Date regTime) {
        this.sysUser.setCreateTime(regTime);
    }

    /**
     * @return 返回用户的更新时间
     */
    public Date getUpdateTime() {
        return this.sysUser.getUpdateTime();
    }

    /**
     * 设置用户的更新时间
     *
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.sysUser.setUpdateTime(updateTime);
    }

}
