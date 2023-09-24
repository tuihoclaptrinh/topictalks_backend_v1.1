package com.anonymity.topictalks.configs.socketDemo;

import com.anonymity.topictalks.models.persists.message.MessageDemoPO;
import com.anonymity.topictalks.services.SocketService;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Slf4j
@Transactional(rollbackFor = Throwable.class)
public class SocketModule {


    private SocketIOServer server;
    private SocketService socketService;

    public SocketModule(SocketIOServer server, SocketService socketService) {
        this.server = server;
        this.socketService = socketService;
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("send_message", MessageDemoPO.class, onChatReceived());

    }


    private DataListener<MessageDemoPO> onChatReceived() {
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
            String conversationId = params.get("conversationId").stream().collect(Collectors.joining());
            String senderId = params.get("senderId").stream().collect(Collectors.joining());
            client.joinRoom(conversationId);
            socketService.saveInfoMessage(client, String.format(SocketConstants.WELCOME_MESSAGE, senderId), Long.valueOf(conversationId));
            log.info("Socket ID[{}] - room[{}] - username [{}]  Connected to chat module through", client.getSessionId().toString(), conversationId, senderId);
        };

    }

    private DisconnectListener onDisconnected() {
        return client -> {
            var params = client.getHandshakeData().getUrlParams();
            String conversationId = params.get("conversationId").stream().collect(Collectors.joining());
            String senderId = params.get("senderId").stream().collect(Collectors.joining());
            socketService.saveInfoMessage(client, String.format(SocketConstants.DISCONNECT_MESSAGE, senderId),Long.valueOf(conversationId));
            log.info("Socket ID[{}] - room[{}] - username [{}]  discnnected to chat module through", client.getSessionId().toString(), conversationId, senderId);
        };
    }


}
