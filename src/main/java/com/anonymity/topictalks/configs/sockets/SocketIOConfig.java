package com.anonymity.topictalks.configs.sockets;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.configs.sockets
 * - Created At: 15-09-2023 23:03:26
 * @since 1.0 - version of class
 */
@Configuration
public class SocketIOConfig {

    @Value("${socket-server.host}")
    private String host;

    @Value("${socket-server.port}")
    private Integer port;

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(host);
        config.setPort(port);
        //  config.setContext("/socket.io");
        return new SocketIOServer(config);
    }

}
