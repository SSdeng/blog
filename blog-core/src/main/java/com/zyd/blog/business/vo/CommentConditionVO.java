package com.zyd.blog.business.vo;

import com.zyd.blog.framework.object.BaseConditionVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */

/**
 * CommentConditionVO继承父类BaseConditionVO属性，在比较时不涉及父类成员，只涉及子类属性
 * 评论者条件
 * @param 用户id
 * @param sid
 * @param pid
 * @param qq
 * @param 邮箱
 * @param url
 * @param 状态
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CommentConditionVO extends BaseConditionVO {
	private Long userId;
	private Long sid;
	private Long pid;
	private String qq;
	private String email;
	private String url;
	private String status;
}

