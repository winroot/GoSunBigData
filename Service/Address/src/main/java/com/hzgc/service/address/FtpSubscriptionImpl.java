package com.hzgc.service.address;

import com.hzgc.common.util.file.ResourceFileUtil;
import com.hzgc.common.util.io.IOUtil;
import com.hzgc.common.util.zookeeper.ZookeeperClient;
import com.hzgc.dubbo.address.FtpSubscription;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Properties;

public class FtpSubscriptionImpl implements FtpSubscription, Serializable {
    private static Logger LOG = Logger.getLogger(FtpSubscriptionImpl.class);
    private ZookeeperClient zookeeperClient;
    private Properties properties = new Properties();

    public FtpSubscriptionImpl() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(ResourceFileUtil.loadResourceFile("service_address.properties"));
            properties.load(fis);
            zookeeperClient = new ZookeeperClient(
                    Integer.valueOf(properties.getProperty("zk_session_timeout")),
                    properties.getProperty("zk_address"),
                    properties.getProperty("zk_path_subscribe"),
                    Boolean.valueOf(properties.getProperty("zk_watcher")));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeStream(fis);
        }
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
            String childPath = properties.getProperty("zk_path_subscribe") + "/" + userId;
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
            zookeeperClient.delete(properties.getProperty("zk_path_subscribe") + "/" + userId);
        }
    }
}
