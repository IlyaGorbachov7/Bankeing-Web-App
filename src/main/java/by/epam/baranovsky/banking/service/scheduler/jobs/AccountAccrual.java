package by.epam.baranovsky.banking.service.scheduler.jobs;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.entity.Operation;
import by.epam.baranovsky.banking.service.exception.ServiceException;
import org.quartz.*;

import java.util.List;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * A job that performs monthly accrual (or subtraction) on Accounts
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class AccountAccrual extends AbstractJob {


    private static final String NAME = "accountAccrual";
    private static final JobDetail DETAIL = JobBuilder.newJob(AccountAccrual.class)
            .withIdentity(NAME, GROUP_NAME)
            .build();
    /**
     * Fires at last day of every month.
     */
    private static final Trigger TRIGGER = newTrigger()
            .withIdentity(NAME, GROUP_NAME)
            .withSchedule(cronSchedule("0 0 0 L * ? *"))
            .forJob(NAME, GROUP_NAME)
            .build();

    /**
     * Iterates through every unlocked and suspended accounts
     * and modifies their balances according to their interest rate.
     * @param jobExecutionContext context of the job
     * @throws JobExecutionException if ServiceException occurs
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            List<Account> accountList = accountService.findAll();

            for(Account account : accountList){
                if(account.getStatusId().equals(DBMetadata.ACCOUNT_STATUS_BLOCKED)
                        || account.getStatusId().equals(ACC_STATUS_PENDING)){
                    continue;
                }

                double percentage = Math.pow(1d + (account.getYearlyInterestRate()/100d), 1d/12d);

                double valueToAdd = account.getBalance()*(percentage-1d);

                if(valueToAdd != 0){
                    Operation operation = new Operation();
                    operation.setAccountId(account.getId());
                    operation.setTypeId(DBMetadata.OPERATION_TYPE_ACCRUAL);
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
