package com.hzgc.collect;

import com.hzgc.collect.expand.conf.CommonConf;
import com.hzgc.collect.expand.merge.RecoverNotProData;
import com.hzgc.collect.expand.subscribe.*;
import com.hzgc.common.ftp.properties.CollectProperties;
import com.hzgc.collect.ftp.ClusterOverFtp;
import com.hzgc.collect.ftp.ConnectionConfigFactory;
import com.hzgc.collect.ftp.FtpServer;
import com.hzgc.collect.ftp.FtpServerFactory;
import com.hzgc.collect.ftp.command.CommandFactoryFactory;
import com.hzgc.collect.ftp.nativefs.filesystem.NativeFileSystemFactory;
import com.hzgc.collect.ftp.ftplet.FtpException;
import com.hzgc.collect.ftp.listener.ListenerFactory;
import com.hzgc.collect.ftp.usermanager.PropertiesUserManagerFactory;
import com.hzgc.common.util.file.ResourceFileUtil;
import com.hzgc.jni.NativeFunction;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

public class FTP extends ClusterOverFtp implements Serializable {

    private static Logger LOG = Logger.getLogger(FTP.class);

    private static Map<Integer, Integer> pidMap = new HashMap<>();

    /**
     * expand模块的公共Conf对象
     */
    private static CommonConf commonConf = new CommonConf();

    static {
        //Set the dynamic log configuration file refresh time
        PropertyConfigurator.configureAndWatch(
                ResourceFileUtil.loadResourceFile("log4j.properties").getAbsolutePath(), 10000);
        LOG.info("Dynamic log configuration is successful! Log configuration file refresh time:" + 10000 + "ms");
        //ftp capture subscription
        new FtpSwitch();
        FtpSubscriptionClient ftpSubscription = new FtpSubscriptionClient(
                Integer.valueOf(CollectProperties.getZookeeperSessionTimeout()),
                CollectProperties.getZookeeperAddress(),
                CollectProperties.getZookeeperPathSubscribe(),
                Boolean.valueOf(CollectProperties.getZookeeperWatcher()));
        ftpSubscription.createFtpSubscriptionZnode();
    }

    @Override
    public void startFtpServer() {

        //使用带CommonConf对象的有参构造器可以构造带有expand模块的FtpServerContext
        FtpServerFactory serverFactory = new FtpServerFactory(commonConf);
        LOG.info("Create " + FtpServerFactory.class + " successful");
        ListenerFactory listenerFactory = new ListenerFactory();
        LOG.info("Create " + ListenerFactory.class + " successful");
        //set the port of the listener
        listenerFactory.setPort(listenerPort);
        LOG.info("The port for listener is " + listenerPort);
        // replace the default listener
        serverFactory.addListener("default", listenerFactory.createListener());
        LOG.info("Add listner, name:default, class:" + serverFactory.getListener("default").getClass());
        // set customer user manager
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        try {
            userManagerFactory.setFile(ResourceFileUtil.loadResourceFile("users.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        serverFactory.setUserManager(userManagerFactory.createUserManager());
        LOG.info("Set customer user manager factory is successful, " + userManagerFactory.getClass());
        //set customer cmd factory
        CommandFactoryFactory commandFactoryFactory = new CommandFactoryFactory();
        serverFactory.setCommandFactory(commandFactoryFactory.createCommandFactory());
        LOG.info("Set customer command factory is successful, " + commandFactoryFactory.getClass());
        //set local file system
        NativeFileSystemFactory nativeFileSystemFactory = new NativeFileSystemFactory();
        serverFactory.setFileSystem(nativeFileSystemFactory);
        LOG.info("Set customer file system factory is successful, " + nativeFileSystemFactory.getClass());
        // TODO: 2017-10-9
        ConnectionConfigFactory connectionConfigFactory = new ConnectionConfigFactory();
        LOG.info("FTP Server Maximum logon number:" + connectionConfigFactory.createUDConnectionConfig().getMaxLogins());
        serverFactory.setConnectionConfig(connectionConfigFactory.createUDConnectionConfig());
        LOG.info("Set user defined connection config file is successful, " + connectionConfigFactory.getClass());
        FtpServer server = serverFactory.createServer();
        try {
            server.start();
            Integer ftpPID = Integer.valueOf(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
            pidMap.put(ftpPID, listenerPort);
        } catch (FtpException e) {
            e.printStackTrace();
        }
        ReceiveThread thread = new ReceiveThread();
        thread.start();
        LOG.info("************************************ FTP SERVER STARTED ************************************");
    }

    public static Map<Integer, Integer> getPidMap() {
        return pidMap;
    }

    public static void main(String args[]) throws Exception {
        int detectorNum = CollectProperties.getFaceDetectorNumber();
        LOG.info("Init face detector, number is " + detectorNum);
        for (int i = 0; i < detectorNum; i++) {
            NativeFunction.init();
        }
        //启动ftp之前，先恢复未处理数据
        LOG.info("start Recovering not process data...");
        RecoverNotProData recoverNotProData = new RecoverNotProData();
        Boolean success = recoverNotProData.recoverNotProData(commonConf);

        //若成功恢复未处理的数据，则启动ftp。
        if (success) {
            LOG.info("recoverNotProData successfully!");
            FTP ftp = new FTP();
            ftp.loadConfig();
            ftp.startFtpServer();
        }
    }
}
