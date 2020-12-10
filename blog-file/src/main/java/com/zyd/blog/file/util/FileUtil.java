package com.zyd.blog.file.util;

import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Arrays;

/**
 * 文件操作工具类
 *
 * @author yadong.zhang email:yadong.zhang0415(a)gmail.com
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/01/09 17:40
 * @since 1.0
 */
public class FileUtil extends cn.hutool.core.io.FileUtil {
    private static final String[] PICTURE_SUFFIXS = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".svg"};

    /**
     * 删除目录，返回删除的文件数
     *
     * @param rootPath 待删除的目录
     * @param fileNum  已删除的文件个数
     * @return 已删除的文件个数
     */
    public static int deleteFiles(String rootPath, int fileNum) {
        File file = new File(rootPath);
        if (!file.exists()) {
            return -1;
        }
        if (file.isDirectory()) {
            File[] sonFiles = file.listFiles();
            if (sonFiles != null && sonFiles.length > 0) {
                for (File sonFile : sonFiles) {
                    if (sonFile.isDirectory()) {
                        fileNum = deleteFiles(sonFile.getAbsolutePath(), fileNum);//递归删除子目录
                    } else {
                        sonFile.delete();
                        fileNum++;
                    }
                }
            }
        } else {
            file.delete();
        }
        return fileNum;
    }

    /**
     * 获取文件前缀
     *
     * @param file  File对象
     * @return      文件名前缀
     */
    public static String getPrefix(File file) {
        return getPrefix(file.getName());
    }

    /**
     * 获取文件前缀
     *
     * @param fileName  带有路径的文件名
     * @return          文件名前缀
     */
    public static String getPrefix(String fileName) {
        int idx = fileName.lastIndexOf(".");
        int xie = fileName.lastIndexOf("/");
        idx = idx == -1 ? fileName.length() : idx;
        xie = xie == -1 ? 0 : xie + 1;
        return fileName.substring(xie, idx);
    }

    /**
     * 获取文件后缀
     *
     * @param file  File对象
     * @return      文件名后缀
     */
    public static String getSuffix(File file) {
        return getSuffix(file.getName());
    }

    /**
     * 获取文件后缀
     *
     * @param fileName  带有路径的文件名
     * @return          文件名后缀
     */
    public static String getSuffix(String fileName) {
        int index = fileName.lastIndexOf(".");
        index = -1 == index ? fileName.length() : index;
        return fileName.substring(index);
    }

    /**
     * 通过url获取文件名后缀，空文件返回".png"
     *
     * @param imgUrl    文件url
     * @return          文件名后缀
     */
    public static String getSuffixByUrl(String imgUrl) {
        String defaultSuffix = ".png";
        if (StringUtils.isEmpty(imgUrl)) {
            return defaultSuffix;
        }
        String fileName = imgUrl;
        if(imgUrl.contains("/")) {
            fileName = imgUrl.substring(imgUrl.lastIndexOf("/"));
        }
        String fileSuffix = getSuffix(fileName);
        return StringUtils.isEmpty(fileSuffix) ? defaultSuffix : fileSuffix;
    }

    /**
     * 生成临时文件名
     *
     * @param imgUrl
     * @return
     */
    public static String generateTempFileName(String imgUrl) {
        return "temp" + getSuffixByUrl(imgUrl);
    }

    /**
     * 根据后缀判断是否为图片
     *
     * @param suffix 后缀名
     * @return
     */
    public static boolean isPicture(String suffix) {
        return !StringUtils.isEmpty(suffix) && Arrays.asList(PICTURE_SUFFIXS).contains(suffix.toLowerCase());
    }

    /**
     * 根据文件路径创建目录
     *
     * @param filePath 文件路径
     */
    public static void mkdirs(String filePath) {
        File file = new File(filePath);
        mkdirs(file);
    }

    /**
     * 根据文件对象创建目录
     *
     * @param file 文件对象
     */
    public static void mkdirs(File file) {
        if (!file.exists()) {
            if (file.isDirectory()) {
                file.mkdirs();
            } else {
                file.getParentFile().mkdirs();
            }
        }
    }

    /**
     * 检查文件路径确保有效
     *
     * @param realFilePath 文件路径
     */
    public static void checkFilePath(String realFilePath) {
        if (StringUtils.isEmpty(realFilePath)) {
            return;
        }
        File parentDir = new File(realFilePath).getParentFile();
        //目标路径上级目录不存在时创建该目录
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
    }
}
