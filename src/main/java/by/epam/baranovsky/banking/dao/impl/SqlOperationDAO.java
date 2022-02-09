package by.epam.baranovsky.banking.dao.impl;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.dao.OperationDAO;
import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.dao.impl.command.OperationCommandEnum;
import by.epam.baranovsky.banking.dao.query.Query;
import by.epam.baranovsky.banking.dao.query.QueryMaster;
import by.epam.baranovsky.banking.dao.query.impl.SqlQueryMaster;
import by.epam.baranovsky.banking.dao.rowmapper.RowMapper;
import by.epam.baranovsky.banking.dao.rowmapper.RowMapperFactory;
import by.epam.baranovsky.banking.entity.Operation;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;

import java.util.List;

/**
 * Implementation of OperationDAO for use with MySQL DB.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class SqlOperationDAO implements OperationDAO {

    /** Mapper to parse ResultSet objects into entities. */
    private static final RowMapper<Operation> mapper = RowMapperFactory.getOperationRowMapper();

    /** Object that executes SQL queries. */
    private static final QueryMaster<Operation> queryMaster = new SqlQueryMaster<>(mapper);

    private static final String SQL_SELECT_ALL = String.format(
            "SELECT * FROM %s LEFT JOIN %s ON %s=%s",
            DBMetadata.OPERATIONS_TABLE, DBMetadata.OPERATION_TYPES_TABLE,
            DBMetadata.OPERATIONS_TYPE_ID,DBMetadata.OPERATION_TYPES_ID);

    private static final String SQL_SELECT_BY_ID = String.format(
            "%s WHERE %s=?", SQL_SELECT_ALL, DBMetadata.OPERATIONS_ID);

    private static final String SQL_UPDATE = String.format(
            "UPDATE %s SET %s=?,%s=?,%s=?,%s=?,%s=?,%s=?,%s=?,%s=?,%s=?,%s=? WHERE %s=?",
            DBMetadata.OPERATIONS_TABLE, DBMetadata.OPERATIONS_VALUE,
            DBMetadata.OPERATIONS_DATE,
            DBMetadata.OPERATIONS_TYPE_ID, DBMetadata.OPERATIONS_ACC_ID,
            DBMetadata.OPERATIONS_TARGET_ACC_ID, DBMetadata.OPERATIONS_CARD_ID,
            DBMetadata.OPERATIONS_TARGET_CARD_ID, DBMetadata.OPERATIONS_BILL_ID,
            DBMetadata.OPERATIONS_PENALTY_ID, DBMetadata.OPERATIONS_COMMISSION,
            DBMetadata.OPERATIONS_ID);

    private static final String SQL_DELETE = String.format(
            "DELETE FROM %s WHERE %s=?",
            DBMetadata.OPERATIONS_TABLE, DBMetadata.OPERATIONS_ID
    );

    /**
     * {@inheritDoc}
     * <p>
     *     Updating an operation in DB will not affect any
     *     changes that this operation has made in other tables.
     * </p>
     * @return Number of rows affected in DB.
     * @throws DAOException if QueryMaster throws DAOException
     */
    @Override
    public Integer update(Operation entity) throws DAOException {
        return queryMaster.executeUpdate(
                SQL_UPDATE,
                entity.getValue(),
                entity.getOperationDate(),
                entity.getTypeId(),
                entity.getAccountId(),
                entity.getTargetAccountId(),
                entity.getBankCardId(),
                entity.getTargetBankCardId(),
                entity.getBillId(),
                entity.getPenaltyId(),
                entity.getCommission(),
                entity.getId());
    }

    /**
     * {@inheritDoc}
     * <p>
     *     Operations are a key part of the system,
     *     and every creation of an operation not only
     *     inserts rows into operations table, but updates
     *     other tables as well.
     * </p>
     * <p>
     *     Since there are more than a dozen different types of operations,
     *     creation of each type of an operation
     *     (identified by passed object's type ID)
     *     has been delegated to separate OperationCommand classes.
     * </p>
     * @return Generated key of inserted row.
     * @throws DAOException if QueryMaster throws DAOException
     * @see by.epam.baranovsky.banking.dao.impl.command.OperationCommand
     */
    @Override
    public Integer create(Operation entity) throws DAOException {
        return OperationCommandEnum.getCommand(entity).create(entity);
    }

    /**
     * {@inheritDoc}
     * @throws DAOException if QueryMaster throws DAOException
     */
    @Override
    public Operation findEntityById(Integer id) throws DAOException {
        return queryMaster.executeSingleEntityQuery(SQL_SELECT_BY_ID, id);
    }

    /**
     * {@inheritDoc}
     * <p>
     *     Removing an operation from DB will not affect any
     *     changes that this operation has made in other tables.
     * </p>
     * @return Number of rows affected in DB.
     * @throws DAOException if QueryMaster throws DAOException
     */
    @Override
    public Integer delete(Integer id) throws DAOException {
        return queryMaster.executeUpdate(SQL_DELETE, id);
    }

    /**
     * {@inheritDoc}
     * <p>
     *     Removing an operation from DB will not affect any
     *     changes that this operation has made in other tables.
     * </p>
     * @return Number of rows affected in DB.
     * @throws DAOException if QueryMaster throws DAOException
     */
    @Override
    public Integer delete(Operation entity) throws DAOException {
        return delete(entity.getId());
    }

    /**
     * {@inheritDoc}
     * @throws DAOException if QueryMaster throws DAOException
     */
    @Override
    public List<Operation> findAll() throws DAOException {
        return queryMaster.executeQuery(SQL_SELECT_ALL);
    }

    /**
     * {@inheritDoc}
     * @throws DAOException if QueryMaster throws DAOException
     */
    @Override
    public List<Operation> findByCriteria(Criteria<? extends EntityParameters.OperationParam> criteria) throws DAOException {
        Query query = criteria.generateQuery(SQL_SELECT_ALL);
        return queryMaster.executeQuery(query.getSqlQueryString(), query.getParameters());
    }
}