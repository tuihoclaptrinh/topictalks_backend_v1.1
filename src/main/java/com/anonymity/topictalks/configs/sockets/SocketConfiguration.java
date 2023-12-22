package com.anonymity.topictalks.configs.sockets;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.configs.sockets
 * - Created At: 21-09-2023 08:02:43
 * @since 1.0 - version of class
 */

@Configuration
public class SocketConfiguration {

    @Value("${socket-server.port}")
    private Integer port;
    @Value("${socket-server.host}")
    private String host;

    @Value("${socket-server.workCount}")
    private int workCount;

    @Value("${socket-server.allowCustomRequests}")
    private boolean allowCustomRequests;

    @Value("${socket-server.upgradeTimeout}")
    private int upgradeTimeout;

    @Value("${socket-server.pingTimeout}")
    private int pingTimeout;

    @Value("${socket-server.pingInterval}")
    private int pingInterval;

    @Value("${socket-server.maxFramePayloadLength}")
    private int maxFramePayloadLength;

    @Value("${socket-server.maxHttpContentLength}")
    private int maxHttpContentLength;

    @Bean("socketIOServer")
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setPort(port);
        //config.setHostname(host);
        config.setOrigin("*");
        com.corundumstudio.socketio.SocketConfig socketConfig = new com.corundumstudio.socketio.SocketConfig();
        socketConfig.setReuseAddress(true);
        config.setSocketConfig(socketConfig);
        config.setWorkerThreads(workCount);
        config.setAllowCustomRequests(allowCustomRequests);
        config.setUpgradeTimeout(upgradeTimeout);
        config.setPingTimeout(pingTimeout);
        config.setPingInterval(pingInterval);
        config.setMaxHttpContentLength(maxHttpContentLength);
        config.setMaxFramePayloadLength(maxFramePayloadLength);
        return new SocketIOServer(config);
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }

}
