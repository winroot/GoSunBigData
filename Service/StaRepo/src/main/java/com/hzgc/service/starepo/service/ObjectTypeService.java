package com.hzgc.service.starepo.service;

import com.hzgc.service.starepo.object.PersionTypeOpts;

import java.util.List;
import java.util.Map;

public interface ObjectTypeService {
    boolean addObjectType(String name, String creator, String remark);

    boolean deleteObjectType(String id);

    boolean updateObjectType(String  id, String name, String creator, String remark);

    List<Map<String, String>> searchObjectType(String name, int start, int limit);

}
