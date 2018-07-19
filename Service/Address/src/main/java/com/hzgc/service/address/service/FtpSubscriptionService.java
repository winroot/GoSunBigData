package com.hzgc.service.address.service;

import com.hzgc.common.collect.facesub.SubscribeRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FtpSubscriptionService {

    @Autowired
    private SubscribeRegister subscribe;

    public boolean openFtpSubscription(String sessionId, List<String> ipcIdList) {
        if (!sessionId.equals("") && !ipcIdList.isEmpty()) {
            subscribe.updateSessionPath(sessionId, ipcIdList);
            return true;
        }
        return false;
    }

    public boolean closeFtpSubscription(String sessionId) {
        if (!sessionId.equals("")) {
            subscribe.deleteSessionPath(sessionId);
            return true;
        }
        return false;
    }
}
