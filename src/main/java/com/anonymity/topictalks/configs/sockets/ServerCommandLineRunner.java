package com.anonymity.topictalks.configs.sockets;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.configs.sockets
 * - Created At: 15-09-2023 23:03:04
 * @since 1.0 - version of class
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class ServerCommandLineRunner implements CommandLineRunner {

    private final SocketIOServer server;


    @Override
    public void run(String... args) throws Exception {
        server.start();
    }

}
