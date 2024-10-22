package com.tomcatmemshell.websocket;

import javax.websocket.*;
import java.io.IOException;

public class MyWebSocketEndpoint extends Endpoint {
    @Override
    public void onOpen(javax.websocket.Session session, EndpointConfig endpointConfig) {
        session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                // 处理接收到的消息
                System.out.println("Server response to client: " + message);

                try {
                    // 向客户端返回消息
                    session.getBasicRemote().sendText("Hello Client!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        super.onClose(session, closeReason);
    }
}