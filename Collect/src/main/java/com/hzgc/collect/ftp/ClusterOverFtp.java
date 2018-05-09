package com.hzgc.collect.ftp;

import com.hzgc.collect.expand.util.CollectProperties;

import java.io.Serializable;

public abstract class ClusterOverFtp implements Serializable {
    protected static int listenerPort = 0;
    protected static String passivePorts = null;
    protected static DataConnectionConfigurationFactory dataConnConf;

    public void loadConfig() throws Exception {

        dataConnConf = new DataConnectionConfigurationFactory();
        listenerPort = CollectProperties.getPort();
        passivePorts = CollectProperties.getDataPorts();
        if (passivePorts != null){
            dataConnConf.setPassivePorts(passivePorts);
        }
    }

public abstract void startFtpServer();
        }
