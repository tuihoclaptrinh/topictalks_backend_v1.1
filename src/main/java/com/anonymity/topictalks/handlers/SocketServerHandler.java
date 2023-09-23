package com.anonymity.topictalks.handlers;

import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.handlers
 * - Created At: 21-09-2023 12:39:02
 * @since 1.0 - version of class
 */
@Component
public class SocketServerHandler implements ApplicationRunner {
    private Logger logger = LoggerFactory.getLogger(SocketServerHandler.class);
    @Resource
    SocketIOServer socketIOServer;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("socket server start...");
        socketIOServer.start();
    }
}
