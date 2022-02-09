package by.epam.baranovsky.banking.controller.listener;

import by.epam.baranovsky.banking.service.scheduler.SchedulerThread;
import org.apache.log4j.Logger;
import org.quartz.SchedulerException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Servlet listener that listens to changes in ServletContext lifecycle
 * and initializes scheduler thread and shuts it down.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class SchedulerListener implements ServletContextListener {

    private static final Logger logger = Logger.getLogger(SchedulerListener.class);
    /** Thread that runs scheduled tasks. */
    private SchedulerThread thread;

    /**
     * {@inheritDoc}
     * <p>
     *     Initializes and starts scheduler thread as a daemon.
     *     If scheduler thread failed to initialize or start,
     *     it sneakily throws an exception. If such exception is caught,
     *     it is logged and RuntimeException is thrown
     *     to stop the execution of application.
     * </p>
     * @see SchedulerThread#run()
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.info("Scheduled tasks are starting.");
        try {
            thread = new SchedulerThread();
            thread.setDaemon(true);
            thread.start();
        } catch (Exception e) {
            logger.error("Failed to start scheduled jobs.",e);
            throw new RuntimeException("Failed to start scheduled jobs.",e);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     *     Stops scheduler on scheduler thread.
     *     If scheduler failed to shut down,
     *     RuntimeException is thrown.
     * </p>
     * @see SchedulerThread#end()
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
            thread.end();
        } catch (SchedulerException e) {
            logger.error("Failed to shut scheduler down.",e);
            throw new RuntimeException("Failed to shut scheduler down.",e);
        }
        logger.info("Finishing scheduled tasks, shutting scheduler down.");
    }
}
