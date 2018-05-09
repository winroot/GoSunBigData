package com.hzgc.service.address.service;

import com.hzgc.common.util.zookeeper.ZookeeperClient;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 人脸抓拍订阅功能（过滤前端设备）
 */
@Service
public class FtpSubscriptionService implements Serializable {

    private ZookeeperClient zookeeperClient;
    private String zk_path_subscribe = FtpApplicationProperties.getZk_path_subscribe();

    public FtpSubscriptionService() {
        int session_timeout = Integer.valueOf(FtpApplicationProperties.getZk_session_timeout());
        String zk_address = FtpApplicationProperties.getZk_address();
        boolean zk_watcher = Boolean.valueOf(FtpApplicationProperties.getZk_watcher());
        zookeeperClient = new ZookeeperClient(session_timeout, zk_address, zk_path_subscribe, zk_watcher);
    }

    /**
     * 打开人脸抓拍订阅功能
     *
     * @param userId    用户ID
     * @param ipcIdList 设备ID列表
     */
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
     * 关闭人脸抓拍订阅功能
     *
     * @param userId 用户ID
     */
    public void closeFtpReception(String userId) {
        if (!userId.equals("")) {
            zookeeperClient.delete(zk_path_subscribe + "/" + userId);
        }
    }
}
