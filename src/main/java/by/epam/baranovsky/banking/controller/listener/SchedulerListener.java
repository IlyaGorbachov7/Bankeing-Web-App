package by.epam.baranovsky.banking.controller.listener;

import by.epam.baranovsky.banking.service.scheduler.SchedulerThread;
import org.apache.log4j.Logger;
import org.quartz.SchedulerException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SchedulerListener implements ServletContextListener {

    private static final Logger logger = Logger.getLogger(SchedulerListener.class);
    private SchedulerThread thread;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.info("Scheduled tasks are starting.");
        try {
            thread = new SchedulerThread();
            thread.setDaemon(true);
            //thread.start();
        } catch (Exception e) {
            logger.error("Failed to start scheduled jobs.",e);
            throw new RuntimeException("Failed to start scheduled jobs.",e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
      /*
        try {
            thread.end();
        } catch (SchedulerException e) {
            logger.error("Failed to finish scheduled jobs.",e);
            throw new RuntimeException("Failed to finish scheduled jobs.",e);
        }*/
        logger.info("Finishing scheduled tasks.");
    }
}
