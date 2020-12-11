package com.zyd.blog.business.service;


import com.github.pagehelper.PageInfo;
import com.zyd.blog.business.entity.File;
import com.zyd.blog.business.vo.FileConditionVO;
import com.zyd.blog.framework.object.AbstractService;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author innodev java team
 * @version 1.0
 * @date 2018/12/14 09:23
 * @since 1.8
 */
public interface BizFileService extends AbstractService<File, Long> {

    /**
     * 分页查询
     *
     * @param vo 文件搜索条件
     * @return 符合条件文件列表
     */
    PageInfo<File> findPageBreakByCondition(FileConditionVO vo);

    /**
     * 按照路径和类型从数据库取出文件
     *
     * @param filePath 文件路径
     * @param uploadType 类型
     * @return 查找到的第一个文件
     */
    File selectFileByPathAndUploadType(String filePath, String uploadType);

    /**
     * 批量删除文件
     *
     * @param ids 删除文件id表
     */
    void remove(Long[] ids);

    /**
     * 上传文件
     *
     * @param file 文件组
     * @return 上传数量
     */
    int upload(MultipartFile[] file);
}
