package by.epam.baranovsky.banking.service.scheduler.jobs;

import by.epam.baranovsky.banking.entity.BankingCard;
import by.epam.baranovsky.banking.service.exception.ServiceException;
import org.quartz.*;

import java.util.Date;
import java.util.List;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class CardExpirationJob extends AbstractJob{

    private static final String NAME = "cardExpiration";
    private static final JobDetail DETAIL = JobBuilder.newJob(AccountAccrual.class)
            .withIdentity(NAME, GROUP_NAME)
            .build();
    private static final Trigger TRIGGER = newTrigger()
            .withIdentity(NAME, GROUP_NAME)
            .withSchedule(cronSchedule("0 0 20 ? * * *"))
            .forJob(NAME, GROUP_NAME)
            .build();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            List<BankingCard> cards = bankCardService.findAll();

            for(BankingCard card : cards){
                Date today = new Date();
                if(card.getExpirationDate().compareTo(today) < 0){
                    card.setStatusId(CARD_STATUS_EXPIRED);
                    bankCardService.update(card);
                }
            }

        } catch (ServiceException e) {
            logger.error("Unable to execute card expiration check",e);
            throw new JobExecutionException("Unable to execute card expiration check",e);
        }
    }

    public static JobDetail getDetail(){
        return DETAIL;
    }

    public static Trigger getTrigger(){
        return TRIGGER;
    }
}
