package by.epam.baranovsky.banking.service.scheduler;

import lombok.SneakyThrows;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import by.epam.baranovsky.banking.service.scheduler.jobs.*;

/**
 * Thread to perform scheduled tasks.
 *
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class SchedulerThread extends Thread {

    private static final String NAME= "Quartz scheduler thread";
    /**
     * Instance of scheduler.
     */
    private final Scheduler scheduler;
    /**
     * Flag that indicates that scheduler is ready to shut down.
     */
    private volatile boolean readyToShutdown = false;

    public SchedulerThread() throws SchedulerException {
        this.setName(NAME);
        scheduler = StdSchedulerFactory.getDefaultScheduler();
    }

    /**
     * Starts thread and schedules jobs.
     * Sneakily throws JobExecutionExceptions if any job has failed,
     * or SchedulerException if scheduler has failed to start or schedule job.
     */
    @SneakyThrows
    @Override
    public void run() {
            scheduler.start();
            scheduler.scheduleJob(AccountAccrual.getDetail(), AccountAccrual.getTrigger());
            scheduler.scheduleJob(BillStatusCheck.getDetail(), BillStatusCheck.getTrigger());
            scheduler.scheduleJob(CardExpirationJob.getDetail(), CardExpirationJob.getTrigger());
            scheduler.scheduleJob(LoanBillsAssignment.getDetail(), LoanBillsAssignment.getTrigger());
            scheduler.scheduleJob(LoanStatusCheck.getDetail(), LoanStatusCheck.getTrigger());
            scheduler.scheduleJob(PenaltyJob.getDetail(), PenaltyJob.getTrigger());
            scheduler.scheduleJob(RemoveHangingBillsJob.getDetail(), RemoveHangingBillsJob.getTrigger());
            readyToShutdown = true;
    }

    /**
     * Shuts the scheduler down if it is ready. Busy waits if it is not.
     * @throws SchedulerException if failed to shut scheduler down.
     */
    public void end() throws SchedulerException{
        while(!readyToShutdown){}
        scheduler.shutdown();
    }
}
