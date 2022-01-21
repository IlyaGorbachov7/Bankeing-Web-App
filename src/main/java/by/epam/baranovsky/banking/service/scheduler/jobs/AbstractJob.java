package by.epam.baranovsky.banking.service.scheduler.jobs;

import by.epam.baranovsky.banking.service.*;
import by.epam.baranovsky.banking.service.impl.*;
import org.apache.log4j.Logger;
import org.quartz.*;


public abstract class AbstractJob implements Job {

    protected static final String GROUP_NAME = "scheduledJobs";
    protected static final Integer BILL_STATUS_PENDING = 1;
    protected static final Integer BILL_STATUS_CLOSED = 2;
    protected static final Integer BILL_STATUS_OVERDUE = 3;

    protected static final Integer PENALTY_STATUS_PENDING = 1;
    protected static final Integer PENALTY_STATUS_UNASSIGNED = 2;
    protected static final Integer PENALTY_STATUS_CLOSED = 3;
    protected static final Integer PENALTY_STATUS_INFLICTED = 4;

    protected static final Integer PENALTY_TYPE_ACCS_SUSP = 1;
    protected static final Integer PENALTY_TYPE_ACCS_LOCK= 2;
    protected static final Integer PENALTY_TYPE_CARDS_LOCK= 3;
    protected static final Integer PENALTY_TYPE_FEE = 4;
    protected static final Integer PENALTY_TYPE_LAWSUIT = 5;

    protected static final Integer LOAN_STATUS_CLOSED = 1;
    protected static final Integer LOAN_STATUS_PENDING  = 2;
    protected static final Integer LOAN_STATUS_OVERDUE = 3;

    protected static final Integer ACC_STATUS_LOCKED=1;
    protected static final Integer ACC_STATUS_SUSPENDED=2;
    protected static final Integer ACC_STATUS_OPERATING=3;
    protected static final Integer ACC_STATUS_PENDING=4;

    protected static final Integer CARD_STATUS_UNLOCKED=1;
    protected static final Integer CARD_STATUS_LOCKED=2;
    protected static final Integer CARD_STATUS_EXPIRED=3;

    protected static final Integer BANK_ACCOUNT_ID=1;

    public static final Logger logger = Logger.getLogger(AbstractJob.class);

    protected static final BillService billService = BillServiceImpl.getInstance();
    protected static final LoanService loanService = LoanServiceImpl.getInstance();
    protected static final AccountService accountService = AccountServiceImpl.getInstance();
    protected static final OperationService operationService = OperationServiceImpl.getInstance();
    protected static final PenaltyService penaltyService = PenaltyServiceImpl.getInstance();
    protected static final BankCardService bankCardService = BankCardServiceImpl.getInstance();


    public BillService getBillService() {
        return billService;
    }

    public LoanService getLoanService() {
        return loanService;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public OperationService getOperationService() {
        return operationService;
    }

    public PenaltyService getPenaltyService() {
        return penaltyService;
    }
}
