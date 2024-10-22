package com.tomcatmemshell.valve;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

import javax.servlet.ServletException;
import java.io.IOException;

public class TestValve extends ValveBase {
    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        System.out.println("TestValve invoke...");
        // 防止程序到这中断，需要继续调用下一个 valve
        getNext().invoke(request, response);
    }
}