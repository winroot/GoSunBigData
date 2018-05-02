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

//数据库相关操作
@Repository
public class DeviceDao {

    private static Logger LOG = Logger.getLogger(DeviceDao.class);

    /**
     * 绑定设备到平台（外）（赵喆）
     *
     * @param platformId 平台 id
     * @param ipcID   设备 ipcID
     * @param notes      备注
     * @return 是否绑定成功
     */
    public boolean bindDevice(String platformId, String ipcID, String notes){
        Table table = HBaseHelper.getTable(DeviceTable.TABLE_DEVICE);
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
    }

    /**
     * 解除设备与平台的绑定关系（外）（赵喆）
     *
     * @param platformId 平台 id
     * @param ipcID   设备 ipcID
     * @return 是否解除绑定成功
     */
    public boolean unbindDevice(String platformId, String ipcID){
        Table table = HBaseHelper.getTable(DeviceTable.TABLE_DEVICE);
            String ipcIDTrim = ipcID.trim();
            try {
                Delete delete = new Delete(Bytes.toBytes(ipcIDTrim));
                //根据设备ID（rowkey），删除该行的平台ID列，而非删除这整行数据。
                delete.addColumns(DeviceTable.CF_DEVICE,DeviceTable.PLAT_ID);
                table.delete(delete);
                LOG.info("Unbind device:" + ipcIDTrim + " and " + platformId + " successful");
                return true;
            } catch (Exception e) {
                LOG.error("Current unbind is failed!");
                return false;
            } finally {
                HBaseHelper.closeTable(table);
            }
    }

    /**
     * 修改备注
     *
     * @param notes 备注信息（外）（赵喆）
     * @param ipcID    设备在平台上的 ipcID
     * @return 是否重命名成功
     */
    public boolean renameNotes(String notes, String ipcID){
        Table table = HBaseHelper.getTable(DeviceTable.TABLE_DEVICE);
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
        }
}
