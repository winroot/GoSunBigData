package com.hzgc.service.device.dao;

import com.hzgc.common.service.connection.HBaseHelper;
import com.hzgc.common.service.table.column.DeviceTable;
import com.hzgc.common.util.empty.IsEmpty;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class HBaseDao {

    private static Logger LOG = Logger.getLogger(HBaseDao.class);

    public HBaseDao() {
        HBaseHelper.getHBaseConnection();
    }

    public boolean bindDevice(String platformId, String ipcID, String notes) {
        Table table = HBaseHelper.getTable(DeviceTable.TABLE_DEVICE);
        if (IsEmpty.strIsRight(ipcID) && IsEmpty.strIsRight(platformId)) {
            String ipcIDTrim = ipcID.trim();
            try {
                Put put = new Put(Bytes.toBytes(ipcIDTrim));
                put.addColumn(DeviceTable.CF_DEVICE, DeviceTable.PLAT_ID, Bytes.toBytes(platformId));
                put.addColumn(DeviceTable.CF_DEVICE, DeviceTable.NOTES, Bytes.toBytes(notes));
                table.put(put);
                LOG.info("Put data[" + ipcIDTrim + ", " + platformId + "] successful");
                return true;
            } catch (Exception e) {
                LOG.error("Current bind is failed!");
                return false;
            } finally {
                HBaseHelper.closeTable(table);
            }
        } else {
            LOG.error("Please check the arguments!");
            return false;
        }
    }

    public boolean unbindDevice(String platformId, String ipcID) {
        Table table = HBaseHelper.getTable(DeviceTable.TABLE_DEVICE);
        if (IsEmpty.strIsRight(platformId) && IsEmpty.strIsRight(ipcID)) {
            String ipcIDTrim = ipcID.trim();
            try {
                Delete delete = new Delete(Bytes.toBytes(ipcIDTrim));
                //根据设备ID（rowkey），删除该行的平台ID列，而非删除这整行数据。
                delete.addColumns(DeviceTable.CF_DEVICE, DeviceTable.PLAT_ID);
                table.delete(delete);
                LOG.info("Unbind device:" + ipcIDTrim + " and " + platformId + " successful");
                return true;
            } catch (Exception e) {
                LOG.error("Current unbind is failed!");
                return false;
            } finally {
                HBaseHelper.closeTable(table);
            }
        } else {
            LOG.error("Please check the arguments!");
            return false;
        }
    }

    public boolean renameNotes(String notes, String ipcID) {
        Table table = HBaseHelper.getTable(DeviceTable.TABLE_DEVICE);
        if (IsEmpty.strIsRight(ipcID)) {
            String ipcIDTrim = ipcID.trim();
            try {
                Put put = new Put(Bytes.toBytes(ipcIDTrim));
                put.addColumn(DeviceTable.CF_DEVICE, DeviceTable.NOTES, Bytes.toBytes(notes));
                table.put(put);
                LOG.info("Rename " + ipcIDTrim + "'s notes successful!");
                return true;
            } catch (Exception e) {
                LOG.error("Current renameNotes is failed!");
                return false;
            }
        } else {
            LOG.error("Please check the arguments!");
            return false;
        }
    }
}
