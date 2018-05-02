package com.hzgc.service.device.service;

import com.hzgc.common.service.table.column.DeviceTable;
import com.hzgc.common.util.empty.IsEmpty;
import com.hzgc.common.service.connection.HBaseHelper;
import com.hzgc.service.device.dao.DeviceDao;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceServiceImpl {

    @Autowired
    private DeviceDao deviceDao;

    private static Logger LOG = Logger.getLogger(DeviceServiceImpl.class);


    public boolean bindDevice(String platformId, String ipcID, String notes) {
        if (IsEmpty.strIsRight(ipcID) && IsEmpty.strIsRight(platformId)){
           return deviceDao.bindDevice(platformId,ipcID,notes);
        }
            LOG.error("Please check the arguments!");
            return false;
    }

    public boolean unbindDevice(String platformId, String ipcID) {
        if (IsEmpty.strIsRight(platformId) && IsEmpty.strIsRight(ipcID)) {
            return deviceDao.unbindDevice(platformId,ipcID);
        }
            LOG.error("Please check the arguments!");
            return false;
    }

    public boolean renameNotes(String notes, String ipcID) {
        if (IsEmpty.strIsRight(ipcID)) {
          return deviceDao.renameNotes(notes,ipcID);
        }
            LOG.error("Please check the arguments!");
            return false;
    }
}
