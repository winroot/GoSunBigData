package com.hzgc.service.starepo.dao;

import com.hzgc.service.starepo.model.ObjectInfo;

import java.util.List;

public interface ObjectInfoMapper {
    /**
     * 通过对象ID删除对象（单个对象）
     *
     * @param id 对象ID
     * @return 是否成功
     */
    int deleteObjectById(String id);

    /**
     * 通过对象ID列表批量删除对象
     *
     * @param idList ID列表
     * @return 是否成功
     */
    int deleteObjectByIdBatch(List<String> idList);

    /**
     * 检查对象是否存在
     *
     * @param objectInfo 人员对象
     * @return 是否成功
     */
    List<ObjectInfo> checkExists(ObjectInfo objectInfo);

    /**
     * 添加对象（单个对象）
     *
     * @param objectInfo 人员对象
     * @return 是否成功
     */
    int addObject(ObjectInfo objectInfo);

    /**
     * 添加对象（多个对象）
     *
     * @param objectInfos 人员对象列表
     * @return 是否成功
     */
    int addObjectByBatch(List<ObjectInfo> objectInfos);

    /**
     * 添加对象（带判空条件）
     *
     * @param objectInfo 人员对象
     * @return 是否成功
     */
    int addObjectSelective(ObjectInfo objectInfo);

    /**
     * 修改对象（单个对象）
     *
     * @param objectInfo 人员对象
     * @return 是否成功
     */
    int updateObjectSelective(ObjectInfo objectInfo);

    /**
     * 根据对象ID查询对象
     *
     * @param id 对象ID
     * @return 对象信息
     */
    ObjectInfo selectObjectById(String id);

    /**
     * 通过ID列表批量查询对象
     *
     * @param idList ID列表
     * @return 对象信息列表
     */
    List<ObjectInfo> selectObjectByIdBatch(List<String> idList);

    /**
     * 查询对象库,一般用作基本的分页查询
     *
     * @return 对象信息列表
     */
    List<ObjectInfo> selectObject();


}