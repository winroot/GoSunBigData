package com.hzgc.service.starepo.service;

import com.hzgc.service.starepo.bean.param.ObjectTypeParam;
import com.hzgc.service.starepo.dao.PhoenixDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ObjectTypeService {

    @Autowired
    private PhoenixDao phoenixDao;

    /**
     * 添加objectType
     *
     * @param param add objectType对象
     * @return boolean
     */
    public boolean addObjectType(ObjectTypeParam param) {
        String name = param.getObjectTypeName();
        String creator = param.getCreator();
        String remark = param.getRemark();
        return phoenixDao.addObjectType(name, creator, remark);
    }

    /**
     * 删除objectType
     *
     * @param objectTypeKeyList 类型Key列表
     * @return boolean
     */
    public boolean deleteObjectType(List<String> objectTypeKeyList) {
        return phoenixDao.deleteObjectType(objectTypeKeyList);
    }

    /**
     * 修改ObjectType
     *
     * @param param update objectType对象
     * @return boolean
     */
    public boolean updateObjectType(ObjectTypeParam param) {
        String id = param.getObjectTypeKey();
        String name = param.getObjectTypeName();
        String creator = param.getCreator();
        String remark = param.getRemark();
        return phoenixDao.updateObjectType(id, name, creator, remark);
    }

    /**
     * 查询objectType
     *
     * @param pageIndex 页码
     * @param pageSize  每页行数
     * @return List<ObjectTypeParam>
     */
    public List<ObjectTypeParam> searchObjectType(int pageIndex, int pageSize) {
        return phoenixDao.searchObjectType(pageIndex, pageSize);
    }

    /**
     * 查询objectTypeName
     *
     * @param objectTypeKeys 对象类型key数组
     * @return Map
     */
    public Map<String, String> searchObjectTypeNames(List<String> objectTypeKeys){
        return phoenixDao.searchTypeNames(objectTypeKeys);
    }

    /**
     * 统计对象类型的数量
     *
     * @return int 对象类型的数量
     */
    public int objectTypeCount(){
        return phoenixDao.objectTypeCount();
    }
}
