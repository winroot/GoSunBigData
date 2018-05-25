package com.hzgc.service.dynrepo.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@Data
@AllArgsConstructor
public class SearchCallBack implements Serializable {
    Connection connection;
    Statement statement;
    ResultSet resultSet;
}

