package ru.job4j.hibernate.myitems.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.job4j.hibernate.myitems.service.Store;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute("store", new Store());
        sce.getServletContext().setAttribute("mapper", new ObjectMapper());
    }
}
