package com.hzgc.service.util.journal.constant;

/**
 * 业务类型
 *
 * @author qirongyu
 */
public interface ServiceType {
    /**
     * 登录
     */
    int LOGIN = 1;
    /**
     * 用户配置
     */
    int CONFIG_USER = 2;
    /**
     * 角色配置
     */
    int CONFIG_ROLE = 3;
    /**
     * 设备配置
     */
    int CONFIG_DEV = 4;
    /**
     * 部门配置
     */
    int CONFIG_RES = 5;
    /**
     * 云台配置
     */
    int CONFIG_PTZ = 6;
    /**
     * 告警联动
     */
    int CONFIG_LINKAGE = 7;
    /**
     * 告警确认
     */
    int ALARM_CONFIRM = 8;
    /**
     * 开关量
     */
    int BOOLEAN = 9;
    /**
     * 云台锁定
     */
    int PTZ_LOCK = 10;
    /**
     * 云台释放
     */
    int PTZ_UNLOCK = 11;
    /**
     * 回放
     */
    int SERVICE_VOD = 12;
    /**
     * 实时监控
     */
    int MONITOR = 13;
    /**
     * 日志
     */
    int LOGOUT = 14;
    /**
     * 数据库
     */
    int BACK_UP = 15;
    /**
     * 系统控制
     */
    int SYSTEM_CONFIG = 16;
    /**
     * 警情处理
     */
    int ALERT_PROCESS = 17;
    /**
     * 保安管理
     */
    int GUARD_MANAGE = 18;
    /**
     * 警情统计
     */
    int ALERTS_STATISTICS = 19;
    /**
     * 警情报表导出
     */
    int ALERTS_TABLE_EXPORT = 20;
    /**
     * 名单管理
     */
    int LIST_ADMINISTRATION = 21;
    /**
     * 名单组管理
     */
    int LIST_GROUP_ADMINISTRATION = 22;
    /**
     * 人脸对比误报
     */
    int LIST_FILTER = 23;
    /**
     * 人脸系统设置
     */
    int FACE_SERVER = 24;
    /**
     * 名单加入
     */
    int ADD_LIST = 25;
    /**
     * 名单删除
     */
    int DEL_LIST = 26;
    /**
     * 名单更新
     */
    int UPD_LIST = 27;
    /**
     * 新增名单组
     */
    int ADD_LIST_GROUP = 28;
    /**
     * 删除名单组
     */
    int DEL_LIST_GROUP = 29;
    /**
     * 更新名单组
     */
    int UPD_LIST_GROUP = 30;
    /**
     * 名单加入名单组
     */
    int ADD_GROUP_LIST = 31;
    /**
     * 名单从名单组中删除
     */
    int DEL_GROUP_LIST = 32;
    /**
     * 社区与名单组关联
     */
    int ADD_DEPARTMENT_LISTGROUP = 33;
    /**
     * 误报增加
     */
    int ADD_LIST_FILTER = 34;
    /**
     * 误报恢复
     */
    int DEL_LIST_FILTER = 35;
    /**
     * 云台控制
     */
    int PTZ_CONTROL = 36;
    /**
     * 临时权限
     */
    int TEMP_AUTH = 37;
    /**
     * 对象管理
     */
    int OBJECT_ADMIN = 38;
    /**
     * 对象类型管理
     */
    int OBJECT_TYPE_ADMIN = 39;
    /**
     * 告警订阅
     */
    int ALARM_SUBSCRIBE = 40;
    /**
     * 盒子管理
     */
    int BOX_MANAGE = 41;
    /**
     * 预案管理
     */
    int PLAN_MANAGE = 42;
    /**
     * 录像下载记录
     */
    int DOWNLOAD_HISTORY = 43;
    /**
     * 地图管理
     */
    int MAP_MANAGE=44;
}
