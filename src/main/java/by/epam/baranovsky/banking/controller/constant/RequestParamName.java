package by.epam.baranovsky.banking.controller.constant;

public class RequestParamName {
    public static final String CONTROLLER="controller";
    public static final String COMMAND_NAME="command";
    public static final String LOCALE="locale";

    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String CONF_PASSWORD = "conf_password";
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    public static final String PATRONYMIC = "patronymic";
    public static final String PASSPORT_SERIES = "passport_series";
    public static final String PASSPORT_NUMBER = "passport_number";
    public static final String BIRTHDATE = "birthdate";

    public static final String ACCOUNT_COUNTRY = "country";
    public static final String ACCOUNT_ID = "account_id";
    public static final String ACCOUNT_NEW_STATUS = "account_new_status";
    public static final String ACCOUNT_NUMBER = "account_number";

    public static final String CARD_ID = "card_id";
    public static final String CARD_NEW_USER_ID= "new_user_id";
    public static final String CARD_NEW_STATUS= "card_new_status";


    public static final String TRANSFER_TARGET_ACC="target_account_id";
    public static final String TRANSFER_TARGET_ACC_NUMBER="target_account_number";
    public static final String TRANSFER_TARGET_CARD="target_card_id";
    public static final String TRANSFER_TARGET_CARD_NUMBER="target_card_number";
    public static final String TRANSFER_TARGET_CARD_EXPIRATION="target_card_expiration";
    public static final String TRANSFER_VALUE="transfer_value";


    public static final String BILL_ID="bill_id";
    public static final String PENALTY_ID="penalty_id";
    public static final String PENALTY_TYPE_ID="penalty_type_id";
    public static final String PENALTY_NOTICE="penalty_notice";
    public static final String PENALTY_VALUE="penalty_value";


    public static final String ID_CHECKED_USER="checked_user_id";
    public static final String ACC_NEW_INTEREST="new_interest_rate";
    public static final String USER_NEW_ROLE="new_role_id";

    public static final String BILL_NOTICE="bill_notice";
    public static final String BILL_VALUE="bill_value";
    public static final String BILL_DUE_DATE="bill_due_date";

    public static final String LOAN_STARTING="loan_starting_value";
    public static final String LOAN_MONTHS="loan_length";

    private RequestParamName() {}
}
