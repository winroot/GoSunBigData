package com.hzgc.service.device.service;

import com.hzgc.service.device.dao.HBaseDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {

    private static Logger LOG = Logger.getLogger(DeviceService.class);

    @Autowired
    private HBaseDao hBaseDao;

    /**
     * 绑定设备到平台
     *
     * @param platformId 平台 id
     * @param ipcID      设备 ipcID
     * @param notes      备注
     * @return 是否绑定成功
     */
    public boolean bindDevice(String platformId, String ipcID, String notes) {
        return hBaseDao.bindDevice(platformId, ipcID, notes);
    }

    /**
     * 解除设备与平台的绑定关系
     *
     * @param platformId 平台 id
     * @param ipcID      设备 ipcID
     * @return 是否解除绑定成功
     */
    public boolean unbindDevice(String platformId, String ipcID) {
        return hBaseDao.unbindDevice(platformId, ipcID);
    }

    /**
     * 修改备注
     *
     * @param notes 备注信息
     * @param ipcID 设备在平台上的 ipcID
     * @return 是否重命名成功
     */
    public boolean renameNotes(String notes, String ipcID) {
        return hBaseDao.renameNotes(notes, ipcID);
    }
}
