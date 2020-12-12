package com.zyd.blog.business.vo;

import com.zyd.blog.business.entity.Link;
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
 *LinkConditionVO继承父类BaseConditionVO属性，在比较时不涉及父类成员，只涉及子类属性
 * @param 链接
 * @param 状态
 * @param 主页展示
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LinkConditionVO extends BaseConditionVO {
	private Link link;//连接
	private Integer status;//状态
	private Integer homePageDisplay;//主页展示

	/**
	 * 连接
	 */
	public LinkConditionVO() {
	}

	/**
	 * 初始化链接
	 * @param status
	 * @param homePageDisplay
	 */
	public LinkConditionVO(Integer status, Integer homePageDisplay) {
		this.status = status;
		this.homePageDisplay = homePageDisplay;
	}
}

