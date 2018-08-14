package com.hzgc.collect.expand.parser;

public interface Parser {
    // 判断FTP当前上传文件是否需要解析
    boolean canParse(String path);
    // FTP上传路径解析
    FtpPathMetaData parse(String path);
    // 小图ftpUrl转大图ftpUrl
    String surlToBurl(String surl);
}
