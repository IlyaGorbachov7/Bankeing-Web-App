package by.epam.baranovsky.banking.controller.listener;

import by.epam.baranovsky.banking.service.scheduler.SchedulerDispatcher;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SchedulerListener implements ServletContextListener {

    Logger logger = Logger.getLogger(SchedulerListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.info("Scheduled tasks are starting.");
        try {
            SchedulerDispatcher.getInstance().init();
        } catch (Exception e) {
            logger.error("Failed to start scheduled jobs.",e);
            throw new RuntimeException("Failed to start scheduled jobs.",e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
            SchedulerDispatcher.getInstance().end();
        } catch (Exception e) {
            logger.error("Failed to finish scheduled jobs.",e);
            throw new RuntimeException("Failed to finish scheduled jobs.",e);
        }
        logger.info("Finishing scheduled tasks.");
    }
}
