package by.epam.baranovsky.banking.service.scheduler.jobs;

import by.epam.baranovsky.banking.constant.ConfigManager;
import by.epam.baranovsky.banking.constant.ConfigParams;
import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.entity.Bill;
import by.epam.baranovsky.banking.entity.Operation;
import by.epam.baranovsky.banking.entity.Penalty;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.entity.criteria.SingularValue;
import by.epam.baranovsky.banking.service.exception.ServiceException;
import org.quartz.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
/**
 * A job that checks if bills are paid or overdue.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class BillStatusCheck extends AbstractJob {

    private static final long DAY_LENGTH=86400000L;
    /** A margin to postpone bills with due date and with payment account that is blocked */
    private static final long DELAY_TIME = DAY_LENGTH *
            Long.parseLong(ConfigManager.getInstance().getValue(ConfigParams.BILL_DELAY_TIME));
    private static final String NAME = "billStatusCheck";
    private static final JobDetail DETAIL = JobBuilder.newJob(BillStatusCheck.class)
            .withIdentity(NAME, GROUP_NAME)
            .build();
    /**
     * Fires at second :00, at minute :30, every hour starting at 00am, of every day
     */
    private static final Trigger TRIGGER = newTrigger()
            .withIdentity(NAME, GROUP_NAME)
            .withSchedule(cronSchedule("0 30 0/1 ? * * *"))
            .forJob(NAME, GROUP_NAME)
            .build();

    /**
     *
     * @param jobExecutionContext context of the job
     * @throws JobExecutionException if ServiceException occurs
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            List<Bill> bills = billService.findAll();

            for(Bill bill : bills){
                checkOverdue(bill);
                assignPenalties(bill);
                checkPayment(bill);
                checkBillsWithLockedAccs(bill);
            }

        } catch (ServiceException e) {
            logger.error("Unable to execute bill status check",e);
            throw new JobExecutionException("Unable to execute bill status check",e);
        }
    }

    /**
     * Checks if bill is paid and closes it if is.
     * <p>
     *     A bill is considered paid if there are transfers
     *     in system that have id of this bill assigned to them
     *     and the sum of their values is equal or more than bill's value.
     * </p>
     * @param bill bill to check and update
     * @throws ServiceException
     */
    private void checkPayment(Bill bill) throws ServiceException{
        Criteria<EntityParameters.OperationParam> criteria = new Criteria<>();
        criteria.add(EntityParameters.OperationParam.BILL, new SingularValue<>(bill.getId()));
        criteria.add(EntityParameters.OperationParam.TYPE_ID, new SingularValue<>(6));
        criteria.add(EntityParameters.OperationParam.TYPE_ID, new SingularValue<>(8));
        criteria.add(
                EntityParameters.OperationParam.TARGET_ACCOUNT,
                new SingularValue<>(bill.getPaymentAccountId()));

        Double sum = 0d;
        for(Operation operation : operationService.findByCriteria(criteria)){
            sum+=operation.getValue();
        }

        if(sum>=bill.getValue()){
            bill.setStatusId(BILL_STATUS_CLOSED);
            billService.update(bill);
        }
    }

    /**
     * Checks if bill is overdue and changes its status.
     * <p>
     *     A bill is considered overdue if its due date
     *     has passed.
     * </p>
     * @param bill bill to check and update
     * @throws ServiceException
     */
    private void checkOverdue(Bill bill) throws ServiceException {
        Date today = new Date();

        if(bill.getDueDate() != null
                && today.compareTo(bill.getDueDate())>=0
                && !bill.getStatusId().equals(BILL_STATUS_CLOSED)){
            bill.setStatusId(BILL_STATUS_OVERDUE);
            billService.update(bill);
        }

    }

    /**
     * Assigns penalties to overdue bills if there are penalties.
     * @param bill bill to check and update
     * @throws ServiceException
     */
    private void assignPenalties(Bill bill) throws ServiceException {

        if(bill.getStatusId().equals(BILL_STATUS_OVERDUE)
                && bill.getPenaltyId() != null){
            Penalty penalty = penaltyService.findById(bill.getPenaltyId());
            if(penalty.getStatusId().equals(PENALTY_STATUS_UNASSIGNED)
                && penalty.getUserId().equals(bill.getUserId())){
                penalty.setStatusId(PENALTY_STATUS_PENDING);
            }
        }

    }

    /**
     * If bill's payment account is locked and thus cannot accept payments,
     * this method tries to fix it.
     * <p>
     *     Steps of fixing:
     *     <ul>
     *         <li> Assigns new payment account out of all
     *         active accounts that belong to the bearer of a bill.</li>
     *         <li> If no such accounts are found and bill is not overdue,
     *         postpones it by a set margin.</li>
     *         <li> If no such accounts are found and bill is overdue,
     *         payment account changes to bank's own account.</li>
     *         <li> If bill has no due date, it's ignored.</li>
     *     </ul>
     * </p>
     * @param bill Bill to check and update.
     * @throws ServiceException
     */
    private void checkBillsWithLockedAccs(Bill bill) throws ServiceException{
        Account paymentAccount = accountService.findById(bill.getPaymentAccountId());

        if(paymentAccount.getStatusId().equals(DBMetadata.ACCOUNT_STATUS_BLOCKED)){

            List<Account> accountOfUser = accountService.findByUserId(bill.getBearerId());
            accountOfUser.removeIf(account -> account.getStatusId().equals(ACC_STATUS_LOCKED)
                    || account.getStatusId().equals(ACC_STATUS_PENDING));

            if(accountOfUser.size()>0){
                bill.setPaymentAccountId(accountOfUser.get(0).getId());
                billService.update(bill);
                return;
            }

            if(bill.getDueDate() != null
                    && !bill.getStatusId().equals(DBMetadata.BILL_STATUS_OVERDUE)){
                bill.setDueDate(new Date(bill.getDueDate().getTime()+DELAY_TIME));
                billService.update(bill);
                return;
            }

            if(bill.getStatusId().equals(DBMetadata.BILL_STATUS_OVERDUE)){
                bill.setPaymentAccountId(DBMetadata.BANK_ACCOUNT_ID);
                billService.update(bill);
                Penalty penalty = penaltyService.findById(bill.getPenaltyId());

                if(penalty != null){
                    penalty.setPaymentAccountId(DBMetadata.BANK_ACCOUNT_ID);
                    penaltyService.update(penalty);
                }
            }
        }

    }

    public static JobDetail getDetail(){
        return DETAIL;
    }

    public static Trigger getTrigger(){
        return TRIGGER;
    }

}
