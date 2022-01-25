package by.epam.baranovsky.banking.service.scheduler;

import by.epam.baranovsky.banking.service.scheduler.jobs.*;
import lombok.SneakyThrows;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;


public class SchedulerThread extends Thread {

    private static final String NAME= "Quartz scheduler thread";
    private final Scheduler scheduler;
    private volatile boolean readyToShutdown = false;

    public SchedulerThread() throws SchedulerException {
        this.setName(NAME);
        scheduler = StdSchedulerFactory.getDefaultScheduler();
    }

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
            readyToShutdown=true;
    }

    public void end() throws SchedulerException{
        while(!readyToShutdown){}
        scheduler.shutdown();
    }
}
