package by.epam.baranovsky.banking.controller.listener;

import by.epam.baranovsky.banking.dao.connectionpool.ConnectionPool;
import by.epam.baranovsky.banking.dao.connectionpool.ConnectionPoolException;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Servlet listener that listens to changes in ServletContext lifecycle
 * and initializes connection pool or shuts it down.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class PoolListener implements ServletContextListener {

    Logger logger = Logger.getLogger(PoolListener.class);

    /**
     * {@inheritDoc}
     * <p>
     *     Creates an instance of connection pool and initializes it.
     *     If ConnectionPool was unable to initialize,
     *     throws RuntimeException to stop the execution of an application.
     * </p>
     */
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
    /**
     * {@inheritDoc}
     * <p>
     *     Disposes of connection pool.
     *     If ConnectionPool was unable to shut down, throws RuntimeException.
     * </p>
     */
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
