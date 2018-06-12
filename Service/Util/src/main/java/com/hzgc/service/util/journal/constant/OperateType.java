package com.hzgc.service.util.journal.constant;

/**
 * 操作类型
 *
 * @author riqongyu
 */
public interface OperateType {
    /**
     * 登陆
     */
    int USER_LOGIN = 1;
    /**
     * 登出
     */
    int USER_LOGOUT = 2;
    /**
     * 启动业务
     */
    int USER_START = 3;
    /**
     * 停止业务
     */
    int USER_STOP = 4;
    /**
     * 新增配置
     */
    int CONFIG_ADD = 5;
    /**
     * 修改配置
     */
    int CONFIG_MOD = 6;
    /**
     * 删除配置
     */
    int CONFIG_DEL = 7;
    /**
     * 查询配置
     */
    int CONFIG_QUERY = 8;
    /**
     * 恢复配置
     */
    int CONFIG_RECOVERY = 9;
    /**
     * 文件备份
     */
    int BACKUP = 10;
    /**
     * 文件下载
     */
    int DOWNLOAD = 11;
}