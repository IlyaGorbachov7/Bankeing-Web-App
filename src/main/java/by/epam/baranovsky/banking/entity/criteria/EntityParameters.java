package by.epam.baranovsky.banking.entity.criteria;

import by.epam.baranovsky.banking.constant.DBMetadata;

public class EntityParameters {

    public enum OperationParam implements EntityEnum{
        VALUE(DBMetadata.OPERATIONS_VALUE),
        ACCOUNT(DBMetadata.OPERATIONS_ACC_ID),
        TARGET_ACCOUNT(DBMetadata.OPERATIONS_TARGET_ACC_ID),
        TYPE_ID(DBMetadata.OPERATIONS_TYPE_ID),
        TYPE_NAME(DBMetadata.OPERATION_TYPES_NAME),
        CARD(DBMetadata.OPERATIONS_CARD_ID),
        TARGET_CARD(DBMetadata.OPERATIONS_TARGET_CARD_ID),
        BILL(DBMetadata.OPERATIONS_BILL_ID),
        PENALTY(DBMetadata.OPERATIONS_PENALTY_ID);

        private final String column;

        OperationParam(String column) {
            this.column=column;
        }

        @Override
        public String getColumn() {
            return column;
        }
    }

    public enum BillParam implements EntityEnum{
        VALUE(DBMetadata.BILLS_VALUE),
        ISSUE_DATE(DBMetadata.BILLS_ISSUE_DATE),
        DUE_DATE(DBMetadata.BILLS_DUE_DATE),
        USER(DBMetadata.BILLS_USER_ID),
        PAYMENT_ACC(DBMetadata.BILLS_PAYMENT_ACC_ID),
        STATUS_ID(DBMetadata.BILLS_STATUS_ID),
        STATUS_NAME(DBMetadata.BILL_STATUS_NAME),
        PENALTY(DBMetadata.BILLS_PENALTY_ID),
        LOAN(DBMetadata.BILLS_LOAN_ID);

        private final String column;

        BillParam(String column) {
            this.column=column;
        }

        @Override
        public String getColumn() {
            return column;
        }
    }

    public enum LoanParams implements EntityEnum{
        SINGLE_PAYMENT_VALUE(DBMetadata.LOANS_SINGLE_PAYMENT_VALUE),
        STARTING_VALUE(DBMetadata.LOANS_STARTING_VALUE),
        TOTAL_PAYMENT(DBMetadata.LOANS_TOTAL_VALUE),
        YEARLY_INTEREST(DBMetadata.LOANS_INTEREST),
        ISSUE_DATE(DBMetadata.LOANS_ISSUE_DATE),
        DUE_DATE(DBMetadata.LOANS_DUE_DATE),
        USER(DBMetadata.LOANS_USER_ID),
        STATUS(DBMetadata.LOANS_STATUS_ID);

        private final String column;

        LoanParams(String column) {
            this.column=column;
        }

        @Override
        public String getColumn() {
            return column;
        }
    }

    public enum PenaltyParams implements EntityEnum{
        VALUE(DBMetadata.PENALTIES_VALUE),
        NOTICE(DBMetadata.PENALTIES_NOTICE),
        PAYMENT_ACC(DBMetadata.PENALTIES_PAYMENT_ACC_ID),
        TYPE_ID(DBMetadata.PENALTIES_TYPE_ID),
        TYPE_NAME(DBMetadata.PENALTY_TYPE_NAME),
        USER(DBMetadata.PENALTIES_USER_ID),
        STATUS_ID(DBMetadata.PENALTIES_STATUS_ID),
        STATUS_NAME(DBMetadata.PENALTY_STATUS_NAME);

        private final String column;

        PenaltyParams(String column) {
            this.column=column;
        }

        @Override
        public String getColumn() {
            return column;
        }
    }

    public enum UserParams implements EntityEnum{
        EMAIL(DBMetadata.USERS_EMAIL),
        PASSWORD(DBMetadata.USERS_PASSWORD),
        SURNAME(DBMetadata.USERS_SURNAME),
        NAME(DBMetadata.USERS_NAME),
        PATRONYMIC(DBMetadata.USERS_PATRONYMIC),
        PASSPORT_SERIES(DBMetadata.USERS_PASS_SERIES),
        PASSPORT_NUMBER(DBMetadata.USERS_PASS_NUMBER),
        BIRTHDATE(DBMetadata.USERS_BIRTHDATE),
        LAST_LOGIN(DBMetadata.USERS_LAST_LOGIN),
        DATE_CREATED(DBMetadata.USERS_DATE_CREATED),
        ROLE_ID(DBMetadata.USERS_ROLE_ID),
        ROLE_NAME(DBMetadata.USER_ROLES_DESC);

        private final String column;

        UserParams(String column) {
            this.column=column;
        }

        @Override
        public String getColumn() {
            return column;
        }
    }

}

interface EntityEnum{
    String getColumn();
}
