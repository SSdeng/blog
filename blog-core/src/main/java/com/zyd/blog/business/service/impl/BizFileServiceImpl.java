package com.zyd.blog.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyd.blog.business.entity.File;
import com.zyd.blog.business.enums.FileUploadType;
import com.zyd.blog.business.service.BizFileService;
import com.zyd.blog.business.vo.FileConditionVO;
import com.zyd.blog.file.FileUploader;
import com.zyd.blog.file.exception.GlobalFileException;
import com.zyd.blog.persistence.beans.BizFile;
import com.zyd.blog.persistence.mapper.BizFileMapper;
import com.zyd.blog.plugin.file.GlobalFileUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author innodev java team
 * @version 1.0
 * @date 2018/12/14 09:23
 * @since 1.8
 */
@Service
public class BizFileServiceImpl implements BizFileService {

    @Autowired
    private BizFileMapper shopFileMapper;

    /**
     * 分页查询
     *
     * @param vo 文件搜索条件
     * @return 符合条件文件列表
     */
    @Override
    public PageInfo<File> findPageBreakByCondition(FileConditionVO vo) {
        // 按vo属性值分页
        PageHelper.startPage(vo.getPageNumber(), vo.getPageSize());
        // 按vo设定条件搜索
        List<BizFile> list = shopFileMapper.findPageBreakByCondition(vo);
        // 转换元素类型
        List<File> boList = getFiles(list);
        // 空表
        if (boList == null) {
            return null;
        }
        // 封装list到pageInfo对象实现分页
        PageInfo bean = new PageInfo<BizFile>(list);
        // 将boList放入pageInfo
        bean.setList(boList);
        return bean;
    }

    /**
     * List<BizFile> -> List<File>
     *
     * @param list 原list
     * @return 新list
     */
    private List<File> getFiles(List<BizFile> list) {
        // 原list为空
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        // 构建新list
        List<File> boList = new ArrayList<>();
        // 拷贝 转换元素类型
        for (BizFile bizFile : list) {
            boList.add(new File(bizFile));
        }
        // 返回新list
        return boList;
    }

    /**
     * 按照路径和类型从数据库取出文件
     *
     * @param filePath 文件路径
     * @param uploadType 类型
     * @return 查找到的第一个文件
     */
    @Override
    public File selectFileByPathAndUploadType(String filePath, String uploadType) {
        // 路径为空
        if (StringUtils.isEmpty(filePath)) {
            return null;
        }
        BizFile file = new BizFile();
        // 设置路径
        file.setFilePath(filePath);
        // TODO
        // 错误代码？
        if (StringUtils.isEmpty(uploadType)) {
            // 记录文件类型
            file.setUploadType(uploadType);
        }
        // 从数据库取出文件
        List<BizFile> fileList = shopFileMapper.select(file);
        // 结果不为空则返回表中第一个文件
        return CollectionUtils.isEmpty(fileList) ? null : new File(fileList.get(0));
    }

    /**
     * 批量删除文件
     *
     * @param ids 删除文件id表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(Long[] ids) {
        // 遍历
        for (Long id : ids) {
            // 按主键获取文件
            File oldFile = this.getByPrimaryKey(id);
            // 按主键删除
            this.removeByPrimaryKey(id);
            try {
                // 从上传文件中删除
                FileUploader uploader = new GlobalFileUploader();
                uploader.delete(oldFile.getFilePath(), oldFile.getUploadType());
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * 上传文件
     *
     * @param file 文件组
     * @return 上传数量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int upload(MultipartFile[] file) {
        // 文件组参数为空
        if (null == file || file.length == 0) {
            throw new GlobalFileException("请至少选择一张图片！");
        }
        // 遍历
        for (MultipartFile multipartFile : file) {
            // 上传文件
            FileUploader uploader = new GlobalFileUploader();
            uploader.upload(multipartFile, FileUploadType.COMMON.getPath(), true);
        }
        // 上传数量
        return file.length;
    }

    /**
     * 将文件插入到数据库
     *
     * @param entity 文件实体
     * @return 更新后实体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public File insert(File entity) {
        // 实体不可为空
        Assert.notNull(entity, "Invalid parameter");
        // 记录创建时间
        entity.setCreateTime(new Date());
        // 记录更新时间
        entity.setUpdateTime(new Date());
        // 插入到数据库
        shopFileMapper.insertSelective(entity.getBizFile());
        // 更新后实体
        return entity;
    }

    /**
     * 按主键删除文件
     *
     * @param primaryKey 主键
     * @return 删除结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByPrimaryKey(Long primaryKey) {
        // 主键参数不可为空
        Assert.notNull(primaryKey, "Invalid parameter");
        // 返回删除结果
        return shopFileMapper.deleteByPrimaryKey(primaryKey) > 0;
    }

    /**
     * 选择更新
     * 空值会用默认值填充
     *
     * @param entity 实体
     * @return 更新结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSelective(File entity) {
        // 参数不可为空
        Assert.notNull(entity, "Invalid parameter");
        // 记录更新时间
        entity.setUpdateTime(new Date());
        // 操作数据库更新 返回结果
        return shopFileMapper.updateByPrimaryKeySelective(entity.getBizFile()) > 0;
    }

    /**
     * 按主键获取文件
     *
     * @param primaryKey 文件主键
     * @return 文件
     */
    @Override
    public File getByPrimaryKey(Long primaryKey) {
        // 主键参数不可为空
        Assert.notNull(primaryKey, "Invalid parameter");
        // 从数据库按主键获取文件
        BizFile entity = shopFileMapper.selectByPrimaryKey(primaryKey);
        // 转换File类对象并返回
        return new File(entity);
    }

    /**
     * 获取所有文件
     *
     * @return 文件表
     */
    @Override
    public List<File> listAll() {
        // 获取数据库中所有文件
        List<BizFile> list = shopFileMapper.selectAll();
        // 转换元素类型后返回
        return getFiles(list);
    }
}
