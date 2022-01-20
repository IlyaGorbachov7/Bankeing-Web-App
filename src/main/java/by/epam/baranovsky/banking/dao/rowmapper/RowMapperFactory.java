package by.epam.baranovsky.banking.dao.rowmapper;

import by.epam.baranovsky.banking.dao.rowmapper.impl.*;

public class RowMapperFactory {

    private static final UserRowMapper USER_ROW_MAPPER = new UserRowMapper();
    private static final AccountRowMapper ACCOUNT_ROW_MAPPER = new AccountRowMapper();
    private static final BillRowMapper BILL_ROW_MAPPER = new BillRowMapper();
    private static final CardRowMapper CARD_ROW_MAPPER = new CardRowMapper();
    private static final LoanRowMapper LOAN_ROW_MAPPER = new LoanRowMapper();
    private static final PenaltyRowMapper PENALTY_ROW_MAPPER = new PenaltyRowMapper();
    private static final OperationRowMapper OPERATION_ROW_MAPPER = new OperationRowMapper();

    public static UserRowMapper getUserRowMapper() {
        return USER_ROW_MAPPER;
    }

    public static AccountRowMapper getAccountRowMapper() {
        return ACCOUNT_ROW_MAPPER;
    }

    public static BillRowMapper getBillRowMapper(){return BILL_ROW_MAPPER;}

    public static CardRowMapper getCardRowMapper(){return CARD_ROW_MAPPER;}

    public static LoanRowMapper getLoanRowMapper(){return LOAN_ROW_MAPPER;}

    public static PenaltyRowMapper getPenaltyRowMapper(){return PENALTY_ROW_MAPPER;}

    public static OperationRowMapper getOperationRowMapper(){return OPERATION_ROW_MAPPER;}
}
