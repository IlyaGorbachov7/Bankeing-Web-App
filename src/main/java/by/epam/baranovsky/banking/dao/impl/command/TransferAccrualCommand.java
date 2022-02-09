package by.epam.baranovsky.banking.dao.impl.command;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.dao.query.Query;
import by.epam.baranovsky.banking.entity.Operation;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of OperationCommand
 * for operation of monthly interest accrual.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class TransferAccrualCommand extends AbstractTransferCommand{

    /**
     * {@inheritDoc}
     * <p>
     *     Transaction includes insertion of an operation,
     *     updating receiver account and bank's own account.
     *     Value of transfer is subtracted from bank and
     *     added to receiver.<b> Receiver is identified  by accountId of operation.</b>
     * </p>
     * @throws DAOException if operation's value or account id are {@code null}.
     */
    @Override
    protected List<Query> prepareTransaction(Operation operation) throws DAOException {
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
    }

    /**
     * {@inheritDoc}
     * @throws DAOException if QueryMaster throws DAOException
     */
    @Override
    public int create(Operation operation) throws DAOException {
        return queryMaster.executeTransaction(prepareTransaction(operation));
    }
}
