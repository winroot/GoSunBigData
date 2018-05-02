package com.hzgc.service.starepo.dao;

import com.hzgc.common.service.table.column.ObjectInfoTable;
import com.hzgc.service.starepo.object.ObjectSearchResult;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.annotation.Resource;
import java.io.Serializable;

public class PhoenixDao implements Serializable {
    @Resource(name = "phoenixJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public ObjectSearchResult searchByRowkey(String id) {
        String sql = "select * from " + ObjectInfoTable.TABLE_NAME + " where id = ?";
        jdbcTemplate.queryForRowSet();
    }
}
