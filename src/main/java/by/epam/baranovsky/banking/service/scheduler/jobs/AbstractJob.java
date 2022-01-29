package by.epam.baranovsky.banking.service.scheduler.jobs;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.service.*;
import by.epam.baranovsky.banking.service.impl.*;
import org.apache.log4j.Logger;
import org.quartz.*;


public abstract class AbstractJob implements Job {

    protected static final String GROUP_NAME = "scheduledJobs";
    protected static final Integer BILL_STATUS_PENDING = DBMetadata.BILL_STATUS_PENDING;
    protected static final Integer BILL_STATUS_CLOSED = DBMetadata.BILL_STATUS_CLOSED;
    protected static final Integer BILL_STATUS_OVERDUE = DBMetadata.BILL_STATUS_OVERDUE;

    protected static final Integer PENALTY_STATUS_PENDING = DBMetadata.PENALTY_STATUS_PENDING;
    protected static final Integer PENALTY_STATUS_UNASSIGNED = DBMetadata.PENALTY_STATUS_UNASSIGNED;
    protected static final Integer PENALTY_STATUS_CLOSED = DBMetadata.PENALTY_STATUS_CLOSED;
    protected static final Integer PENALTY_STATUS_INFLICTED = DBMetadata.PENALTY_STATUS_INFLICTED;

    protected static final Integer PENALTY_TYPE_ACCS_SUSP = DBMetadata.PENALTY_TYPE_ACCS_SUSP;
    protected static final Integer PENALTY_TYPE_ACCS_LOCK= DBMetadata.PENALTY_TYPE_ACCS_LOCK;
    protected static final Integer PENALTY_TYPE_CARDS_LOCK= DBMetadata.PENALTY_TYPE_CARDS_LOCK;
    protected static final Integer PENALTY_TYPE_FEE = DBMetadata.PENALTY_TYPE_FEE;
    protected static final Integer PENALTY_TYPE_LAWSUIT = DBMetadata.PENALTY_TYPE_LAWSUIT;

    protected static final Integer LOAN_STATUS_CLOSED = DBMetadata.LOAN_STATUS_CLOSED;
    protected static final Integer LOAN_STATUS_PENDING  = DBMetadata.LOAN_STATUS_PENDING;
    protected static final Integer LOAN_STATUS_OVERDUE = DBMetadata.LOAN_STATUS_OVERDUE;

    protected static final Integer ACC_STATUS_LOCKED=DBMetadata.ACCOUNT_STATUS_BLOCKED;
    protected static final Integer ACC_STATUS_SUSPENDED=DBMetadata.ACCOUNT_STATUS_SUSPENDED;
    protected static final Integer ACC_STATUS_OPERATING=DBMetadata.ACCOUNT_STATUS_UNLOCKED;
    protected static final Integer ACC_STATUS_PENDING=DBMetadata.ACCOUNT_STATUS_PENDING;

    protected static final Integer CARD_STATUS_UNLOCKED=DBMetadata.CARD_STATUS_UNLOCKED;
    protected static final Integer CARD_STATUS_LOCKED=DBMetadata.CARD_STATUS_LOCKED;
    protected static final Integer CARD_STATUS_EXPIRED=DBMetadata.CARD_STATUS_EXPIRED;

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
