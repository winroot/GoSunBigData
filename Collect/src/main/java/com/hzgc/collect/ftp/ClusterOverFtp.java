package com.hzgc.collect.ftp;

import com.hzgc.collect.expand.util.FtpServerProperties;

import java.io.Serializable;

public abstract class ClusterOverFtp implements Serializable {
    protected static int listenerPort = 0;
    protected static String passivePorts = null;
    protected static DataConnectionConfigurationFactory dataConnConf;

    public void loadConfig() throws Exception {

        dataConnConf = new DataConnectionConfigurationFactory();
        listenerPort = Integer.parseInt(FtpServerProperties.getFtp_proxy_port());
        passivePorts = FtpServerProperties.getData_ports();
        if (passivePorts != null){
            dataConnConf.setPassivePorts(passivePorts);
        }
    }

public abstract void startFtpServer();
        }
