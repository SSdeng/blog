package com.zyd.blog.file.util;

import com.zyd.blog.file.entity.VirtualFile;
import com.zyd.blog.file.exception.GlobalFileException;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;

/**
 * 操作图片工具类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/18 11:48
 * @since 1.0
 */
public class ImageUtil {

    /**
     *  异常信息
     */
    private static final String ERROR = "获取图片信息发生异常！";

    /**
     * 隐藏工具类的public构造方法
     */
    private ImageUtil(){
        throw new IllegalStateException("Utility class");
    }

    /**
     * 通过File对象获取图片信息
     *
     * @param file File对象
     * @throws IOException
     */
    public static VirtualFile getInfo(File file) {
        if (null == file) {
            return new VirtualFile();
        }
        try {
            return getInfo(new FileInputStream(file))
                    .setSize(file.length())
                    .setOriginalFileName(file.getName())
                    .setSuffix(BlogFileUtil.getSuffix(file.getName()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalFileException(ERROR, e);
        }
    }

    /**
     * 根据MultipartFile对象获取图片信息
     *
     * @param multipartFile MultipartFile对象
     * @throws IOException
     */
    public static VirtualFile getInfo(MultipartFile multipartFile) {
        if (null == multipartFile) {
            return new VirtualFile();
        }
        try {
            return getInfo(multipartFile.getInputStream())
                    .setSize(multipartFile.getSize())
                    .setOriginalFileName(multipartFile.getOriginalFilename())
                    .setSuffix(BlogFileUtil.getSuffix(multipartFile.getOriginalFilename()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalFileException(ERROR, e);
        }
    }

    /**
     * 根据输入流获取图片信息
     *
     * @param inputStream 要提取的文件输入流
     * @throws IOException
     */
    public static VirtualFile getInfo(InputStream inputStream) {
        try (BufferedInputStream in = new BufferedInputStream(inputStream)) {
            //字节流转图片对象
            Image bi = ImageIO.read(in);
            if (null == bi) {
                return new VirtualFile();
            }
            //获取默认图像的高度，宽度
            return new VirtualFile()
                    .setWidth(bi.getWidth(null))
                    .setHeight(bi.getHeight(null))
                    .setSize(inputStream.available());
        } catch (Exception e) {
            throw new GlobalFileException(ERROR, e);
        }
    }
}
