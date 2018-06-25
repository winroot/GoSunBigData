package com.hzgc.service.util.rest;

import com.hzgc.service.util.auth.annotation.AuthorizeCode;

public class BigDataPermission {

    /**
     * DynRepo模块权限颗粒
     */
    @AuthorizeCode(id = 2301, name = "查询", menu = "23", description = "人脸查询")
    public static final String FACE_SEARCH = "dyn_repo/menu/face_search";
    @AuthorizeCode(id = 6401, name = "查询", menu = "67", description = "抓拍历史查询")
    public static final String HISTORY_FACE_SEARCH = "dyn_repo/menu/history_face_search";
    /**
     * StaRepo模块权限颗粒
     */
    @AuthorizeCode(id = 5201, name = "查看", menu = "52", description = "人脸查看")
    public static final String OBJECT_VIEW = "sta_repo/object/object_view";
    @AuthorizeCode(id = 5202, name = "操作", menu = "52", description = "人脸操作")
    public static final String OBJECT_OPERATION = "sta_repo/object/object_operation";

    /**
     * Dispatch模块权限颗粒
     */
    @AuthorizeCode(id = 2201, name = "查看", menu = "22", description = "识别规则查看")
    public static final String RULE_VIEW = "dispatch/menu/rule_view";
    @AuthorizeCode(id = 2202, name = "操作", menu = "22", description = "识别规则操作")
    public static final String RULE_OPERATION = "dispatch/menu/rule_operation";


}
