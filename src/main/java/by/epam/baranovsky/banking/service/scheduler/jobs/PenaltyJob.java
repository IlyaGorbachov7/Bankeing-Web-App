package by.epam.baranovsky.banking.service.scheduler.jobs;

import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.entity.BankingCard;
import by.epam.baranovsky.banking.entity.Bill;
import by.epam.baranovsky.banking.entity.Penalty;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.entity.criteria.SingularValue;
import by.epam.baranovsky.banking.service.exception.ServiceException;
import org.quartz.*;

import java.util.List;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class PenaltyJob extends AbstractJob {

    private static final String NAME = "penaltyAccounting";
    private static final JobDetail DETAIL = JobBuilder.newJob(AccountAccrual.class)
            .withIdentity(NAME, GROUP_NAME)
            .build();
    private static final Trigger TRIGGER = newTrigger()
            .withIdentity(NAME, GROUP_NAME)
            .withSchedule(cronSchedule("0 0 1 ? * * *"))
            .forJob(NAME, GROUP_NAME)
            .build();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        try {
            List<Penalty> penalties = penaltyService.findAll();

            for (Penalty penalty : penalties) {

                if(penalty.getStatusId().equals(PENALTY_STATUS_INFLICTED)){
                    liftPenaltyIfAble(penalty);
                }

                if(penalty.getStatusId().equals(PENALTY_STATUS_PENDING)){
                    inflictPenalty(penalty);
                }
            }

        } catch (ServiceException e) {
            logger.error("Unable to execute penalty check", e);
            throw new JobExecutionException("Unable to execute penalty check", e);
        }
    }

    public static JobDetail getDetail() {
        return DETAIL;
    }

    public static Trigger getTrigger() {
        return TRIGGER;
    }

    private void liftPenaltyIfAble(Penalty penalty) throws ServiceException {
        PenaltyLiftCommand command
                = PenaltyCommandEnum.getLiftCommand(penalty.getTypeId());
        if (command != null) {
            command.lift(penalty);
        }
    }

    private void inflictPenalty(Penalty penalty) throws ServiceException {
        PenaltyInflictCommand command
                = PenaltyCommandEnum.getInflictCommand(penalty.getTypeId());
        if (command != null) {
            command.inflict(penalty);
        }
    }

    private interface PenaltyLiftCommand {
        void lift(Penalty penalty) throws ServiceException;
    }

    private interface PenaltyInflictCommand {
        void inflict(Penalty penalty) throws ServiceException;
    }

    private enum PenaltyCommandEnum {

        FEE(
                PENALTY_TYPE_FEE,
                penalty -> {
                    Criteria<EntityParameters.BillParam> criteria = new Criteria<>();
                    criteria.add(
                            EntityParameters.BillParam.PENALTY,
                            new SingularValue<>(penalty.getId()));

                    Double sum = 0d;
                    for (Bill bill : billService.findByCriteria(criteria)) {
                        sum += bill.getValue();
                    }
                    if (sum >= penalty.getValue()) {
                        penalty.setStatusId(PENALTY_STATUS_CLOSED);
                        penaltyService.update(penalty);
                    }
                },
                penalty -> {
                    penalty.setStatusId(PENALTY_STATUS_INFLICTED);
                    penaltyService.update(penalty);}),
        ACCS_LOCK(
                PENALTY_TYPE_ACCS_LOCK,
                penalty ->{},
                penalty ->{
                    List<Account> accountList
                            = accountService.findByUserId(penalty.getUserId());
                    for(Account account : accountList){
                        account.setStatusId(ACC_STATUS_LOCKED);
                        account.setUsers(null);
                        accountService.update(account);
                    }
                    penalty.setStatusId(PENALTY_STATUS_INFLICTED);
                    penaltyService.update(penalty);
                }),
        ACCS_SUSP(
                PENALTY_TYPE_ACCS_SUSP,
                penalty ->{
                    Criteria<EntityParameters.BillParam> billsCriteria = new Criteria<>();
                    billsCriteria.add(
                            EntityParameters.BillParam.USER,
                            new SingularValue<>(penalty.getUserId()));

                    Criteria<EntityParameters.PenaltyParams> penaltiesCriteria = new Criteria<>();
                    penaltiesCriteria.add(EntityParameters.PenaltyParams.USER,
                            new SingularValue<>(penalty.getUserId()));
                    penaltiesCriteria.add(EntityParameters.PenaltyParams.TYPE_ID,
                            new SingularValue<>(PENALTY_TYPE_FEE));
                    for(Bill bill : billService.findByCriteria(billsCriteria)){
                        if(!bill.getStatusId().equals(BILL_STATUS_CLOSED)){
                            return;
                        }
                    }
                    for(Penalty userPenalty : penaltyService.findByCriteria(penaltiesCriteria)){
                        if(!userPenalty.getStatusId().equals(PENALTY_STATUS_CLOSED)){
                            return;
                        }
                    }

                    List<Account> accountList
                            = accountService.findByUserId(penalty.getUserId());
                    for(Account account : accountList){
                        account.setStatusId(ACC_STATUS_OPERATING);
                        account.setUsers(null);
                        accountService.update(account);
                    }

                    penalty.setStatusId(PENALTY_STATUS_CLOSED);
                    penaltyService.update(penalty);
                },
                penalty ->{
                    List<Account> accountList
                            = accountService.findByUserId(penalty.getUserId());
                    for(Account account : accountList){
                        account.setStatusId(ACC_STATUS_SUSPENDED);
                        account.setUsers(null);
                        accountService.update(account);
                    }
                    penalty.setStatusId(PENALTY_STATUS_INFLICTED);
                    penaltyService.update(penalty);
                }),
        CARDS_LOCK(PENALTY_TYPE_CARDS_LOCK,
                penalty ->{
                    Criteria<EntityParameters.BillParam> billsCriteria = new Criteria<>();
                    billsCriteria.add(
                            EntityParameters.BillParam.USER,
                            new SingularValue<>(penalty.getUserId()));

                    Criteria<EntityParameters.PenaltyParams> penaltiesCriteria = new Criteria<>();
                    penaltiesCriteria.add(EntityParameters.PenaltyParams.USER,
                            new SingularValue<>(penalty.getUserId()));
                    penaltiesCriteria.add(EntityParameters.PenaltyParams.TYPE_ID,
                            new SingularValue<>(PENALTY_TYPE_FEE));

                    for(Bill bill : billService.findByCriteria(billsCriteria)){
                        if(!bill.getStatusId().equals(BILL_STATUS_CLOSED)){
                            return;
                        }
                    }
                    for(Penalty userPenalty : penaltyService.findByCriteria(penaltiesCriteria)){
                        if(!userPenalty.getStatusId().equals(PENALTY_STATUS_CLOSED)){
                            return;
                        }
                    }

                    List<BankingCard> cards
                            = bankCardService.findByUser(penalty.getUserId());
                    for(BankingCard card : cards){
                        card.setStatusId(CARD_STATUS_UNLOCKED);
                        bankCardService.update(card);
                    }
                    penalty.setStatusId(PENALTY_STATUS_CLOSED);
                    penaltyService.update(penalty);
                },
                penalty ->{
                    List<BankingCard> cardsList = bankCardService.findByUser(penalty.getUserId());
                    for(BankingCard card : cardsList){
                        if(!card.getStatusId().equals(CARD_STATUS_EXPIRED)){
                            card.setStatusId(CARD_STATUS_LOCKED);
                            bankCardService.update(card);
                        }
                    }
                    penalty.setStatusId(PENALTY_STATUS_INFLICTED);
                    penaltyService.update(penalty);
                }),
        LAWSUIT(PENALTY_TYPE_LAWSUIT,
                penalty -> {},
                penalty -> {});

        Integer typeId;
        PenaltyLiftCommand liftCommand;
        PenaltyInflictCommand inflictCommand;

        PenaltyCommandEnum(Integer penaltyType,
                           PenaltyLiftCommand command,
                           PenaltyInflictCommand inflictCommand) {
            this.typeId = penaltyType;
            this.liftCommand = command;
            this.inflictCommand = inflictCommand;
        }

        static PenaltyLiftCommand getLiftCommand(Integer id) {
            for (PenaltyCommandEnum entry : PenaltyCommandEnum.values()) {
                if (entry.typeId.equals(id)) {
                    return entry.liftCommand;
                }
            }
            return null;
        }

        static PenaltyInflictCommand getInflictCommand(Integer id) {
            for (PenaltyCommandEnum entry : PenaltyCommandEnum.values()) {
                if (entry.typeId.equals(id)) {
                    return entry.inflictCommand;
                }
            }
            return null;
        }
    }
}
