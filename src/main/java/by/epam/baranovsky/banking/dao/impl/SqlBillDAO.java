package by.epam.baranovsky.banking.dao.impl;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.dao.BillDAO;
import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.dao.query.Query;
import by.epam.baranovsky.banking.dao.query.QueryMaster;
import by.epam.baranovsky.banking.dao.query.impl.SqlQueryMaster;
import by.epam.baranovsky.banking.dao.rowmapper.RowMapper;
import by.epam.baranovsky.banking.dao.rowmapper.RowMapperFactory;
import by.epam.baranovsky.banking.entity.Bill;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;

import java.sql.Date;
import java.util.List;

/**
 * Implementation of BillDAO for use with MySQL DB.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class SqlBillDAO implements BillDAO {

    /** Mapper to parse ResultSet objects into entities. */
    private static final RowMapper<Bill> mapper = RowMapperFactory.getBillRowMapper();

    /** Object that executes SQL queries. */
    private static final QueryMaster<Bill> queryMaster = new SqlQueryMaster<>(mapper);

    private static final String SQL_SELECT_ALL = String.format(
            "SELECT * FROM %s LEFT JOIN %s on %s.%s=%s.%s",
            DBMetadata.BILLS_TABLE, DBMetadata.BILL_STATUS_TABLE,
            DBMetadata.BILLS_TABLE, DBMetadata.BILLS_STATUS_ID,
            DBMetadata.BILL_STATUS_TABLE, DBMetadata.BILL_STATUS_ID);

    private static final String SQL_SELECT_BY_ID = String.format(
            "%s WHERE %s=?", SQL_SELECT_ALL, DBMetadata.BILLS_ID);


    private static final String SQL_UPDATE = String.format(
            "UPDATE %s SET %s=?,%s=?,%s=?,%s=?,%s=?,%s=?,%s=?,%s=?,%s=?,%s=? WHERE %s=?",
            DBMetadata.BILLS_TABLE, DBMetadata.BILLS_VALUE,
            DBMetadata.BILLS_ISSUE_DATE, DBMetadata.BILLS_DUE_DATE,
            DBMetadata.BILLS_USER_ID, DBMetadata.BILLS_PAYMENT_ACC_ID,
            DBMetadata.BILLS_STATUS_ID, DBMetadata.BILLS_PENALTY_ID,
            DBMetadata.BILLS_LOAN_ID, DBMetadata.BILLS_BEARER_ID,
            DBMetadata.BILLS_NOTICE, DBMetadata.BILLS_ID);

    private static final String SQL_INSERT= String.format(
            "INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s) VALUES (DEFAULT,?,?,?,?,?,?,?,?,?,?)",
            DBMetadata.BILLS_TABLE, DBMetadata.BILLS_ID,
            DBMetadata.BILLS_VALUE, DBMetadata.BILLS_ISSUE_DATE,
            DBMetadata.BILLS_DUE_DATE, DBMetadata.BILLS_USER_ID,
            DBMetadata.BILLS_PAYMENT_ACC_ID,DBMetadata.BILLS_STATUS_ID,
            DBMetadata.BILLS_PENALTY_ID, DBMetadata.BILLS_LOAN_ID,
            DBMetadata.BILLS_BEARER_ID, DBMetadata.BILLS_NOTICE);

    private static final String SQL_DELETE= String.format(
            "DELETE FROM %s WHERE %s=? LIMIT 1",
            DBMetadata.BILLS_TABLE, DBMetadata.BILLS_ID
    );

    /**
     * {@inheritDoc}
     * @return Number of rows affected in DB.
     * @throws DAOException if QueryMaster throws DAOException
     */
    @Override
    public Integer update(Bill entity) throws DAOException {

        return queryMaster.executeUpdate(
                SQL_UPDATE,
                entity.getValue(),
                new Date(entity.getIssueDate().getTime()),
                (entity.getDueDate() != null ? new Date(entity.getDueDate().getTime()) : null),
                entity.getUserId(),
                entity.getPaymentAccountId(),
                entity.getStatusId(),
                entity.getPenaltyId(),
                entity.getLoanId(),
                entity.getBearerId(),
                entity.getNotice(),
                entity.getId());
    }

    /**
     * {@inheritDoc}
     * @return Generated key of inserted row.
     * @throws DAOException if DAOException is thrown by QueryMaster.
     */
    @Override
    public Integer create(Bill entity) throws DAOException {
        return queryMaster.executeUpdate(
                SQL_INSERT,
                entity.getValue(),
                new Date(entity.getIssueDate().getTime()),
                (entity.getDueDate() != null ? new Date(entity.getDueDate().getTime()) : null),
                entity.getUserId(),
                entity.getPaymentAccountId(),
                entity.getStatusId(),
                entity.getPenaltyId(),
                entity.getLoanId(),
                entity.getBearerId(),
                entity.getNotice());
    }

    /**
     * {@inheritDoc}
     * @throws DAOException if QueryMaster throws DAOException
     */
    @Override
    public Bill findEntityById(Integer id) throws DAOException {
        return queryMaster.executeSingleEntityQuery(SQL_SELECT_BY_ID, id);
    }

    /**
     * {@inheritDoc}
     * @return Number of rows affected in DB.
     * @throws DAOException if QueryMaster throws DAOException
     */
    @Override
    public Integer delete(Integer id) throws DAOException {
        return queryMaster.executeUpdate(SQL_DELETE, id);
    }

    /**
     * {@inheritDoc}
     * @return Number of rows affected in DB.
     * @throws DAOException if QueryMaster throws DAOException
     */
    @Override
    public Integer delete(Bill entity) throws DAOException {
        return delete(entity.getId());
    }

    /**
     * {@inheritDoc}
     * @throws DAOException if QueryMaster throws DAOException
     */
    @Override
    public List<Bill> findAll() throws DAOException {
        return queryMaster.executeQuery(SQL_SELECT_ALL);
    }

    /**
     * {@inheritDoc}
     * @throws DAOException if QueryMaster throws DAOException
     */
    @Override
    public List<Bill> findByCriteria(Criteria<? extends EntityParameters.BillParam> criteria) throws DAOException {
        Query query = criteria.generateQuery(SQL_SELECT_ALL);
        return queryMaster.executeQuery(query.getSqlQueryString(), query.getParameters());
    }
}
