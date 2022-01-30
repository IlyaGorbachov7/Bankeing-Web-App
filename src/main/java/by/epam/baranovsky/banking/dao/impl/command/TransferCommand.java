package by.epam.baranovsky.banking.dao.impl.command;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.dao.query.Query;
import by.epam.baranovsky.banking.dao.query.QueryMaster;
import by.epam.baranovsky.banking.dao.query.impl.SqlQueryMaster;
import by.epam.baranovsky.banking.dao.rowmapper.RowMapperFactory;
import by.epam.baranovsky.banking.entity.Operation;

import java.util.ArrayList;
import java.util.List;

public class TransferCommand implements OperationCommand {

    private TransferType type;
    private QueryMaster<Operation> master
            = new SqlQueryMaster<>(RowMapperFactory.getOperationRowMapper());

    public TransferCommand(TransferType type) {
        this.type = type;
    }

    @Override
    public int create(Operation operation) throws DAOException {
        return master.executeTransaction(type.prepareTransaction(operation));
    }

    private interface TransferType {
        List<Query> prepareTransaction(Operation operation) throws DAOException;
    }

    static class TransferTypes {

        private static final String SQL_INSERT_OPERATION = String.format(
                "INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, NOW(),?)",
                DBMetadata.OPERATIONS_TABLE, DBMetadata.OPERATIONS_ID,
                DBMetadata.OPERATIONS_VALUE,DBMetadata.OPERATIONS_TYPE_ID,
                DBMetadata.OPERATIONS_ACC_ID,DBMetadata.OPERATIONS_TARGET_ACC_ID,
                DBMetadata.OPERATIONS_CARD_ID,DBMetadata.OPERATIONS_TARGET_CARD_ID,
                DBMetadata.OPERATIONS_BILL_ID, DBMetadata.OPERATIONS_PENALTY_ID,
                DBMetadata.OPERATIONS_DATE, DBMetadata.OPERATIONS_COMMISSION);

        private static final String SQL_UPDATE_ACC = String.format(
                "UPDATE %s SET %s=%s+? WHERE %s=?",
                DBMetadata.ACCOUNTS_TABLE, DBMetadata.ACCOUNTS_BALANCE,
                DBMetadata.ACCOUNTS_BALANCE, DBMetadata.ACCOUNTS_ID);

        private static final String SQL_UPDATE_ACC_FROM_CARD = String.format(
                "UPDATE %s SET %s=%s+? WHERE %s=(SELECT %s FROM %s WHERE %s=? LIMIT 1)",
                DBMetadata.ACCOUNTS_TABLE, DBMetadata.ACCOUNTS_BALANCE,
                DBMetadata.ACCOUNTS_BALANCE, DBMetadata.ACCOUNTS_ID,
                DBMetadata.BANK_CARDS_ACCOUNT_ID, DBMetadata.BANK_CARDS_TABLE,
                DBMetadata.BANK_CARDS_ID);

        static final TransferType ACC_TO_ACC = (operation) -> {
            double comm = (operation.getCommission() == null) ? 0d : operation.getCommission();
            OperationCommand.testNonNull(operation.getValue(),
                    operation.getTargetAccountId(), operation.getAccountId());
            List<Query> queries = new ArrayList<>();
            queries.add(getBasicInsert(operation));
            queries.add(new Query(
                    SQL_UPDATE_ACC,
                    operation.getValue(),
                    operation.getTargetAccountId()));
            queries.add(new Query(
                    SQL_UPDATE_ACC,
                    -(operation.getValue()+comm),
                    operation.getAccountId()));
            queries.add(new Query(
                    SQL_UPDATE_ACC,
                    comm,
                    DBMetadata.BANK_ACCOUNT_ID));
            return queries;
        };

        static final TransferType ACC_TO_CARD = (operation) -> {
            double comm = (operation.getCommission() == null) ? 0d : operation.getCommission();
            OperationCommand.testNonNull(operation.getValue(),
                    operation.getTargetBankCardId(), operation.getAccountId());
            List<Query> queries = new ArrayList<>();
            queries.add(getBasicInsert(operation));
            queries.add(new Query(
                    SQL_UPDATE_ACC_FROM_CARD,
                    operation.getValue(),
                    operation.getTargetBankCardId()));
            queries.add(new Query(
                    SQL_UPDATE_ACC,
                    -(operation.getValue()+comm),
                    operation.getAccountId()));
            queries.add(new Query(
                    SQL_UPDATE_ACC,
                    comm,
                    DBMetadata.BANK_ACCOUNT_ID));
            return queries;
        };

        static final TransferType CARD_TO_CARD = (operation) -> {
            double comm = (operation.getCommission() == null) ? 0d : operation.getCommission();
            OperationCommand.testNonNull(operation.getValue(),
                    operation.getTargetBankCardId(), operation.getBankCardId());
            List<Query> queries = new ArrayList<>();
            queries.add(getBasicInsert(operation));
            queries.add(new Query(
                    SQL_UPDATE_ACC_FROM_CARD,
                    operation.getValue(),
                    operation.getTargetBankCardId()));
            queries.add(new Query(
                    SQL_UPDATE_ACC_FROM_CARD,
                    -(operation.getValue()+comm),
                    operation.getBankCardId()));
            queries.add(new Query(
                    SQL_UPDATE_ACC,
                    comm,
                    DBMetadata.BANK_ACCOUNT_ID));
            return queries;
        };

        static final TransferType CARD_TO_ACC = (operation) -> {
            double comm = (operation.getCommission() == null) ? 0d : operation.getCommission();
            OperationCommand.testNonNull(operation.getValue(),
                    operation.getBankCardId(), operation.getTargetAccountId());
            List<Query> queries = new ArrayList<>();
            queries.add(getBasicInsert(operation));
            queries.add(new Query(
                    SQL_UPDATE_ACC,
                    operation.getValue(),
                    operation.getTargetAccountId()));
            queries.add(new Query(
                    SQL_UPDATE_ACC_FROM_CARD,
                    -(operation.getValue()+comm),
                    operation.getBankCardId()));
            queries.add(new Query(
                    SQL_UPDATE_ACC,
                    comm,
                    DBMetadata.BANK_ACCOUNT_ID));
            return queries;
        };

        static final TransferType ACCRUAL = (operation) ->{
            OperationCommand.testNonNull(operation.getValue(), operation.getAccountId());
            List<Query> queries = new ArrayList<>();
            queries.add(getBasicInsert(operation));
            queries.add(new Query(SQL_UPDATE_ACC,
                    operation.getValue(),
                    operation.getAccountId()));
            queries.add(new Query(
                    SQL_UPDATE_ACC,
                    -operation.getValue(),
                    DBMetadata.BANK_ACCOUNT_ID));
            return queries;
        };

        private TransferTypes(){}

        private static Query getBasicInsert(Operation operation){
            return new Query(
                    SQL_INSERT_OPERATION,
                    operation.getValue(),
                    operation.getTypeId(),
                    operation.getAccountId(),
                    operation.getTargetAccountId(),
                    operation.getBankCardId(),
                    operation.getTargetBankCardId(),
                    operation.getBillId(),
                    operation.getPenaltyId(),
                    operation.getCommission());
        }

    }

}
