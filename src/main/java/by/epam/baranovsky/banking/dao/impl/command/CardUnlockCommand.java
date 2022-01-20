package by.epam.baranovsky.banking.dao.impl.command;

import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.dao.query.Query;
import by.epam.baranovsky.banking.dao.query.QueryMaster;
import by.epam.baranovsky.banking.dao.query.impl.SqlQueryMaster;
import by.epam.baranovsky.banking.dao.rowmapper.RowMapperFactory;
import by.epam.baranovsky.banking.entity.Operation;

import java.util.ArrayList;
import java.util.List;

public class CardUnlockCommand implements OperationCommand{

    private static final QueryMaster<Operation> queryMaster
            = new SqlQueryMaster<>(RowMapperFactory.getOperationRowMapper());

    private static final String SQL_INSERT_OPERATION = "INSERT INTO operations " +
            "(id_operations, `value`, id_operation_type," +
            " id_account, id_target_account, id_bank_card," +
            " id_target_bank_card, id_bill, id_penalty) " +
            "VALUES (DEFAULT, NULL, 5, NULL, NULL, ?, NULL, NULL)";
    private static final String SQL_UPDATE_CARD = "UPDATE bank_cards " +
            "SET id_card_status=1 WHERE id_bank_Cards=?";

    @Override
    public int create(Operation operation) throws DAOException {
        OperationCommand.testNonNull(operation.getBankCardId());
        List<Query> queries = new ArrayList<>();

        queries.add(new Query(SQL_INSERT_OPERATION, operation.getBankCardId()));
        queries.add(new Query(SQL_UPDATE_CARD, operation.getBankCardId()));

        return queryMaster.executeTransaction(queries);
    }

}
