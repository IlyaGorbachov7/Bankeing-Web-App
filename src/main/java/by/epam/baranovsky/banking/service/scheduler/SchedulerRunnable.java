package by.epam.baranovsky.banking.service.scheduler;

import by.epam.baranovsky.banking.service.scheduler.jobs.*;
import lombok.SneakyThrows;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;


public class SchedulerRunnable implements Runnable{

    private  Scheduler scheduler;

    @SneakyThrows
    @Override
    public void run() {
        scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        scheduler.scheduleJob(AccountAccrual.getDetail(), AccountAccrual.getTrigger());
        scheduler.scheduleJob(BillStatusCheck.getDetail(), BillStatusCheck.getTrigger());
        scheduler.scheduleJob(CardExpirationJob.getDetail(), CardExpirationJob.getTrigger());
        scheduler.scheduleJob(LoanBillsAssignment.getDetail(), LoanBillsAssignment.getTrigger());
        scheduler.scheduleJob(LoanStatusCheck.getDetail(), LoanStatusCheck.getTrigger());
        scheduler.scheduleJob(PenaltyJob.getDetail(), PenaltyJob.getTrigger());
    }


    public void end() throws SchedulerException{
        scheduler.shutdown(false);
    }

    public Scheduler getScheduler() {
        return scheduler;
    }
}