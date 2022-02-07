package by.epam.baranovsky.banking.entity.criteria;

import by.epam.baranovsky.banking.constant.DBMetadata;

/**
 * Utility class that contains enumerations of entity parameters.
 */
public class EntityParameters {

    /**
     * Enumeration that contains parameters for Operation entity.
     * @see by.epam.baranovsky.banking.entity.Operation
     */
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

    /**
     * Enumeration that contains parameters for Bill entity.
     * @see by.epam.baranovsky.banking.entity.Bill
     */
    public enum BillParam implements EntityEnum{
        VALUE(DBMetadata.BILLS_VALUE),
        ISSUE_DATE(DBMetadata.BILLS_ISSUE_DATE),
        DUE_DATE(DBMetadata.BILLS_DUE_DATE),
        USER(DBMetadata.BILLS_USER_ID),
        PAYMENT_ACC(DBMetadata.BILLS_PAYMENT_ACC_ID),
        STATUS_ID(DBMetadata.BILLS_STATUS_ID),
        STATUS_NAME(DBMetadata.BILL_STATUS_NAME),
        PENALTY(DBMetadata.BILLS_PENALTY_ID),
        LOAN(DBMetadata.BILLS_LOAN_ID),
        NOTICE(DBMetadata.BILLS_NOTICE),
        BEARER(DBMetadata.BILLS_BEARER_ID);

        private final String column;

        BillParam(String column) {
            this.column=column;
        }

        @Override
        public String getColumn() {
            return column;
        }
    }

    /**
     * Enumeration that contains parameters for Loan entity.
     * @see by.epam.baranovsky.banking.entity.Loan
     */
    public enum LoanParams implements EntityEnum{
        SINGLE_PAYMENT_VALUE(DBMetadata.LOANS_SINGLE_PAYMENT_VALUE),
        STARTING_VALUE(DBMetadata.LOANS_STARTING_VALUE),
        TOTAL_PAYMENT(DBMetadata.LOANS_TOTAL_VALUE),
        YEARLY_INTEREST(DBMetadata.LOANS_INTEREST),
        ISSUE_DATE(DBMetadata.LOANS_ISSUE_DATE),
        DUE_DATE(DBMetadata.LOANS_DUE_DATE),
        USER(DBMetadata.LOANS_USER_ID),
        STATUS(DBMetadata.LOANS_STATUS_ID),
        CARD_ID(DBMetadata.LOANS_CARD_ID),
        ACC_ID(DBMetadata.LOANS_ACCOUNT_ID);

        private final String column;

        LoanParams(String column) {
            this.column=column;
        }

        @Override
        public String getColumn() {
            return column;
        }
    }

    /**
     * Enumeration that contains parameters for Penalty entity.
     * @see by.epam.baranovsky.banking.entity.Penalty
     */
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

    /**
     * Enumeration that contains parameters for User entity.
     * @see by.epam.baranovsky.banking.entity.User
     */
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

    private EntityParameters(){}

}

/**
 * Interface that is used to generalize enumerations
 * that contain parameters for DB entities.
 *
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
interface EntityEnum{
    /**
     * Returns a name of a column in database that is associated with given parameter name.
     * @return name of a column in database.
     */
    String getColumn();
}
