package com.hzgc.service.util.auth.scan;

import com.hzgc.service.util.auth.service.AuthorizeSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.Set;

/**
 * @author liuzhikun
 * @date 2018/04/27
 */
public class AuthorizeCodeAutoSyncRunner implements ApplicationRunner {
    private static Logger logger = LoggerFactory.getLogger(AuthorizeCodeAutoSyncRunner.class);

    private AuthorizeSyncService authorizeSyncService;

    private AuthorizeCodeAnnotationScanner scanner;

    public AuthorizeCodeAnnotationScanner getScanner() {
        return scanner;
    }

    public void setScanner(AuthorizeCodeAnnotationScanner scanner) {
        this.scanner = scanner;
    }

    public AuthorizeSyncService getAuthorizeSyncService() {
        return authorizeSyncService;
    }

    public void setAuthorizeSyncService(AuthorizeSyncService authorizeSyncService) {
        this.authorizeSyncService = authorizeSyncService;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        logger.info("Begin to scan authorize code...");

        Set<DefaultAuthorizeDefination> authorizeDefinationSet = scanner.scan();
        if (null != authorizeDefinationSet && !authorizeDefinationSet.isEmpty()) {
            authorizeSyncService.sync(authorizeDefinationSet);
        }
    }
}
