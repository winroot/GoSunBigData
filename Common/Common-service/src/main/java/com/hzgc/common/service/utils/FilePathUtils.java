package com.hzgc.common.service.utils;

/**
 * 文件路径工具类
 *
 * @author liuzhikun
 */
public class FilePathUtils {
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    // public static final String FILE_SEPARATOR = File.separator;

    public static String getFilePath(String path) {
        return path.replace("/", FILE_SEPARATOR).replace("\\", FILE_SEPARATOR);
    }

    public static String getHttpURLPath(String path) {
        return path.replace("\\", "/");
    }

    public static boolean isAbsolutePath(String path) {
        if (path.startsWith("/") || path.indexOf(":") > 0) {
            return true;
        }
        return false;
    }
}
