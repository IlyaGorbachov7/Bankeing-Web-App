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

public class CardLockCommand implements OperationCommand{

    private static final QueryMaster<Operation> queryMaster
            = new SqlQueryMaster<>(RowMapperFactory.getOperationRowMapper());

    private static final String SQL_INSERT_OPERATION = String.format(
            "INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s,%s,%s) VALUES (DEFAULT, NULL, 4, NUll, NULL, ?, NULL, NULL, ?)",
            DBMetadata.OPERATIONS_TABLE, DBMetadata.OPERATIONS_ID,
            DBMetadata.OPERATIONS_VALUE,DBMetadata.OPERATIONS_TYPE_ID,
            DBMetadata.OPERATIONS_ACC_ID,DBMetadata.OPERATIONS_TARGET_ACC_ID,
            DBMetadata.OPERATIONS_CARD_ID,DBMetadata.OPERATIONS_TARGET_CARD_ID,
            DBMetadata.OPERATIONS_BILL_ID, DBMetadata.OPERATIONS_PENALTY_ID);

    private static final String SQL_UPDATE_CARD = String.format(
            "UPDATE %s SET %s=2 WHERE %s=?",
            DBMetadata.BANK_CARDS_TABLE, DBMetadata.BANK_CARDS_STATUS_ID,
            DBMetadata.BANK_CARDS_ID);


    @Override
    public int create(Operation operation) throws DAOException {
        OperationCommand.testNonNull(operation.getBankCardId());
        List<Query> queries = new ArrayList<>();

        queries.add(new Query(SQL_INSERT_OPERATION,
                operation.getBankCardId(),
                operation.getPenaltyId()));
        queries.add(new Query(SQL_UPDATE_CARD, operation.getBankCardId()));

        return queryMaster.executeTransaction(queries);
    }

}
