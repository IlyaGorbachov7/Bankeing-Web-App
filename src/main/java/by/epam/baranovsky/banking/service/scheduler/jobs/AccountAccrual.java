package by.epam.baranovsky.banking.service.scheduler.jobs;

import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.entity.Operation;
import by.epam.baranovsky.banking.service.exception.ServiceException;
import org.quartz.*;

import java.util.List;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class AccountAccrual extends AbstractJob {


    private static final String NAME = "accountAccrual";
    private static final JobDetail DETAIL = JobBuilder.newJob(AccountAccrual.class)
            .withIdentity(NAME, GROUP_NAME)
            .build();
    private static final Trigger TRIGGER = newTrigger()
            .withIdentity(NAME, GROUP_NAME)
            .withSchedule(cronSchedule("0 0 0 L * ? *"))
            .forJob(NAME, GROUP_NAME)
            .build();
    private static final Integer OPERATION_TYPE_ID = 13;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            List<Account> accountList = accountService.findAll();

            for(Account account : accountList){

                double percentage = Math.pow(1d + (account.getYearlyInterestRate()/100d), 1d/12d);

                double valueToAdd = account.getBalance()*(percentage-1d);

                if(valueToAdd != 0){
                    Operation operation = new Operation();
                    operation.setAccountId(account.getId());
                    operation.setTypeId(OPERATION_TYPE_ID);
                    operation.setValue(valueToAdd);
                    operationService.create(operation);
                }

            }
        } catch (ServiceException e) {
            logger.error("Unable to execute account accrual",e);
            throw new JobExecutionException("Unable to execute account accrual",e);
        }
    }

    public static JobDetail getDetail(){
        return DETAIL;
    }

    public static Trigger getTrigger(){
        return TRIGGER;
    }
}
