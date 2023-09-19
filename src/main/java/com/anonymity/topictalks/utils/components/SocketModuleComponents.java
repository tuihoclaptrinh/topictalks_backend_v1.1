package com.anonymity.topictalks.utils.components;

import com.anonymity.topictalks.models.persists.message.MessagePO;
import com.anonymity.topictalks.services.ISocketService;
import com.anonymity.topictalks.utils.constants.ConstantUtils;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.utils.components
 * - Created At: 15-09-2023 23:05:52
 * @since 1.0 - version of class
 */

@Slf4j
@Component
public class SocketModuleComponents {

    private final SocketIOServer server;
    private final ISocketService socketService;

    public SocketModuleComponents(SocketIOServer server, ISocketService socketService) {
        this.server = server;
        this.socketService = socketService;
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("send_message", MessagePO.class, onChatReceived());

    }


    private DataListener<MessagePO> onChatReceived() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
            socketService.saveMessage(senderClient, data);
        };
    }


    private ConnectListener onConnected() {
        return (client) -> {
//            String room = client.getHandshakeData().getSingleUrlParam("room");
//            String username = client.getHandshakeData().getSingleUrlParam("room");
            var params = client.getHandshakeData().getUrlParams();
            Long room = Long.parseLong(params.get("room").stream().collect(Collectors.joining()));
            String username = params.get("username").stream().collect(Collectors.joining());
            client.joinRoom(room.toString());
            socketService.saveInfoMessage(client, String.format(ConstantUtils.WELCOME_MESSAGE, username), room, username.toString());
            log.info(username);
            log.info("Socket ID[{}] - room[{}] - username [{}]  Connected to chat module through", client.getSessionId().toString(), room, username.toString());
        };

    }

    private DisconnectListener onDisconnected() {
        return client -> {
            var params = client.getHandshakeData().getUrlParams();
            Long room = Long.parseLong(params.get("room").stream().collect(Collectors.joining()));
            String username = params.get("username").stream().collect(Collectors.joining());
            socketService.saveInfoMessage(client, String.format(ConstantUtils.DISCONNECT_MESSAGE, username), room,username);
            log.info("Socket ID[{}] - room[{}] - username [{}]  disconnected to chat module through", client.getSessionId().toString(), room, username);
        };
    }

}
