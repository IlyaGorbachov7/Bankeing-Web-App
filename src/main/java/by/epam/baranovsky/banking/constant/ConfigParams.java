package by.epam.baranovsky.banking.constant;

/**
 * Utility class that stores constant strings
 * that represent config parameters' name.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public final class ConfigParams {

    public static final String LOAN_PENALTY="loan.overdue.penalty";
    public static final String TRANSFER_COMMISSION_RATE="transfer.commission.percent";
    public static final String ACCOUNT_REQUESTS_MAX="account.requests.max";
    public static final String BILLS_REQUESTS_MAX="bills.requests.max";
    public static final String CARDS_PER_USER="max.cards.per.user";

    public static final String LOANS_MAX_TIME ="loans.max.time";
    public static final String LOANS_MIN_TIME ="loans.min.time";
    public static final String LOANS_MAX_INTEREST ="loans.max.interest";
    public static final String LOANS_MIN_INTEREST ="loans.min.interest";
    public static final String LOAN_MIN_VALUE="loans.min.value";
    public static final String LOANS_MAX_LOANS="loans.max.active.loans";
    public static final String BILL_MIN_LENGTH="bills.min.length.months";
    public static final String BILL_HANGING_TIME_LIMIT="bills.hanging.limit.months";
    public static final String BILL_DELAY_TIME="bills.locked.acc.delay.time.days";


    private ConfigParams(){}
}
