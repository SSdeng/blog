package com.zyd.blog.business.service;


import com.github.pagehelper.PageInfo;
import com.zyd.blog.business.entity.Comment;
import com.zyd.blog.business.vo.CommentConditionVO;
import com.zyd.blog.framework.exception.ZhydCommentException;
import com.zyd.blog.framework.object.AbstractService;

import java.util.List;
import java.util.Map;

/**
 * 评论
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
public interface BizCommentService extends AbstractService<Comment, Long> {

    /**
     * 分页查询
     *
     * @param vo 评论状态规定
     * @return 符合条件的评论列表
     */
    PageInfo<Comment> findPageBreakByCondition(CommentConditionVO vo);

    /**
     * 分页查询
     * 返回Map映射：
     * commentList 对应 评论列表
     * total 对应 评论总数
     * hasNextPage 对应 是否有下一页
     * nextPage 对应 下一页
     *
     * @param vo 评论状态规定
     * @return Map映射
     */
    Map<String, Object> list(CommentConditionVO vo);

    /**
     * admin发表评论
     *
     * @param comment 评论
     * @throws ZhydCommentException
     */
    void commentForAdmin(Comment comment) throws ZhydCommentException;

    /**
     * 发表评论
     *
     * @param comment 评论
     * @return 更新后的comment
     */
    Comment comment(Comment comment) throws ZhydCommentException;

    /**
     * 查询近期评论
     *
     * @param pageSize 分页大小
     * @return 近期评论列表
     */
    List<Comment> listRecentComment(int pageSize);

    /**
     * 查询未审核的评论
     *
     * @param pageSize 分页大小
     * @return 未审核的评论列表
     */
    List<Comment> listVerifying(int pageSize);

    /**
     * 点赞
     *
     * @param id 评论id
     */
    void doSupport(Long id);

    /**
     * 点踩
     *
     * @param id 评论id
     */
    void doOppose(Long id);
}
