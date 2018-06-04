package com.hzgc.service.starepo.service;

public interface ConfigConstants {

    /**
     * 忽略标识
     */
    String IGNORE_YES = "yes";
    String IGNORE_NO = "no";


    /**
     * errorCoed
     */
    // 2001 = 没有传入照片信息
    Integer EMPTY_IMAGE = 2001;
    // 2002 = 特征值取得失败!
    Integer GET_FEATURE_ERR = 2002;
    // 2003 = 插入数据失败
    Integer ADD_DATA_ERR = 2003;
    // 2004 = 更新数据失败
    Integer UPD_DATA_ERR = 2004;
    // 2005 = 删除数据失败
    Integer DEL_DATA_ERR = 2005;
    // 2006 = 搜索数据失败
    Integer GET_DATA_ERR = 2006;
    // 2007 = 对象插入时，名单类型被删除，请刷新页面
    Integer OBJECT_NOT_EXIST = 2007;
    // 2011 = 身份证ID格式不对
    Integer IDCOED_ERR = 2011;
    // 2012 = 身份证ID格式不对
    Integer SIMI_ERR = 2012;
    // 2013 = 请重新导入照片，离上传导入时间过长
    Integer FEATURE_NOT_EXISTS = 2013;
    // 2014 = 不存在特征值，请导入照片！
    Integer FEATUREKEY_NOT_EXISTS = 2014;
    // 2101 = 此对象类型存在布控，请将其布控计划删除后在进行此操作
    Integer EXISTS_RECOGNIZE = 2101;
    // 2111 = 对象类型名字长度不能超过36
    Integer OBJ_TYPE_NAME_LEN_ERR = 2111;
    // 2112 = 对象类型备注长度不能超过36
    Integer OBJ_TYPE_REMARK_LEN_ERR = 2112;
    // 2113 = 对象类型布控人长度不能超过36
    Integer OBJ_TYPE_CREATOR_LEN_ERR = 2113;
    // 2114 = 对象类型插入失败，可能为对象类型ID过长，请联系厂家
    Integer OBJ_TYPE_ID_TOO_LONG = 2114;
    // 2115 = 数据库中对象类型为空，请先创建！
    Integer OBJ_TYPE_NOT_EXISTS = 2115;
    // 2116 = 对象类型中存在对象，不能删除对象类型！
    Integer OBJECT_EXISTS = 2116;

    // 2140 = 大数据接口调用失败
    Integer ERROR_BIG_RESULT = 2140;
    // 2141 = 参数错误
    Integer ERROR_PARAMS = 2141;
    // 2142 = 空参数
    Integer EMPTY_PARAM = 2142;
    // 2143 = 大数据返回结果为空
    Integer EMPTY_BIG_RESULT = 2143;

    // 2081 = 大数据没有启动，接口不存在
    Integer BIGDATA_API_NOT_EXISTS = 2081;
    /**
     * 数据库数据errorCode
     */
    // 2091 = 增加数据重复!
    Integer ADD_DATA_EXISTS = 2091;
    // 2092 = 删除数据不存在!
    Integer DEL_DATA_NOT_EXISTS = 2092;
    // 2093 = 更新数据不存在!
    Integer UPD_DATA_NOT_EXISTS = 2093;
    // 2094 = 数据不能为空!
    Integer EMPTY_DATA = 2094;
    // 2095 = 更新的数据表中已经存在!
    Integer UPD_DATA_EXISTS = 2095;
    // 2096 = 数据库错误!
    Integer DATA_ERR = 2096;


    /**
     * 对象导出
     */
    Integer PAGE_COUNT = 200;

    String PEOPLE_DATA_KEY = "peopleListKey";
    String INDEX_KEY = "index";
    String INDEX_KEY_DATA = "序号";
    String INDEX_DATA_KEY = "indexData";
    String TIME_KEY = "time";
    String TIME_KEY_DATA = "时间";
    String TIME_DATA_KEY = "timeData";
    String TYPE_KEY = "type";
    String TYPE_KEY_DATA = "类型";
    String TYPE_DATA_KEY = "typeData";
    String NAME_KEY = "name";
    String NAME_KEY_DATA = "名字";
    String NAME_DATA_KEY = "nameData";
    String SEX_KEY = "sex";
    String SEX_KEY_DATA = "性别";
    String SEX_DATA_KEY = "sexData";
    String SIMILARITY_KEY = "similarity";
    String SIMILARITY_KEY_DATA = "相似度";
    String SIMILARITY_DATA_KEY = "similarityData";
    String CHARGE_KEY = "charge";
    String CHARGE_KEY_DATA = "布控人员";
    String CHARGE_DATA_KEY = "chargeData";
    String TELEPHNOE_KEY = "telephone";
    String TELEPHNOE_KEY_DATA = "联系方式";
    String TELEPHONE_DATA_KEY = "telephoneData";
    String ID_KEY = "ID";
    String ID_KEY_DATA = "身份证";
    String IDDATA_KEY = "IDData";
    String REASON_KEY = "reason";
    String REASON_KEY_DATA = "布控原因";
    String REASON_DATA_KEY = "reasonData";
    String PICTURE_KEY = "picture";
    String PICTURE_INDEX_KEY = "pictureIndex";
    String NO_DATA = "";

    String EXPORT_FILE_NAME = "people.doc";

    String IMPORTANT_PEOPLE_TEMPLATE = "com/hzgc/service/starepo/util/important_people.xml";

    String IMPORTANT_PEOPLE_FILE_NAME = "people.doc";
    String IMPORTANT_PEOPLE_ZIP_NAME = "people.zip";
}
