package com.zyd.blog.business.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zyd.blog.business.enums.UserTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 评论详情，用于页面传输
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BizCommentDTO {
    @JsonIgnore
    BizCommentDTO parentDTO;
    private Long id;
    @JsonIgnore
    /**
     * 评论的概况
     * @param createTime 创建时间
     * @param sid
     * @param pid
     * @param nickname 昵称
     * @param avatar 头像
     * @param url
     * @param address 地址
     * @param os 操作系统
     * @param osShortName 操作系统短名
     * @param browser 浏览器
     * @param browserShortName 浏览器简称
     * @param content 内容
     * @param support 点赞数量
     * @param oppose 踩的数量
     *
     */
    private Date createTime;//创建时间
    private Long sid;
    private Long pid;
    private String nickname;//昵称
    private String avatar;//头像
    private String url;
    private String address;//地址
    private String os;//操作系统
    private String osShortName;//操作系统短名
    private String browser;//浏览器
    private String browserShortName;//浏览器短名
    private String content;//内容
    private Integer support;//赞的数量
    private Integer oppose;//踩的数量

    @JsonIgnore
    private UserTypeEnum userType;
    //判断使用者类型是否是管理员
    public boolean isRoot() {
        return null != userType && userType == UserTypeEnum.ROOT;
    }
    //定义评论的创建时间格式yyyy-MM-dd HH:mm
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    //获取评论创建时间
    public Date getCreateTimeString() {
        return this.getCreateTime();
    }
    //获取上一页面
    public BizCommentDTO getParent() {
        return this.parentDTO;
    }
}
