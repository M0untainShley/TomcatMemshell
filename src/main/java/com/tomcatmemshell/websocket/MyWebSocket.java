package com.tomcatmemshell.websocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.logging.Logger;

@ServerEndpoint("/mywebsocket")
public class MyWebSocket {
    private static final Logger LOGGER = Logger.getLogger(MyWebSocket.class.getName());

    // 连接建立时调用
    @OnOpen
    public void onOpen(Session session) {
        LOGGER.info("Connection opened: " + session.getId());
        try {
            session.getBasicRemote().sendText("Welcome! Your session ID is " + session.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 收到消息时调用
    @OnMessage
    public void onMessage(String message, Session session) {
        LOGGER.info("Received message from " + session.getId() + ": " + message);
        try {
            session.getBasicRemote().sendText("Echo: " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 连接关闭时调用
    @OnClose
    public void onClose(Session session, CloseReason reason) {
        LOGGER.info("Connection closed. Session ID: " + session.getId() +
                ", Reason: " + reason.getReasonPhrase());
    }

    // 发生错误时调用
    @OnError
    public void onError(Session session, Throwable throwable) {
        LOGGER.severe("Error on session " + session.getId() + ": " + throwable.getMessage());
    }
}