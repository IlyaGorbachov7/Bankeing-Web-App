package by.epam.baranovsky.banking.controller.listener;

import by.epam.baranovsky.banking.dao.connectionpool.ConnectionPool;
import by.epam.baranovsky.banking.dao.connectionpool.ConnectionPoolException;
import com.mysql.cj.log.Log;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AppCycleListener implements ServletContextListener {

    Logger logger = Logger.getLogger(AppCycleListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.info("Application is starting.");
        try {
            ConnectionPool.getInstance();
        } catch (ConnectionPoolException e) {
            logger.error("Unable to initialize connection pool",e);
            throw new RuntimeException("Unable to initialize connection pool.", e);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

        logger.info("Application is shutting down.");
        try {
            ConnectionPool.getInstance().dispose();
        } catch (ConnectionPoolException e) {
            logger.error("Unable to dispose of a connection pool.",e);
            throw new RuntimeException("Unable to dispose of a connection pool.", e);
        }
    }
}
