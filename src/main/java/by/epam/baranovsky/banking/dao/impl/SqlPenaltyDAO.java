package by.epam.baranovsky.banking.dao.impl;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.dao.query.Query;
import by.epam.baranovsky.banking.dao.query.QueryMaster;
import by.epam.baranovsky.banking.dao.query.impl.SqlQueryMaster;
import by.epam.baranovsky.banking.dao.rowmapper.RowMapper;
import by.epam.baranovsky.banking.dao.rowmapper.RowMapperFactory;
import by.epam.baranovsky.banking.entity.Penalty;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;

import java.util.List;

public class SqlPenaltyDAO implements by.epam.baranovsky.banking.dao.PenaltyDAO {

    private static final RowMapper<Penalty> mapper = RowMapperFactory.getPenaltyRowMapper();
    private static final QueryMaster<Penalty> queryMaster = new SqlQueryMaster<>(mapper);

    private static final String SQL_SELECT_ALL = String.format(
            "SELECT * FROM %s LEFT JOIN %s ON %s.%s=%s.%s LEFT JOIN %s ON %s=%s",
            DBMetadata.PENALTIES_TABLE, DBMetadata.PENALTY_TYPE_TABLE,
            DBMetadata.PENALTIES_TABLE, DBMetadata.PENALTIES_TYPE_ID,
            DBMetadata.PENALTY_TYPE_TABLE, DBMetadata.PENALTY_TYPE_ID,
            DBMetadata.PENALTY_STATUS_TABLE, DBMetadata.PENALTY_STATUS_ID,
            DBMetadata.PENALTIES_STATUS_ID);

    private static final String SQL_SELECT_BY_ID = String.format(
            "%s WHERE %s=?", SQL_SELECT_ALL, DBMetadata.PENALTIES_ID);

    private static final String SQL_INSERT = String.format(
            "INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s) VALUES (DEFAULT,?,?,?,?,?,?)",
            DBMetadata.PENALTIES_TABLE, DBMetadata.PENALTIES_ID,
            DBMetadata.PENALTIES_VALUE, DBMetadata.PENALTIES_NOTICE, DBMetadata.PENALTIES_PAYMENT_ACC_ID,
            DBMetadata.PENALTIES_TYPE_ID, DBMetadata.PENALTIES_USER_ID,
            DBMetadata.PENALTIES_STATUS_ID);

    private static final String SQL_UPDATE = String.format(
            "UPDATE %s SET %s=?,%s=?,%s=?,%s=?,%s=?,%s=? WHERE %s=?",
            DBMetadata.PENALTIES_TABLE, DBMetadata.PENALTIES_VALUE,
            DBMetadata.PENALTIES_NOTICE, DBMetadata.PENALTIES_PAYMENT_ACC_ID,
            DBMetadata.PENALTIES_TYPE_ID, DBMetadata.PENALTIES_USER_ID,
            DBMetadata.PENALTIES_STATUS_ID,DBMetadata.PENALTIES_ID);
    private static final String SQL_DELETE = String.format(
            "DELETE FROM %s WHERE %s=?",
            DBMetadata.PENALTIES_TABLE, DBMetadata.PENALTIES_ID);

    @Override
    public Integer update(Penalty entity) throws DAOException {
        return queryMaster.executeUpdate(
                SQL_UPDATE,
                entity.getValue(),
                entity.getNotice(),
                entity.getPaymentAccountId(),
                entity.getTypeId(),
                entity.getUserId(),
                entity.getStatusId(),
                entity.getId());
    }

    @Override
    public Integer create(Penalty entity) throws DAOException {
        return queryMaster.executeUpdate(
                SQL_INSERT,
                entity.getValue(),
                entity.getNotice(),
                entity.getPaymentAccountId(),
                entity.getTypeId(),
                entity.getUserId(),
                entity.getStatusId());
    }

    @Override
    public Penalty findEntityById(Integer id) throws DAOException {
        return queryMaster.executeSingleEntityQuery(SQL_SELECT_BY_ID,id);
    }

    @Override
    public Integer delete(Integer id) throws DAOException {
        return queryMaster.executeUpdate(SQL_DELETE, id);
    }

    @Override
    public Integer delete(Penalty entity) throws DAOException {
        return delete(entity.getId());
    }

    @Override
    public List<Penalty> findAll() throws DAOException {
        return queryMaster.executeQuery(SQL_SELECT_ALL);
    }

    @Override
    public List<Penalty> findByCriteria(Criteria<? extends EntityParameters.PenaltyParams> criteria) throws DAOException {
        Query query = criteria.generateQuery(SQL_SELECT_ALL);
        return queryMaster.executeQuery(query.getSqlQueryString(), query.getParameters());
    }
}