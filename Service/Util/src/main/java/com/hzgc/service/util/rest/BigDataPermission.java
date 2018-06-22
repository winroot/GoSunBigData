package com.hzgc.service.util.rest;

import com.hzgc.service.util.auth.annotation.AuthorizeCode;

public class BigDataPermission {

    /**
     * DynRepo模块权限颗粒
     */
    @AuthorizeCode(name = "查询", menu = "23", description = "人脸查询")
    public static final String FACE_SEARCH = "menu/face_search";
    @AuthorizeCode(name = "查询", menu = "67", description = "抓拍历史查询")
    public static final String HISTORY_FACE_SEARCH = "menu/history_face_search";
    /**
     * StaRepo模块权限颗粒
     */
    @AuthorizeCode(name = "查看", menu = "52", description = "人脸查看")
    public static final String OBJECT_VIEW = "object/object_view";
    @AuthorizeCode(name = "操作", menu = "52", description = "人脸操作")
    public static final String OBJECT_OPERATION = "object/object_operation";

    /**
     * Dispatch模块权限颗粒
     */
    @AuthorizeCode(name = "查看", menu = "22", description = "识别规则查看")
    public static final String RULE_VIEW = "menu/rule_view";
    @AuthorizeCode(name = "操作", menu = "22", description = "识别规则操作")
    public static final String RULE_OPERATION = "menu/rule_operation";

}
