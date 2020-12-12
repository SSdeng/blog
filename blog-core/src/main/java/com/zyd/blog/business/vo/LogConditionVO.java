package com.zyd.blog.business.vo;

import com.zyd.blog.framework.object.BaseConditionVO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yadong.zhang email:yadong.zhang0415(a)gmail.com
 * @version 1.0
 * @date 2018/01/09 17:40
 * @since 1.0
 */

/**
 * LogConditionVO继承父类BaseConditionVO属性,为属性自动生成Getter和setter方法
 *
 */
@Getter
@Setter
public class LogConditionVO extends BaseConditionVO {
	private Long userId;//用户id
	private String logLevel;//登入级别
	private String type;//类型
	private Boolean spider;//是否爬登入
}

