package com.hzgc.service.starepo.service;

import com.hzgc.service.starepo.dao.ObjectTypeDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ObjectTypeService {
    private static Logger LOG = Logger.getLogger(ObjectInfoHandlerService.class);

    @Autowired
    private ObjectTypeDao objectTypeDao;

    /**
     * 添加objectType
     * @param name
     * @param creator
     * @param remark
     * @return
     */
    public boolean addObjectType(String name, String creator, String remark) {
        return objectTypeDao.addObjectType(name, creator, remark);
    }

    /**
     * 删除objectType
     * @param id
     * @return
     */
    public boolean deleteObjectType(String id) {
       return objectTypeDao.deleteObjectType(id);
    }

    /**
     * 修改ObjectType
     * @param id
     * @param name
     * @param creator
     * @param remark
     * @return
     */
    public boolean updateObjectType(String  id, String name, String creator, String remark) {
        return objectTypeDao.updateObjectType(id, name, creator, remark);
    }

    /**
     * 查询objectType
     * @param name
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public List<Map<String, String>> searchObjectType(String name, int pageIndex, int pageSize) {
        return objectTypeDao.searchObjectType(name, pageIndex, pageSize);
    }
}
