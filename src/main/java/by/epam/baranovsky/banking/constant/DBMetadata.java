package by.epam.baranovsky.banking.constant;

public class DBMetadata {

    public static final String USERS_TABLE = "users";
    public static final String USERS_ID = "id_users";
    public static final String USERS_EMAIL ="email";
    public static final String USERS_PASSWORD ="password";
    public static final String USERS_SURNAME ="surname";
    public static final String USERS_NAME ="name";
    public static final String USERS_PATRONYMIC ="patronymic";
    public static final String USERS_PASS_SERIES ="passport_series";
    public static final String USERS_PASS_NUMBER ="passport_number";
    public static final String USERS_BIRTHDATE ="birthdate";
    public static final String USERS_LAST_LOGIN ="last_login";
    public static final String USERS_DATE_CREATED ="created";
    public static final String USERS_ROLE_ID ="id_role";

    public static final Integer USER_ROLE_ADMIN= 1;
    public static final Integer USER_ROLE_REGULAR = 2;
    public static final Integer USER_ROLE_EMPLOYEE= 3;
    public static final Integer USER_ROLE_BANNED = 4;

    public static final String USER_ROLES_TABLE = "user_roles";
    public static final String USER_ROLES_ID ="id_roles";
    public static final String USER_ROLES_DESC="description";

    public static final String USERS_HAS_ACCOUNTS_TABLE = "users_has_accounts";
    public static final String USERS_HAS_ACCOUNTS_USER_ID = "id_user";
    public static final String USERS_HAS_ACCOUNTS_ACCOUNT_ID="id_account";

    public static final String ACCOUNTS_TABLE = "accounts";
    public static final String ACCOUNTS_ID = "id_accounts";
    public static final String ACCOUNTS_BALANCE = "balance";
    public static final String ACCOUNTS_NUMBER = "account_number";
    public static final String ACCOUNTS_INTEREST = "yearly_interest_rate";
    public static final String ACCOUNTS_ACCOUNT_STATUS_ID = "id_account_status";

    public static final String ACCOUNT_STATUS_TABLE = "account_status";
    public static final String ACCOUNT_STATUS_ID = "id_status";
    public static final String ACCOUNT_STATUS_DESC = "description";
    public static final Integer ACCOUNT_STATUS_BLOCKED = 1;
    public static final Integer ACCOUNT_STATUS_SUSPENDED = 2;
    public static final Integer ACCOUNT_STATUS_UNLOCKED= 3;
    public static final Integer ACCOUNT_STATUS_PENDING= 4;

    public static final String BANK_CARDS_TABLE = "bank_cards";
    public static final String BANK_CARDS_ID = "id_bank_cards";
    public static final String BANK_CARDS_NUMBER = "credit_card_number";
    public static final String BANK_CARDS_CVC = "cvc_code";
    public static final String BANK_CARDS_PIN = "pin_code";
    public static final String BANK_CARDS_EXPIRATION_DATE = "expiration_date";
    public static final String BANK_CARDS_REGISTRATION_DATE = "registration_date";
    public static final String BANK_CARDS_BALANCE = "balance";
    public static final String BANK_CARDS_OVERDRAFT_MAXIMUM = "overdraft_maximum";
    public static final String BANK_CARDS_OVERDRAFT_INTEREST = "overdraft_interest_rate";
    public static final String BANK_CARDS_USER_ID = "id_user";
    public static final String BANK_CARDS_ACCOUNT_ID = "id_account";
    public static final String BANK_CARDS_TYPE_ID = "id_card_type";
    public static final String BANK_CARDS_STATUS_ID = "id_card_status";

    public static final String CARD_TYPE_TABLE = "card_types";
    public static final String CARD_TYPE_ID = "id_card_type";
    public static final String CARD_TYPE_NAME = "type";

    public static final Integer CARD_TYPE_DEBIT = 1;
    public static final Integer CARD_TYPE_CREDIT = 2;
    public static final Integer CARD_TYPE_OVERDRAFT = 3;

    public static final String CARD_STATUS_TABLE = "card_status";
    public static final String CARD_STATUS_ID = "id_status";
    public static final String CARD_STATUS_NAME="status";

    public static final Integer CARD_STATUS_UNLOCKED = 1;
    public static final Integer CARD_STATUS_LOCKED = 2;
    public static final Integer CARD_STATUS_EXPIRED= 3;
    public static final Integer CARD_STATUS_PENDING= 4;

    public static final String BILLS_TABLE = "bills";
    public static final String BILLS_ID = "id_bills";
    public static final String BILLS_VALUE = "value";
    public static final String BILLS_ISSUE_DATE = "issue_date";
    public static final String BILLS_DUE_DATE = "due_date";
    public static final String BILLS_USER_ID = "id_user";
    public static final String BILLS_PAYMENT_ACC_ID = "id_payment_account";
    public static final String BILLS_STATUS_ID = "id_bill_status";
    public static final String BILLS_PENALTY_ID = "id_penalty";
    public static final String BILLS_LOAN_ID = "id_loans";

    public static final String BILL_STATUS_TABLE= "bill_status";
    public static final String BILL_STATUS_ID="id_bill_status";
    public static final String BILL_STATUS_NAME="status";

    public static final Integer BILL_STATUS_PENDING = 1;
    public static final Integer BILL_STATUS_CLOSED = 2;
    public static final Integer BILL_STATUS_OVERDUE = 3;

    public static final String LOANS_TABLE = "loans";
    public static final String LOANS_ID = "single_payment_value";
    public static final String LOANS_SINGLE_PAYMENT_VALUE = "single_payment_value";
    public static final String LOANS_STARTING_VALUE = "starting_value";
    public static final String LOANS_TOTAL_VALUE = "total_payment_value";
    public static final String LOANS_INTEREST = "yearly_interest_rate";
    public static final String LOANS_ISSUE_DATE = "date_of_issue";
    public static final String LOANS_DUE_DATE = "due_date";
    public static final String LOANS_USER_ID = "users_id_users";
    public static final String LOANS_STATUS_ID = "id_status";
    public static final String LOANS_CARD_ID = "id_card";

    public static final String LOAN_STATUS_TABLE = "loan_status";
    public static final String LOAN_STATUS_ID = "id_loan_status";
    public static final String LOAN_STATUS_NAME = "status_desc";

    public static final Integer LOAN_STATUS_CLOSED = 1;
    public static final Integer LOAN_STATUS_PENDING = 2;
    public static final Integer LOAN_STATUS_OVERDUE = 3;

    public static final String OPERATIONS_TABLE = "operations";
    public static final String OPERATIONS_ID = "id_operations";
    public static final String OPERATIONS_VALUE = "value";
    public static final String OPERATIONS_TYPE_ID = "id_operation_type";
    public static final String OPERATIONS_ACC_ID = "id_account";
    public static final String OPERATIONS_TARGET_ACC_ID = "id_target_account";
    public static final String OPERATIONS_CARD_ID = "id_bank_card";
    public static final String OPERATIONS_TARGET_CARD_ID = "id_target_bank_card";
    public static final String OPERATIONS_BILL_ID = "id_bill";
    public static final String OPERATIONS_PENALTY_ID = "id_penalty";
    public static final String OPERATIONS_DATE = "operation_date";

    public static final Integer OPERATION_TYPE_ACC_LOCK =1;
    public static final Integer OPERATION_TYPE_ACC_SUSP =2;
    public static final Integer OPERATION_TYPE_ACC_UNLOCK =3;
    public static final Integer OPERATION_TYPE_CARD_LOCK =4;
    public static final Integer OPERATION_TYPE_CARD_UNLOCK =5;
    public static final Integer OPERATION_TYPE_TRANSFER_A_A =6;
    public static final Integer OPERATION_TYPE_TRANSFER_A_C =7;
    public static final Integer OPERATION_TYPE_TRANSFER_C_A =8;
    public static final Integer OPERATION_TYPE_TRANSFER_C_C =9;
    public static final Integer OPERATION_TYPE_CARD_EXPIRE =11;
    public static final Integer OPERATION_TYPE_ACCRUAL =13;

    public static final String OPERATION_TYPES_TABLE = "operation_types";
    public static final String OPERATION_TYPES_ID="id_operation";
    public static final String OPERATION_TYPES_NAME="description";

    public static final String PENALTIES_TABLE = "penalties";
    public static final String PENALTIES_ID = "id_penalties";
    public static final String PENALTIES_VALUE = "value";
    public static final String PENALTIES_NOTICE="notice";
    public static final String PENALTIES_PAYMENT_ACC_ID="id_payment_account";
    public static final String PENALTIES_TYPE_ID="id_penalty_type";
    public static final String PENALTIES_USER_ID="id_user";
    public static final String PENALTIES_STATUS_ID="id_status";

    public static final String PENALTY_STATUS_TABLE = "penalty_status";
    public static final String PENALTY_STATUS_ID="id_penalty_status";
    public static final String PENALTY_STATUS_NAME="status";

    public static final Integer PENALTY_STATUS_PENDING = 1;
    public static final Integer PENALTY_STATUS_UNASSIGNED = 2;
    public static final Integer PENALTY_STATUS_CLOSED = 3;
    public static final Integer PENALTY_STATUS_INFLICTED = 4;

    public static final String PENALTY_TYPE_TABLE = "penalty_type";
    public static final String PENALTY_TYPE_ID="id_penalty_type";
    public static final String PENALTY_TYPE_NAME="type";

    public static final Integer PENALTY_TYPE_ACCS_SUSP = 1;
    public static final Integer PENALTY_TYPE_ACCS_LOCK= 2;
    public static final Integer PENALTY_TYPE_CARDS_LOCK= 3;
    public static final Integer PENALTY_TYPE_FEE = 4;
    public static final Integer PENALTY_TYPE_LAWSUIT = 5;
    private DBMetadata() {}
}
