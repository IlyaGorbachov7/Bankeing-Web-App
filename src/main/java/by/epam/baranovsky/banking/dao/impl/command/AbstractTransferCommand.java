package by.epam.baranovsky.banking.dao.impl.command;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.dao.query.Query;
import by.epam.baranovsky.banking.dao.query.QueryMaster;
import by.epam.baranovsky.banking.dao.query.impl.SqlQueryMaster;
import by.epam.baranovsky.banking.dao.rowmapper.RowMapperFactory;
import by.epam.baranovsky.banking.entity.Operation;

import java.util.List;

public abstract class AbstractTransferCommand implements OperationCommand{

    protected static final String SQL_INSERT_OPERATION = String.format(
            "INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, NOW(),?)",
            DBMetadata.OPERATIONS_TABLE, DBMetadata.OPERATIONS_ID,
            DBMetadata.OPERATIONS_VALUE,DBMetadata.OPERATIONS_TYPE_ID,
            DBMetadata.OPERATIONS_ACC_ID,DBMetadata.OPERATIONS_TARGET_ACC_ID,
            DBMetadata.OPERATIONS_CARD_ID,DBMetadata.OPERATIONS_TARGET_CARD_ID,
            DBMetadata.OPERATIONS_BILL_ID, DBMetadata.OPERATIONS_PENALTY_ID,
            DBMetadata.OPERATIONS_DATE, DBMetadata.OPERATIONS_COMMISSION);

    protected static final String SQL_UPDATE_ACC = String.format(
            "UPDATE %s SET %s=%s+? WHERE %s=?",
            DBMetadata.ACCOUNTS_TABLE, DBMetadata.ACCOUNTS_BALANCE,
            DBMetadata.ACCOUNTS_BALANCE, DBMetadata.ACCOUNTS_ID);

    protected static final String SQL_UPDATE_ACC_FROM_CARD = String.format(
            "UPDATE %s SET %s=%s+? WHERE %s=(SELECT %s FROM %s WHERE %s=? LIMIT 1)",
            DBMetadata.ACCOUNTS_TABLE, DBMetadata.ACCOUNTS_BALANCE,
            DBMetadata.ACCOUNTS_BALANCE, DBMetadata.ACCOUNTS_ID,
            DBMetadata.BANK_CARDS_ACCOUNT_ID, DBMetadata.BANK_CARDS_TABLE,
            DBMetadata.BANK_CARDS_ID);

    protected QueryMaster<Operation> master
            = new SqlQueryMaster<>(RowMapperFactory.getOperationRowMapper());

    protected static Query getBasicInsert(Operation operation){
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

    protected abstract List<Query> prepareQuery(Operation operation) throws DAOException;
}
