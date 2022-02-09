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

/**
 * Subclass of AbstractOperationCommand
 * for card blockage operation.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class CardLockCommand extends AbstractOperationCommand{

    private static final String SQL_INSERT_OPERATION = String.format(
            "INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s) VALUES (DEFAULT, NULL, 4, NUll, NULL, ?, NULL, NULL, ?, NOW())",
            DBMetadata.OPERATIONS_TABLE, DBMetadata.OPERATIONS_ID,
            DBMetadata.OPERATIONS_VALUE,DBMetadata.OPERATIONS_TYPE_ID,
            DBMetadata.OPERATIONS_ACC_ID,DBMetadata.OPERATIONS_TARGET_ACC_ID,
            DBMetadata.OPERATIONS_CARD_ID,DBMetadata.OPERATIONS_TARGET_CARD_ID,
            DBMetadata.OPERATIONS_BILL_ID, DBMetadata.OPERATIONS_PENALTY_ID,
            DBMetadata.OPERATIONS_DATE);

    private static final String SQL_UPDATE_CARD = String.format(
            "UPDATE %s SET %s=2 WHERE %s=?",
            DBMetadata.BANK_CARDS_TABLE, DBMetadata.BANK_CARDS_STATUS_ID,
            DBMetadata.BANK_CARDS_ID);


    /**
     * {@inheritDoc}
     * <p>
     *     Updates card passed in operation's
     *     bankCardId field (changes status to locked).
     * </p>
     * @param operation Operation to save to DB.
     * @throws DAOException if QueryMaster throws DAOException
     *      * or if bank card id of operation is {@code null}.
     */
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
