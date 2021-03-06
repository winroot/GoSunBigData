package com.hzgc.service.starepo.util;

import com.hzgc.common.util.file.FileUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Map;

@Slf4j
public class DocHandlerUtil {
    private static Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
    private final static String exportDir = "tmp" + File.separator;

    static {
        configuration.setDefaultEncoding("utf-8");
    }

    public static byte[] createDoc(Map<String, Object> dataMap, String fileName){
        configuration.setClassLoaderForTemplateLoading(DocHandlerUtil.class.getClassLoader(), "");

        Template t = null;
        try {
            t = configuration.getTemplate("important_people.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 输出文档路径及名称
        File outFile = new File(exportDir + fileName);
        if(!outFile.isFile()){
            try {
                new File(exportDir).mkdir();
                outFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Writer out = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outFile);
            OutputStreamWriter oWriter = new OutputStreamWriter(fos, "UTF-8");
            out = new BufferedWriter(oWriter);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
        try {
            if (t != null) {
                t.process(dataMap, out);
            }
            if (out != null) {
                out.close();
            }
            if (fos != null) {
                fos.close();
            }
        } catch (TemplateException | IOException e) {
            log.error(e.getMessage());
        }
        return FileUtil.fileToByteArray(exportDir + File.separator + fileName);
    }
}