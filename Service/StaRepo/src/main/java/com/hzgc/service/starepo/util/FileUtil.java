package com.hzgc.service.starepo.util;

import org.apache.log4j.Logger;

import java.io.File;
import java.net.URL;

public class FileUtil {
    private static Logger LOG = Logger.getLogger(FileUtil.class);

    public static File loadResourceFile(String resourceName) {
        if (resourceName != null && resourceName.length() != 0) {
            URL url = ClassLoader.getSystemResource(resourceName);
            if (url != null) {
                File file = new File(url.getPath());
                return file;
            } else {
                LOG.error("Resource file:" +
                        ClassLoader.getSystemResource("") + resourceName + " is not exist!");
                return null;
            }
        } else {
            LOG.error("The file name is not vaild!");
            return null;
        }

    }
}
