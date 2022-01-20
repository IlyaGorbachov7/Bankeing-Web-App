package by.epam.baranovsky.banking.dao.impl;

import by.epam.baranovsky.banking.constant.DBMetadata;
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
public class SqlOperationDAO implements by.epam.baranovsky.banking.dao.OperationDAO {

    private static final RowMapper<Operation> mapper = RowMapperFactory.getOperationRowMapper();
    private static final QueryMaster<Operation> queryMaster = new SqlQueryMaster<>(mapper);

    private static final String SQL_SELECT_ALL = String.format(
            "SELECT * FROM %s LEFT JOIN %s ON %s=%s",
            DBMetadata.OPERATIONS_TABLE, DBMetadata.OPERATION_TYPES_TABLE,
            DBMetadata.OPERATIONS_TYPE_ID,DBMetadata.OPERATION_TYPES_ID);

    private static final String SQL_SELECT_BY_ID = String.format(
            "%s WHERE %s=?", SQL_SELECT_ALL, DBMetadata.OPERATIONS_ID);

    private static final String SQL_UPDATE = String.format(
            "UPDATE %s SET %s=?,%s=?,%s=?,%s=?,%s=?,%s=?,%s=?,%s=? WHERE %s=?",
            DBMetadata.OPERATIONS_TABLE, DBMetadata.OPERATIONS_VALUE,
            DBMetadata.OPERATIONS_TYPE_ID, DBMetadata.OPERATIONS_ACC_ID,
            DBMetadata.OPERATIONS_TARGET_ACC_ID, DBMetadata.OPERATIONS_CARD_ID,
            DBMetadata.OPERATIONS_TARGET_CARD_ID, DBMetadata.OPERATIONS_BILL_ID,
            DBMetadata.OPERATIONS_PENALTY_ID, DBMetadata.OPERATIONS_ID);

    private static final String SQL_DELETE = String.format(
            "DELETE FROM %s WHERE %s=?",
            DBMetadata.OPERATIONS_TABLE, DBMetadata.OPERATIONS_ID
    );

    @Override
    public Integer update(Operation entity) throws DAOException {
        return queryMaster.executeUpdate(
                SQL_UPDATE,
                entity.getValue(),
                entity.getTypeId(),
                entity.getAccountId(),
                entity.getTargetAccountId(),
                entity.getBankCardId(),
                entity.getTargetBankCardId(),
                entity.getBillId(),
                entity.getPenaltyId(),
                entity.getId());
    }

    @Override
    public Integer create(Operation entity) throws DAOException {
        return OperationCommandEnum.getCommand(entity).create(entity);
    }

    @Override
    public Operation findEntityById(Integer id) throws DAOException {
        return queryMaster.executeSingleEntityQuery(SQL_SELECT_BY_ID, id);
    }

    @Override
    public Integer delete(Integer id) throws DAOException {
        return queryMaster.executeUpdate(SQL_DELETE, id);
    }

    @Override
    public Integer delete(Operation entity) throws DAOException {
        return delete(entity.getId());
    }

    @Override
    public List<Operation> findAll() throws DAOException {
        return queryMaster.executeQuery(SQL_SELECT_ALL);
    }

    @Override
    public List<Operation> findByCriteria(Criteria<? extends EntityParameters.OperationParam> criteria) throws DAOException {
        Query query = criteria.generateQuery(SQL_SELECT_ALL);
        return queryMaster.executeQuery(query.getQuery(), query.getParameters());
    }
}