package com.zyd.blog.business.vo;

import com.zyd.blog.framework.object.BaseConditionVO;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		LogConditionVO that = (LogConditionVO) o;
		return Objects.equals(userId, that.userId) && Objects.equals(logLevel, that.logLevel) && Objects.equals(type, that.type) && Objects.equals(spider, that.spider);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), userId, logLevel, type, spider);
	}
}

