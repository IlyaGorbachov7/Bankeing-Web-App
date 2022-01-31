package by.epam.baranovsky.banking.dao.impl.command;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.dao.query.Query;
import by.epam.baranovsky.banking.entity.Operation;

import java.util.ArrayList;
import java.util.List;

public class TransferCardCardCommand extends AbstractTransferCommand{

    @Override
    protected List<Query> prepareQuery(Operation operation) throws DAOException {
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
    }

    @Override
    public int create(Operation operation) throws DAOException {
        return master.executeTransaction(prepareQuery(operation));
    }
}
