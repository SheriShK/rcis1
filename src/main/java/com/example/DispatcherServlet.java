package com.example;

import jakarta.servlet.annotation.WebServlet;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import jakarta.servlet.Filter;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

/**
 * Конфигурация сервлета для обработки запросов
 */
@WebServlet
public class DispatcherServlet extends AbstractAnnotationConfigDispatcherServletInitializer{

    /**
     * Конфигурация приложения
     * @return
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {AppConfig.class};
    }

    /**
     * Конфигурация веб
     * @return
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {WebConfig.class};
    }

    /**
     * Поиск шаблонов
     * @return
     */
    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }

    @Override
    public void onStartup(ServletContext aServletContext) throws ServletException {
        super.onStartup(aServletContext);
        registerHiddenFieldFilter(aServletContext);
    }

    /**
     * Фильтр
     * @param aContext
     */
    private void registerHiddenFieldFilter(ServletContext aContext) {
        aContext.addFilter("hiddenHttpMethodFilter",
                (Filter) new HiddenHttpMethodFilter()).addMappingForUrlPatterns(null ,true, "/*");
    }
}
