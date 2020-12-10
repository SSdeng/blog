package com.zyd.blog.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyd.blog.business.annotation.RedisCache;
import com.zyd.blog.business.entity.Article;
import com.zyd.blog.business.entity.User;
import com.zyd.blog.business.enums.ArticleStatusEnum;
import com.zyd.blog.business.enums.CommentStatusEnum;
import com.zyd.blog.business.enums.FileUploadType;
import com.zyd.blog.business.enums.ResponseStatus;
import com.zyd.blog.business.service.BizArticleService;
import com.zyd.blog.business.service.BizArticleTagsService;
import com.zyd.blog.business.vo.ArticleConditionVO;
import com.zyd.blog.file.FileUploader;
import com.zyd.blog.file.entity.VirtualFile;
import com.zyd.blog.framework.exception.ZhydArticleException;
import com.zyd.blog.framework.exception.ZhydException;
import com.zyd.blog.framework.holder.RequestHolder;
import com.zyd.blog.persistence.beans.*;
import com.zyd.blog.persistence.mapper.*;
import com.zyd.blog.plugin.file.GlobalFileUploader;
import com.zyd.blog.util.IpUtil;
import com.zyd.blog.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 文章列表
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Slf4j
@Service
public class BizArticleServiceImpl implements BizArticleService {

    @Autowired
    private BizArticleMapper bizArticleMapper;
    @Autowired
    private BizArticleLoveMapper bizArticleLoveMapper;
    @Autowired
    private BizArticleLookMapper bizArticleLookMapper;
    @Autowired
    private BizArticleTagsMapper bizArticleTagsMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private BizArticleTagsService articleTagsService;
    @Autowired
    private BizCommentMapper commentMapper;

    /**
     * 分页查询
     *
     * @param vo 文章状态规定
     * @return 符合条件的文章列表
     */
    @Override
    public PageInfo<Article> findPageBreakByCondition(ArticleConditionVO vo) {
        // 按照vo中参数分页
        PageHelper.startPage(vo.getPageNumber(), vo.getPageSize());
        // 查询
        List<BizArticle> list = bizArticleMapper.findPageBreakByCondition(vo);
        // 列表为空
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        // 将list存储的所有文章类的id取出
        List<Long> ids = list.stream().map(BizArticle::getId).collect(Collectors.toList());

        // 获取指定文章的标签集合
        List<BizArticle> listTag = bizArticleMapper.listTagsByArticleId(ids);

        // 将listTag转换为Map类 key值为Id value值为listTag存值 key值重复时保留旧值
        Map<Long, BizArticle> tagMap = listTag.stream().collect(Collectors.toMap(BizArticle::getId, a -> a, (k1, k2) -> k1));

        // 创建存储文章信息的列表
        List<Article> boList = new LinkedList<>();
        // 遍历list
        for (BizArticle bizArticle : list) {
            // 按Id取出对应文章标签信息
            BizArticle tagArticle = tagMap.get(bizArticle.getId());
            // 标签信息为空
            if (null == tagArticle) {
                log.warn("文章[{}] 未绑定标签信息，或者已绑定的标签不存在！", bizArticle.getTitle());
            } else {
                // 将标签信息存入对象中
                bizArticle.setTags(tagArticle.getTags());
            }
            // 更新文章相关信息
            this.subquery(bizArticle);
            // 更新完毕后 将文章信息存入表中
            boList.add(new Article(bizArticle));
        }
        // 封装list到pageInfo对象实现分页
        PageInfo bean = new PageInfo<BizArticle>(list);
        // 将boList放入pageInfo
        bean.setList(boList);
        return bean;
    }

    /**
     * 站长推荐
     *
     * @param pageSize 分页大小
     * @return 站长推荐文章列表
     */
    @Override
    public List<Article> listRecommended(int pageSize) {
        // 设置文章搜索条件
        ArticleConditionVO vo = new ArticleConditionVO();
        // 推荐值为真
        vo.setRecommended(true);
        // 已被发布
        vo.setStatus(ArticleStatusEnum.PUBLISHED.getCode());
        // 设定分页大小
        vo.setPageSize(pageSize);
        // 调用分页查询 获取PageInfo类查询结果
        PageInfo pageInfo = this.findPageBreakByCondition(vo);
        // 为空返回空指针 否则返回pageInfo中封装的列表
        return null == pageInfo ? null : pageInfo.getList();
    }

    /**
     * 近期文章
     *
     * @param pageSize 分页大小
     * @return 近期文章列表
     */
    @Override
    public List<Article> listRecent(int pageSize) {
        // 设置文章搜索条件
        ArticleConditionVO vo = new ArticleConditionVO();
        // 设置分页大小
        vo.setPageSize(pageSize);
        // 已发布
        vo.setStatus(ArticleStatusEnum.PUBLISHED.getCode());
        // 查找符合文章
        PageInfo pageInfo = this.findPageBreakByCondition(vo);
        // 不为空返回pageInfo中封装的列表
        return null == pageInfo ? null : pageInfo.getList();
    }

    /**
     * 随机文章
     *
     * @param pageSize 分页大小
     * @return 随机文章列表
     */
    @Override
    public List<Article> listRandom(int pageSize) {
        // 设置文章搜索条件
        ArticleConditionVO vo = new ArticleConditionVO();
        // 随机搜索
        vo.setRandom(true);
        // 已发布
        vo.setStatus(ArticleStatusEnum.PUBLISHED.getCode());
        // 设置分页大小
        vo.setPageSize(pageSize);
        // 查找符合文章
        PageInfo pageInfo = this.findPageBreakByCondition(vo);
        // 不为空则返回pageInfo中封装的列表
        return null == pageInfo ? null : pageInfo.getList();
    }

    /**
     * 获取热门文章
     *
     * @param pageSize 分页大小
     * @return 热门文章列表
     */
    @Override
    public List<Article> listHotArticle(int pageSize) {
        // 分页
        PageHelper.startPage(1, pageSize);
        // 获取热门文章列表
        List<BizArticle> entityList = bizArticleMapper.listHotArticle();

        // 列表为空
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }

        // 创建返回对象
        List<Article> list = new ArrayList<>();
        // 拷贝
        for (BizArticle entity : entityList) {
            list.add(new Article(entity));
        }
        return list;
    }

    /**
     * 根据某篇文章获取与该文章相关的文章<br>
     * 搜索同类型、同标签下的文章
     *
     * @param pageSize 分页大小
     * @param article 原型文章
     * @return 类似文章列表
     */
    @Override
    public List<Article> listRelatedArticle(int pageSize, Article article) {
        // 原型文章为空
        if (null == article) {
            // 随机选取文章
            return listRandom(pageSize);
        }
        // 设置文章搜索条件
        ArticleConditionVO vo = new ArticleConditionVO();
        // 已发布
        vo.setStatus(ArticleStatusEnum.PUBLISHED.getCode());
        // 获取文章相关tag
        List<BizTags> tags = article.getTags();
        // tag表不为空
        if (!CollectionUtils.isEmpty(tags)) {
            // 取出tagId
            List<Long> tagIds = new ArrayList<>();
            for (BizTags tag : tags) {
                tagIds.add(tag.getId());
            }
            // 将tagId加入vo
            vo.setTagIds(tagIds);
        }
        // 规定文章类型
        vo.setTypeId(article.getTypeId());
        // 设置分页大小
        vo.setPageSize(pageSize);
        // 按条件搜索文章
        PageInfo pageInfo = this.findPageBreakByCondition(vo);
        // 不为空则返回pageInfo中封装的列表
        return null == pageInfo ? null : pageInfo.getList();
    }

    /**
     * 获取上一篇和下一篇
     *
     * @param insertTime 当前文章创建时间
     * @return 存储两篇文章的Map对象
     */
    @Override
    public Map<String, Article> getPrevAndNextArticles(Date insertTime) {
        // insertTime为空则新建一个Date对象
        insertTime = null == insertTime ? new Date() : insertTime;
        // 获取前后两篇文章组成的列表
        List<BizArticle> entityList = bizArticleMapper.getPrevAndNextArticles(insertTime);
        // 为空则返回null
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        // 创建存储结果的Map对象
        Map<String, Article> resultMap = new HashMap<>();
        for (BizArticle entity : entityList) {
            // 用键值“prev”映射创建时间早于该文章的文章
            if (entity.getCreateTime().getTime() < insertTime.getTime()) {
                resultMap.put("prev", new Article(entity));
            // 用“next”映射后一篇文章
            } else {
                resultMap.put("next", new Article(entity));
            }
        }
        // 返回结果map
        return resultMap;
    }

    /**
     * 文章点赞
     *
     * @param id 点赞文章id
     */
    @Override
    @RedisCache(flush = true)
    public void doPraise(Long id) {
        // 获取发起请求的ip地址
        String ip = IpUtil.getRealIp(RequestHolder.getRequest());
        // 构建操作对应键值
        String key = ip + "_doPraise_" + id;
        // 获取缓存值
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        // redis中存有该操作key
        if (redisTemplate.hasKey(key)) {
            // 同一ip对同一文章一小时内不能多次点赞
            throw new ZhydArticleException("一个小时只能点赞一次哈，感谢支持~~");
        }
        // 从session中取出用户
        User user = SessionUtil.getUser();
        // 创建文章点赞对象
        BizArticleLove love = new BizArticleLove();
        // 用户不为空
        if (null != user) {
            // 设定点赞用户为当前用户
            love.setUserId(user.getId());
        }
        // 设置点赞目标文章
        love.setArticleId(id);
        // 设置用户ip
        love.setUserIp(IpUtil.getRealIp(RequestHolder.getRequest()));
        // 记录时间
        love.setLoveTime(new Date());
        love.setCreateTime(new Date());
        love.setUpdateTime(new Date());
        // 将点赞操作插入数据库
        bizArticleLoveMapper.insert(love);
        // 记录点赞操作
        operations.set(key, id, 1, TimeUnit.HOURS);
    }

    /**
     * 查询是否存在文章
     *
     * @param id 查询文章id
     * @return 存在返回true 否则返回false
     */
    @Override
    public boolean isExist(Long id) {
        // 查询对应id文章 返回数量
        Integer count = bizArticleMapper.isExist(id);
        // 返回值不为空且数量大于0返回true 否则返回false
        return count != null && count > 0;
    }

    /**
     * 发布文章
     *
     * @param article 待发布文章
     * @param tags 文章相关tag
     * @param file 封面图片文件
     * @return 发布结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publish(Article article, Long[] tags, MultipartFile file) {
        // tags参数中不含标签
        if (null == tags || tags.length <= 0) {
            throw new ZhydArticleException("请至少选择一个标签");
        }
        // file参数不为空
        if (null != file) {
            // 将图片上传到数据库
            FileUploader uploader = new GlobalFileUploader();
            VirtualFile virtualFile = uploader.upload(file, FileUploadType.COVER_IMAGE.getPath(), true);
            // 记录封面图片存储路径
            article.setCoverImage(virtualFile.getFilePath());
        }
        Long articleId = null;
        // article中存有id 属于更新
        if ((articleId = article.getId()) != null) {
            this.updateSelective(article);
        // article中没有id 属于新发布文章
        } else {
            // 记录发布者
            article.setUserId(SessionUtil.getUser().getId());
            // 上传新文章 获取分配到的文章id
            articleId = this.insert(article).getId();
        }
        if (articleId != null) {
            // 删除该文章id与标签关联的旧数据
            articleTagsService.removeByArticleId(articleId);
            // 新建文章-标签关联数据
            articleTagsService.insertList(tags, articleId);
        }
        // 发布成功
        return true;
    }

    /**
     * 修改置顶、推荐
     *
     * @param type 操作类型
     * @param id 文章id
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTopOrRecommendedById(String type, Long id) {
        // 按id查找到文章
        BizArticle article = bizArticleMapper.selectByPrimaryKey(id);
        article.setId(id);
        // 置顶操作
        if ("top".equals(type)) {
            // 取反
            article.setTop(!article.getTop());
        // 推荐操作
        } else if ("recommend".equals(type)) {
            // 取反
            article.setRecommended(!article.getRecommended());
        // 评论操作
        } else if ("comment".equals(type)) {
            // 取反
            article.setComment(!article.getComment());
        // 不明操作
        } else {
            throw new ZhydException(ResponseStatus.INVALID_PARAMS.getMessage());
        }
        // 更新修改时间
        article.setUpdateTime(new Date());
        // 上传更新结果
        return bizArticleMapper.updateByPrimaryKeySelective(article) > 0;
    }

    /**
     * 批量修改status
     *
     * @param ids 待修改id表
     * @param status 修改值
     */
    @Override
    public void batchUpdateStatus(Long[] ids, boolean status) {
        // 待修改列表为空
        if (ids == null || ids.length <= 0) {
            return;
        }
        // 批量修改表中文章status
        bizArticleMapper.batchUpdateStatus(Arrays.asList(ids), status);
    }

    /**
     * 上传新文章
     *
     * @param entity 待上传文章
     * @return 更新信息后的entity
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Article insert(Article entity) {
        Assert.notNull(entity, "Article不可为空！");
        // 记录更新时间
        entity.setUpdateTime(new Date());
        // 记录创建时间
        entity.setCreateTime(new Date());
        // 文章是否为原创
        entity.setOriginal(entity.isOriginal());
        // 文章是否是评论
        entity.setComment(entity.isComment());
        // 上传文章
        bizArticleMapper.insertSelective(entity.getBizArticle());
        return entity;
    }

    /**
     * 根据主键删除文章
     *
     * @param primaryKey 主键
     * @return 删除结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByPrimaryKey(Long primaryKey) {
        // 从数据库中删除 记录结果
        boolean result = bizArticleMapper.deleteByPrimaryKey(primaryKey) > 0;
        // 删除标签记录
        // 用Example类来实现sql语句的条件设置
        Example tagsExample = new Example(BizArticleTags.class);
        Example.Criteria tagsCriteria = tagsExample.createCriteria();
        // where articleId = 'primaryKdy'
        tagsCriteria.andEqualTo("articleId", primaryKey);
        // 从数据库中删除对应实体
        bizArticleTagsMapper.deleteByExample(tagsExample);

        // 删除查看记录
        // 用Example类来实现sql语句的条件设置
        Example lookExample = new Example(BizArticleLook.class);
        Example.Criteria lookCriteria = lookExample.createCriteria();
        // where articleId = 'primaryKdy'
        lookCriteria.andEqualTo("articleId", primaryKey);
        // 从数据库中删除对应实体
        bizArticleLookMapper.deleteByExample(lookExample);

        // 删除赞记录
        // 用Example类来实现sql语句的条件设置
        Example loveExample = new Example(BizArticleLove.class);
        Example.Criteria loveCriteria = loveExample.createCriteria();
        // where articleId = 'primaryKdy'
        loveCriteria.andEqualTo("articleId", primaryKey);
        // 从数据库中删除对应实体
        bizArticleLoveMapper.deleteByExample(loveExample);
        // 返回结果
        return result;
    }

    /**
     * 更新已发布文章
     *
     * @param entity 需更新文章
     * @return 更新结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSelective(Article entity) {
        // entity不可为空
        Assert.notNull(entity, "Article不可为空！");
        // 记录更新时间
        entity.setUpdateTime(new Date());
        // 记录文章是否为原创
        entity.setOriginal(entity.isOriginal());
        // 判断文章是否为评论
        entity.setComment(entity.isComment());
        // 上传文章
        return bizArticleMapper.updateByPrimaryKeySelective(entity.getBizArticle()) > 0;
    }

    /**
     * 按主键查找文章
     *
     * @param primaryKey 主键
     * @return 文章
     */
    @Override
    public Article getByPrimaryKey(Long primaryKey) {
        // 主键参数不可为空
        Assert.notNull(primaryKey, "PrimaryKey不可为空！");
        // 按主键查找文章实体
        BizArticle entity = bizArticleMapper.get(primaryKey);
        // 没有对应文章
        if (null == entity) {
            return null;
        }
        // 查询并更新相关信息
        this.subquery(entity);
        // 封装对象并返回
        return new Article(entity);
    }

    /**
     * 查询并更新文章相关信息
     *
     * @param entity 待查询文章
     */
    private void subquery(BizArticle entity) {
        // 以Id为主键
        Long primaryKey = entity.getId();

        // 查看的次数
        // 生成查找参数
        BizArticleLook look = new BizArticleLook();
        look.setArticleId(primaryKey);
        // 更新文章被查看次数
        entity.setLookCount(bizArticleLookMapper.selectCount(look));

        // 评论数
        // 生成查找参数
        Example example = new Example(BizComment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sid", primaryKey);
        criteria.andEqualTo("status", CommentStatusEnum.APPROVED.toString());
        // 更新文章评论数
        entity.setCommentCount(commentMapper.selectCountByExample(example));

        // 喜欢的次数
        // 生成查找参数
        BizArticleLove love = new BizArticleLove();
        love.setArticleId(primaryKey);
        // 更细文章喜欢次数
        entity.setLoveCount(bizArticleLoveMapper.selectCount(love));
    }

    /**
     * 列出所有文章
     *
     * @return 文章列表
     */
    @Override
    public List<Article> listAll() {
        // 取出所有文章
        List<BizArticle> entityList = bizArticleMapper.selectAll();

        // 列表为空
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }

        // 创建返回对象
        List<Article> list = new ArrayList<>();
        // 拷贝数据
        for (BizArticle entity : entityList) {
            list.add(new Article(entity));
        }
        return list;
    }
}
