package com.hzgc.service.starepo.dao;

import lombok.Data;

import java.util.List;

@Data
public class SqlAndArgs {
    private String sql;
    private List<Object> args;
}
