package com.hzgc.collect.expand.subscribe;


import com.hzgc.collect.expand.util.FTPConstants;
import com.hzgc.common.util.zookeeper.ZookeeperClient;
import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

import java.util.*;

/**
 * 使用ZookeeperClient支持人脸抓拍订阅功能
 */
public class FtpSubscriptionClient extends ZookeeperClient {

    private static Logger LOG = Logger.getLogger(FtpSubscriptionClient.class);

    public FtpSubscriptionClient(int session_timeout, String zookeeperAddress, String path, boolean watcher) {
        super(session_timeout, zookeeperAddress, path, watcher);
    }

    /**
     * 创建人脸抓拍订阅功能节点
     */
    public void createFtpSubscriptionZnode() {
        super.create();
    }

    /**
     * 获取MQ节点所有子节点(长期调用，故不自动创建连接，不关闭连接)
     */
    public List<String> getChildren() {
        List<String> children = new ArrayList<>();
        try {
            children = zooKeeper.getChildren(path, watcher);
        } catch (KeeperException | InterruptedException e) {
            LOG.warn("Get MQ znode Children is null!" + path);
            e.printStackTrace();
        }
        return children;
    }

    /**
     * 获取人脸抓拍订阅功能子节点数据(长期调用，故不自动创建连接，不关闭连接)
     */
    public byte[] getDate(String path) {
        byte[] bytes = null;
        try {
            Stat stat = zooKeeper.exists(path, watcher);
            bytes = zooKeeper.getData(path, watcher, stat);
        } catch (KeeperException | InterruptedException e) {
            LOG.error("Get MQ znode data Failed!");
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 删除人脸抓拍订阅功能节点(长期调用，故不自动创建连接，不关闭连接)
     */
    public void delete(String path) {
        try {
            zooKeeper.delete(path, FTPConstants.NUM_MINUS_ONE);
        } catch (InterruptedException | KeeperException e) {
            LOG.error("Delete MQ znode Failed!");
            e.printStackTrace();
        }
    }

    /**
     * 获取人脸抓拍订阅功能节点数据
     */
    public Map<String, Map<String, List<String>>> getData() {
        Map<String, Map<String, List<String>>> mqMap = new HashMap<>();
        List<String> children = getChildren();
        if (!children.isEmpty()) {
            for (String child : children) {
                Map<String, List<String>> map = new HashMap<>();
                String childPath = path + "/" + child;
                byte[] data = getDate(childPath);
                if (data != null) {
                    String ipcIds = new String(data);
                    if (!ipcIds.equals("") && ipcIds.contains(",") && ipcIds.split(",").length >= FTPConstants.NUM_THREE) {
                        ipcIds = ipcIds.substring(FTPConstants.NUM_ZERO, ipcIds.length() - FTPConstants.NUM_ONE);
                        List<String> list = Arrays.asList(ipcIds.split(","));
                        String userId = list.get(FTPConstants.NUM_ZERO);
                        String time = list.get(FTPConstants.NUM_ONE);
                        List<String> ipcIdList = new ArrayList<>();
                        for (int i = FTPConstants.NUM_TWO; i < list.size(); i++) {
                            ipcIdList.add(list.get(i));
                        }
                        map.put(time, ipcIdList);
                        mqMap.put(userId, map);
                    }
                }
            }
        }
        return mqMap;
    }
}
