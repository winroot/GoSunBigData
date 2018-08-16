package com.hzgc.service.starepo.dao;

import com.hzgc.service.starepo.model.ObjectType;

import java.util.List;

public interface ObjectTypeMapper {
    /**
     * 通过ID删除对象类型（单个对象）
     *
     * @param id 对象ID
     * @return 是否成功
     */
    int deleteObjectTypeById(String id);

    /**
     * 通过ID列表批量删除对象类型
     *
     * @param idList ID列表
     * @return 是否成功
     */
    int deleteObjectTypeByIdBatch(List<String> idList);

    /**
     * 添加对象类型（单个对象类型）
     *
     * @param objectType 对象类型
     * @return 是否成功
     */
    int addObjectType(ObjectType objectType);

    /**
     * 添加对象类型（带判空条件）
     *
     * @param objectType 对象类型
     * @return 是否成功
     */
    int addObjectTypeSelective(ObjectType objectType);

    /**
     * 根据ID查询对象类型
     *
     * @param id 对象类型ID
     * @return 对象类型信息
     */
    ObjectType selectObjectTypeById(String id);

    /**
     * 通过ID列表批量查询对象类型
     *
     * @param idList ID列表
     * @return 对象类型信息列表
     */
    List<ObjectType> selectObjectTypeByIdBatch(List<String> idList);

    /**
     * 查询全部对象类型
     *
     * @return 对象类型信息列表
     */
    List<ObjectType> selectAllObjectType();

    /**
     * 修改对象类型（单个对象类型）
     *
     * @param objectType 对象类型
     * @return 是否成功
     */
    int updateObjectTypeById(ObjectType objectType);
}