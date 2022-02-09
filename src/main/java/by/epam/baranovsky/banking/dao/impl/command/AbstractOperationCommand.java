package by.epam.baranovsky.banking.dao.impl.command;

import by.epam.baranovsky.banking.dao.query.QueryMaster;
import by.epam.baranovsky.banking.dao.query.impl.SqlQueryMaster;
import by.epam.baranovsky.banking.dao.rowmapper.RowMapperFactory;
import by.epam.baranovsky.banking.entity.Operation;

/**
 * Abstract implementation of OperationCommand.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public abstract class AbstractOperationCommand implements OperationCommand{

    /** Object that executes SQL statements. */
    protected final QueryMaster<Operation> queryMaster
            = new SqlQueryMaster<>(RowMapperFactory.getOperationRowMapper());

}
