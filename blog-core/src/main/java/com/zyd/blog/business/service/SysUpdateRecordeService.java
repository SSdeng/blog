package com.zyd.blog.business.service;


import com.zyd.blog.framework.object.AbstractService;
import com.zyd.blog.business.entity.UpdateRecorde;
import com.zyd.blog.business.vo.UpdateRecordeConditionVO;
import com.github.pagehelper.PageInfo;

/**
 * 更新记录服务接口类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
public interface SysUpdateRecordeService extends AbstractService<UpdateRecorde, Long> {

    /**
     * 分页查询更新记录
     * 使用PageHelper开源项目实现分页 <br>
     * 工作原理<br>
     *  1.使用PageHelper.startPage时在当前线程上下文中设置一个ThreadLocal变量<br>
     *  2.在 ThreadLocal中设置了分页参数，之后在查询执行的时候，获取当前线程中的分页参数<br>
     *  3.执行查询的时候通过拦截器在sql语句中添加分页参数，之后实现分页查询，查询结束后在 finally 语句中清除ThreadLocal中的查询参数
     * @param vo 更新记录条件VO，包含分页参数
     * @return PageInfo
     */
    PageInfo<UpdateRecorde> findPageBreakByCondition(UpdateRecordeConditionVO vo);
}
