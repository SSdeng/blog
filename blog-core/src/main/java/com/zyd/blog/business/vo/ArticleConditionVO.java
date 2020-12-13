package com.zyd.blog.business.vo;

import com.zyd.blog.business.entity.Article;
import com.zyd.blog.framework.object.BaseConditionVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */

/**
 * ArticleConditionVO继承父类BaseConditionVO属性，在比较时不涉及父类成员，只涉及子类属性
 * @param 文章
 * @param 类型id
 * @param tagid
 * @param 是否置顶
 * @param 状态
 * @param 是否推荐
 * @param 是否是原来的
 * @param 是否随机
 * @param tagid容器
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ArticleConditionVO extends BaseConditionVO {
	private Article article;//文章
	private Long typeId;//类型id
	private Long tagId;//标签id
	private Integer status;//文章状态
	private Boolean top;//是否置顶
	private Boolean recommended;//是否推荐
	private Boolean original;//是否原初
	private Boolean random;//是否随机
	private List<Long> tagIds;//标签id容器
}

