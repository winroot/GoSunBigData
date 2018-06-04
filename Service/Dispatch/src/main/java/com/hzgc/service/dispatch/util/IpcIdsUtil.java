package com.hzgc.service.dispatch.util;

import com.hzgc.common.util.uuid.UuidUtil;
import com.hzgc.service.dispatch.bean.Device;
import com.hzgc.service.dispatch.bean.Dispatch;
import com.hzgc.service.dispatch.bean.Rule;
import com.hzgc.service.util.api.DeviceDTO;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class IpcIdsUtil {

    public static List <Long> toDeviceIdList(List <Device> list) {
        List <Long> li = new ArrayList <>();
        for (Device dev : list) {
            Long i = Long.valueOf(dev.getId());
            li.add(i);
        }
        return li;
    }


    //数据解析存储
    public static Map <String, Dispatch> toDispatchMap(Dispatch dispatch) {
        if (null != dispatch){
            Rule rule = dispatch.getRule();
            String ruleId = null;
            //设置ruleId
            if (null == dispatch.getRule().getRuleId()){
                 rule.setRuleId(UuidUtil.getUuid());
                 ruleId= rule.getRuleId();
            }
//            List<Device> deviceList = dispatch.getDevices();
//            List<DeviceDTO> deviceDTOList = new ArrayList <>();
//            for (Map.Entry<String,DeviceDTO> entry:map.entrySet()){
//                DeviceDTO deviceDTO = entry.getValue();
//                deviceDTOList.add(deviceDTO);
//            }
//            for (int i=0;i<deviceDTOList.size();i++){
//                deviceList.get(i).setIpcId(deviceDTOList.get(i).getSerial());
//            }
            Map <String, Dispatch> dispatchMap = new LinkedHashMap <>();
            dispatchMap.put(ruleId, dispatch);
            return dispatchMap;
        }else {
            return null;
        }
    }
}
