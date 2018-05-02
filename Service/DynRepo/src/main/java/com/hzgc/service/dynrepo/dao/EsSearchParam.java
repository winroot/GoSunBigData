package com.hzgc.service.dynrepo.dao;

import java.io.Serializable;

public class EsSearchParam implements Serializable {
    public static final String DESC = "DESC";
    public static final String ASC = "ASC";
    public static final String OR = "OR";
    public static final String STANDARD = "standard";
    public static final String TIMEFORMAT_YMDHMS ="yyyy-MM-dd HH:mm:ss";
    public static final String ZEROCLOCK = " 00:00:00";
    public static final Long LONG_OBNEHOUR = 1000 * 60 * 60L;
    public static final String TIME = "time";
}
