package com.zyd.blog.business.vo;

import com.zyd.blog.framework.object.BaseConditionVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author innodev java team
 * @version 1.0
 * @date 2018/12/14 09:23
 * @since 1.8
 */

/**
 * FileConditionVO继承父类BaseConditionVO属性，在比较时不涉及父类成员，只涉及子类属性
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FileConditionVO extends BaseConditionVO {

}

