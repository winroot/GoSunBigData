package com.hzgc.common.service.utils;

/**
 * 数据库模糊查询字段组装工具类
 *
 * @author liuzhikun
 */
public class FuzzyQueryUtil {
    /**
     * 获取模糊查询字符串
     *
     * @param fuzzyValue 模糊值
     * @return
     */
    public static String getGeneralLikeStr(String fuzzyValue) {
        return "%" + fuzzyValue + "%";
    }
}
