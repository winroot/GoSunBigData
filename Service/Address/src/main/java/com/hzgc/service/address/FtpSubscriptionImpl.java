package com.hzgc.service.address;

import com.hzgc.common.ftp.properties.CollectProperties;
import com.hzgc.common.util.zookeeper.ZookeeperClient;

import java.io.Serializable;
import java.util.List;

public class FtpSubscriptionImpl implements FtpSubscription, Serializable {

    private ZookeeperClient zookeeperClient;
    private String zk_path_subscribe = CollectProperties.getZookeeperPathSubscribe();

    public FtpSubscriptionImpl() {
        int session_timeout = Integer.valueOf(CollectProperties.getZookeeperSessionTimeout());
        String zk_address = CollectProperties.getZookeeperAddress();
        boolean zk_watcher = Boolean.valueOf(CollectProperties.getZookeeperWatcher());
        zookeeperClient = new ZookeeperClient(session_timeout, zk_address, zk_path_subscribe, zk_watcher);
    }

    /**
     * 打开MQ接收数据
     *
     * @param userId    用户ID
     * @param ipcIdList 设备ID列表
     */
    @Override
    public void openFtpReception(String userId, List<String> ipcIdList) {
        if (!userId.equals("") && !ipcIdList.isEmpty()) {
            String childPath = zk_path_subscribe + "/" + userId;
            long time = System.currentTimeMillis();
            StringBuilder data = new StringBuilder();
            data.append(userId).append(",").append(time).append(",");
            for (String ipcId : ipcIdList) {
                data.append(ipcId).append(",");
            }
            zookeeperClient.create(childPath, data.toString().getBytes());
        }
    }

    /**
     * 关闭MQ接收数据
     *
     * @param userId 用户ID
     */
    @Override
    public void closeFtpReception(String userId) {
        if (!userId.equals("")) {
            zookeeperClient.delete(zk_path_subscribe + "/" + userId);
        }
    }
}
