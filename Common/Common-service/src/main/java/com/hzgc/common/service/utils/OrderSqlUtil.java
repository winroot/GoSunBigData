package com.hzgc.common.service.utils;

/**
 * sql排序工具类
 *
 * @author liuzk
 */
public class OrderSqlUtil {
    /**
     * 根据排序string来生成sql排序
     *
     * @param sort 排序字符串，多个排序字段用逗号隔开。默认升序，'-'表示降序，如id,-name
     * @return sql排序语句
     */
    public static String getOrderSqlStringBySort(String sort) {
        if (null == sort || "".equals(sort)) {
            return null;
        }

        StringBuffer orderSqlString = new StringBuffer();
        String[] orderStringList = sort.split(",");
        for (String s : orderStringList) {
            char orderTypeChar = s.charAt(0);
            // 降序
            if ("-".toCharArray()[0] == orderTypeChar) {
                orderSqlString.append(s.substring(1));
                orderSqlString.append(" ");
                orderSqlString.append("desc");
            }
            // 升序
            else {
                orderSqlString.append(s);
                orderSqlString.append(" ");
                orderSqlString.append("asc");
            }
            if (!s.equals(orderStringList[orderStringList.length - 1])) {
                orderSqlString.append(",");
            }
        }
        return orderSqlString.toString();
    }
}
