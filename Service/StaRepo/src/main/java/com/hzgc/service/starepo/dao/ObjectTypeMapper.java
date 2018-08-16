package com.hzgc.service.starepo.dao;

import com.hzgc.service.starepo.model.ObjectType;

public interface ObjectTypeMapper {
    int deleteByPrimaryKey(String id);

    int insert(ObjectType record);

    int insertSelective(ObjectType record);

    ObjectType selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ObjectType record);

    int updateByPrimaryKey(ObjectType record);
}