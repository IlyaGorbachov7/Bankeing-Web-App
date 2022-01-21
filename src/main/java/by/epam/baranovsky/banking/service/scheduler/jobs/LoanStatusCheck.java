package by.epam.baranovsky.banking.service.scheduler.jobs;

import by.epam.baranovsky.banking.entity.Bill;
import by.epam.baranovsky.banking.entity.Loan;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.entity.criteria.SingularValue;
import by.epam.baranovsky.banking.service.exception.ServiceException;
import org.quartz.*;

import java.util.Date;
import java.util.List;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class LoanStatusCheck extends AbstractJob{

    private static final String NAME = "loanStatusCheck";
    private static final JobDetail DETAIL = JobBuilder.newJob(AccountAccrual.class)
            .withIdentity(NAME, GROUP_NAME)
            .build();
    private static final Trigger TRIGGER = newTrigger()
            .withIdentity(NAME, GROUP_NAME)
            .withSchedule(cronSchedule("0 45 23 ? * * *"))
            .forJob(NAME, GROUP_NAME)
            .build();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        try {
            List<Loan> loans = loanService.findAll();

            for(Loan loan : loans){
                if(!loan.getStatusId().equals(LOAN_STATUS_CLOSED)){
                    checkPayment(loan);
                    checkOverdue(loan);
                }
            }

        } catch (ServiceException e) {
            logger.error("Unable to execute loan status check",e);
            throw new JobExecutionException("Unable to execute loan status check",e);
        }


    }

    private void checkPayment(Loan loan) throws ServiceException {

        Criteria<EntityParameters.BillParam> criteria = new Criteria<>();
        criteria.add(EntityParameters.BillParam.LOAN, new SingularValue<>(loan.getId()));
        criteria.add(EntityParameters.BillParam.VALUE, new SingularValue<>(loan.getSinglePaymentValue()));
        criteria.add(EntityParameters.BillParam.STATUS_ID, new SingularValue<>(BILL_STATUS_CLOSED));

        List<Bill> bills = billService.findByCriteria(criteria);

        Double sum = 0d;
        for(Bill bill : bills){
            sum+=bill.getValue();
        }

        if(sum>=loan.getTotalPaymentValue()){
            loan.setStatusId(LOAN_STATUS_CLOSED);
            loanService.update(loan);
        }
    }

    private void checkOverdue(Loan loan) throws ServiceException {
        Date today = new Date();
        if(loan.getDueDate().compareTo(today)>0
                && !loan.getStatusId().equals(LOAN_STATUS_OVERDUE)){
            loan.setStatusId(LOAN_STATUS_OVERDUE);
            loanService.update(loan);
        }
    }

    public static JobDetail getDetail(){
        return DETAIL;
    }

    public static Trigger getTrigger(){
        return TRIGGER;
    }
}
