package com.hzgc.common.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtil {
    /**
     * 根据文件绝对路径获取文件
     *
     * @param filePath 文件绝对路径
     * @return 文件
     */
    public static byte[] getFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fis.read(bytes);
            return bytes;
        } catch (IOException e) {
            return null;
        }
    }

}
