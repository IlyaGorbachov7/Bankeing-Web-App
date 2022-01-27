package by.epam.baranovsky.banking.controller.constant;

public class RequestAttributeNames {
    private RequestAttributeNames(){}

    public static final String USER_DATA = "USER_DATA";
    public static final String OPERATIONS_DATA = "OPERATIONS_DATA";
    public static final String ERROR_MSG = "ERROR_MSG";

    public static final String UNLOCKED_ACCS = "UNLOCKED_ACCS";
    public static final String BLOCKED_ACCS = "BLOCKED_ACCS";
    public static final String SUSPENDED_ACCS = "SUSPENDED_ACCS";
    public static final String PENDING_ACCS_COUNT = "PENDING_ACCS_COUNT";
    public static final String ACCOUNT_USERS_INFO = "ACCOUNT_USERS_INFO";
    public static final String ACCOUNT_CARDS_INFO = "ACCOUNT_CARDS_INFO";
    public static final String ACCOUNT_DATA = "ACCOUNT_DATA";

    public static final String USER_CARDS="USER_CARDS";
    public static final String USER_ACCOUNTS="USER_ACCOUNTS";

    public static final String CARD_DATA="CARD_DATA";
    public static final String CARD_USER="CARD_USER";
    public static final String CARD_ACCOUNT="CARD_ACCOUNT";
    public static final String CARD_OVERDRAFTED="CARD_OVERDRAFTED";
    public static final String PREVIOUS_PAGE="PREV_PAGE";

    public static final String BILL_ID="BILL_ID";
    public static final String PENALTY_ID="PENALTY_ID";

    public static final String TARGET_ACC="TARGET_ACC";
    public static final String TARGET_CARD="TARGET_CARD";
    public static final String OWN_CARD="OWN_CARD";
    public static final String OWN_ACC="OWN_ACC";
    public static final String TRANSFER_VALUE="transfer_value";
    public static final String TRANSFER_VALUE_COMMISSIONED="transfer_value_with_commission";
}
