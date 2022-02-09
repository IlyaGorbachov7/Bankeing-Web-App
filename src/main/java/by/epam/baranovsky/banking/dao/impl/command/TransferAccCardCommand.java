package by.epam.baranovsky.banking.dao.impl.command;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.dao.query.Query;
import by.epam.baranovsky.banking.entity.Operation;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of OperationCommand
 * for transfer operation from account to bank card.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class TransferAccCardCommand extends AbstractTransferCommand{

    /**
     * {@inheritDoc}
     * <p>
     *     Transaction includes insertion of an operation,
     *     updating transfer's sender account and
     *     an account that is tied to receiver card.
     * </p>
     * <p>
     *     If operation has a commission, it is further subtracted
     *     from sender account and added to bank's own account.
     * </p>
     * @throws DAOException if operation's value, account id or target card id are {@code null}.
     */
    @Override
    protected List<Query> prepareTransaction(Operation operation) throws DAOException {
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
