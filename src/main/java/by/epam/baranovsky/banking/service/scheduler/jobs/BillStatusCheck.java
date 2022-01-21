package by.epam.baranovsky.banking.service.scheduler.jobs;

import by.epam.baranovsky.banking.entity.Bill;
import by.epam.baranovsky.banking.entity.Operation;
import by.epam.baranovsky.banking.entity.Penalty;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.entity.criteria.SingularValue;
import by.epam.baranovsky.banking.service.exception.ServiceException;
import org.quartz.*;

import java.util.Date;
import java.util.List;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class BillStatusCheck extends AbstractJob {

    private static final String NAME = "billStatusCheck";
    private static final JobDetail DETAIL = JobBuilder.newJob(AccountAccrual.class)
            .withIdentity(NAME, GROUP_NAME)
            .build();
    private static final Trigger TRIGGER = newTrigger()
            .withIdentity(NAME, GROUP_NAME)
            .withSchedule(cronSchedule(" 0 30 0/1 ? * * *"))
            .forJob(NAME, GROUP_NAME)
            .build();


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            List<Bill> bills = billService.findAll();

            for(Bill bill : bills){
                checkOverdue(bill);
                assignPenalties(bill);
                checkPayment(bill);
            }

        } catch (ServiceException e) {
            logger.error("Unable to execute bill status check",e);
            throw new JobExecutionException("Unable to execute bill status check",e);
        }
    }

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

    private void checkOverdue(Bill bill) throws ServiceException {
        Date today = new Date();

        if(bill.getDueDate() != null
                && today.compareTo(bill.getDueDate())>=0
                && !bill.getStatusId().equals(BILL_STATUS_CLOSED)){
            bill.setStatusId(BILL_STATUS_OVERDUE);
            billService.update(bill);
        }

    }

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

    public static JobDetail getDetail(){
        return DETAIL;
    }

    public static Trigger getTrigger(){
        return TRIGGER;
    }

}
