package com.hzgc.service.starepo.service;

import com.hzgc.service.starepo.dao.ObjectTypeDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ObjectTypeService {

    @Autowired
    private ObjectTypeDao objectTypeDao;

    /**
     * 添加objectType
     *
     * @param name    类型名
     * @param creator 创建者
     * @param remark  备注
     * @return boolean
     */
    public boolean addObjectType(String name, String creator, String remark) {
        return objectTypeDao.addObjectType(name, creator, remark);
    }

    /**
     * 删除objectType
     *
     * @param id 类型ID
     * @return boolean
     */
    public boolean deleteObjectType(String id) {
        return objectTypeDao.deleteObjectType(id);
    }

    /**
     * 修改ObjectType
     *
     * @param id      类型ID
     * @param name    类型名
     * @param creator 创建者
     * @param remark  备注
     * @return boolean
     */
    public boolean updateObjectType(String id, String name, String creator, String remark) {
        return objectTypeDao.updateObjectType(id, name, creator, remark);
    }

    /**
     * 查询objectType
     *
     * @param name      类型名
     * @param pageIndex 页码
     * @param pageSize  每页行数
     * @return List<Map<String, String>>
     */
    public List<Map<String, String>> searchObjectType(String name, int pageIndex, int pageSize) {
        return objectTypeDao.searchObjectType(name, pageIndex, pageSize);
    }
}
