package com.tomcatmemshell.websocket;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpointConfig;

public class WebSocketConfig implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServerContainer container = (ServerContainer) sce.getServletContext().getAttribute("javax.websocket.server.ServerContainer");
        ServerEndpointConfig config = ServerEndpointConfig.Builder.create(MyWebSocketEndpoint.class, "/websocket")
                .build();
        try {
            container.addEndpoint(config);
        } catch (DeploymentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 处理上下文销毁
    }
}