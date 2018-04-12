package com.hzgc.common.service.utils;

import java.util.ResourceBundle;

/**
 * server工具类
 *
 * @author liuzk
 */
public final class ServerIdUtil {
    private static String serverId = null;
    private final static String SERVER_ID_CONFIG = "server.id";
    private final static String DEFAULT_SERVER_ID = "0001";

    /**
     * 获取服务id
     *
     * @return 服务id
     */
    public static String getId() {
        try {
            if (null == serverId || serverId.isEmpty()) {
                ResourceBundle bundle = ResourceBundle.getBundle("application");
                serverId = bundle.getString(SERVER_ID_CONFIG);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            serverId = DEFAULT_SERVER_ID;
        }

        return serverId;
    }
}
