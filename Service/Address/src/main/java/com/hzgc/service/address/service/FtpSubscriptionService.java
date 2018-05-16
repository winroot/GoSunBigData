package com.hzgc.service.address.service;

import com.hzgc.common.util.zookeeper.ZookeeperClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 人脸抓拍订阅功能（过滤前端设备）
 */
@Service
public class FtpSubscriptionService implements Serializable {

    /**
     * ftp subscription 相关配置
     */
    @Value("${zk.session.timeout}")
    private int zk_session_timeout;
    @Value("${zk.address}")
    private String zk_address;
    @Value("${zk.path.subscribe}")
    private String zk_path_subscribe;
    @Value("${zk.watcher}")
    private String zk_watcher;

    private ZookeeperClient zookeeperClient;

    public FtpSubscriptionService() {
        zookeeperClient = new ZookeeperClient(zk_session_timeout,
                zk_address, zk_path_subscribe, Boolean.valueOf(zk_watcher));
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
