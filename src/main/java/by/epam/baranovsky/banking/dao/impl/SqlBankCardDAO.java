package by.epam.baranovsky.banking.dao.impl;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.dao.BankCardDAO;
import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.dao.query.QueryMaster;
import by.epam.baranovsky.banking.dao.query.impl.SqlQueryMaster;
import by.epam.baranovsky.banking.dao.rowmapper.RowMapper;
import by.epam.baranovsky.banking.dao.rowmapper.RowMapperFactory;
import by.epam.baranovsky.banking.entity.BankingCard;

import java.util.List;

public class SqlBankCardDAO  implements BankCardDAO {

    private static final RowMapper<BankingCard> mapper = RowMapperFactory.getCardRowMapper();
    private static final QueryMaster<BankingCard> queryMaster = new SqlQueryMaster<>(mapper);

    private static final String SQL_SELECT_ALL = String.format(
            "SELECT * FROM %s LEFT JOIN %s ON %s=%s LEFT JOIN %s ON %s.%s=%s.%s",
            DBMetadata.BANK_CARDS_TABLE, DBMetadata.CARD_STATUS_TABLE,
            DBMetadata.CARD_STATUS_ID, DBMetadata.BANK_CARDS_STATUS_ID,
            DBMetadata.CARD_TYPE_TABLE, DBMetadata.CARD_TYPE_TABLE,
            DBMetadata.CARD_TYPE_ID,
            DBMetadata.BANK_CARDS_TABLE, DBMetadata.BANK_CARDS_TYPE_ID);

    private static final String SQL_SELECT_BY_ID = String.format(
            "%s WHERE %s=?", SQL_SELECT_ALL, DBMetadata.BANK_CARDS_ID);

    private static final String SQL_SELECT_BY_NUM_CVC = String.format(
            "%s WHERE %s=? AND %s=?",
            SQL_SELECT_ALL, DBMetadata.BANK_CARDS_NUMBER, DBMetadata.BANK_CARDS_CVC);

    private static final String SQL_SELECT_BY_USER = String.format(
            "%s WHERE %s=?", SQL_SELECT_ALL, DBMetadata.BANK_CARDS_USER_ID);

    private static final String SQL_SELECT_BY_ACC = String.format(
            "%s WHERE %s=?", SQL_SELECT_ALL, DBMetadata.BANK_CARDS_ACCOUNT_ID);

    private static final String SQL_SELECT_BY_TYPE = String.format(
            "%s WHERE %s.%s=?", SQL_SELECT_ALL,
            DBMetadata.BANK_CARDS_TABLE, DBMetadata.BANK_CARDS_TYPE_ID);

    private static final String SQL_UPDATE = String.format(
            "UPDATE %s SET %s=?,%s=?,%s=?,%s=?," +
                    "%s=?,%s=?,%s=?,%s=?,%s=?,%s=?,%s=?,%s=? WHERE %s=? ",
            DBMetadata.BANK_CARDS_TABLE, DBMetadata.BANK_CARDS_NUMBER,
            DBMetadata.BANK_CARDS_CVC, DBMetadata.BANK_CARDS_PIN,
            DBMetadata.BANK_CARDS_EXPIRATION_DATE, DBMetadata.BANK_CARDS_REGISTRATION_DATE,
            DBMetadata.BANK_CARDS_BALANCE, DBMetadata.BANK_CARDS_OVERDRAFT_MAXIMUM,
            DBMetadata.BANK_CARDS_OVERDRAFT_INTEREST, DBMetadata.BANK_CARDS_USER_ID,
            DBMetadata.BANK_CARDS_ACCOUNT_ID,DBMetadata.BANK_CARDS_TYPE_ID,
            DBMetadata.BANK_CARDS_STATUS_ID, DBMetadata.BANK_CARDS_ID);


    private static final String SQL_INSERT = String.format(
            "INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            DBMetadata.BANK_CARDS_TABLE, DBMetadata.BANK_CARDS_ID,
            DBMetadata.BANK_CARDS_NUMBER,
            DBMetadata.BANK_CARDS_CVC, DBMetadata.BANK_CARDS_PIN,
            DBMetadata.BANK_CARDS_EXPIRATION_DATE, DBMetadata.BANK_CARDS_REGISTRATION_DATE,
            DBMetadata.BANK_CARDS_BALANCE, DBMetadata.BANK_CARDS_OVERDRAFT_MAXIMUM,
            DBMetadata.BANK_CARDS_OVERDRAFT_INTEREST, DBMetadata.BANK_CARDS_USER_ID,
            DBMetadata.BANK_CARDS_ACCOUNT_ID,DBMetadata.BANK_CARDS_TYPE_ID,
            DBMetadata.BANK_CARDS_STATUS_ID);

    private static final String SQL_DELETE = String.format(
            "DELETE FROM %s WHERE %s=?",
            DBMetadata.BANK_CARDS_TABLE, DBMetadata.BANK_CARDS_ID);

    @Override
    public Integer update(BankingCard entity) throws DAOException {

        java.util.Date expirationDate = entity.getExpirationDate();
        java.util.Date registrationDate = entity.getRegistrationDate();

        return queryMaster.executeUpdate(SQL_UPDATE,
                entity.getNumber(),
                entity.getCvc(),
                entity.getPin(),
                new java.sql.Date(expirationDate.getTime()),
                new java.sql.Date(registrationDate.getTime()),
                entity.getBalance(),
                entity.getOverdraftMax(),
                entity.getOverdraftInterestRate(),
                entity.getUserId(),
                entity.getAccountId(),
                entity.getCardTypeId(),
                entity.getStatusId(),
                entity.getId());
    }

    @Override
    public Integer create(BankingCard entity) throws DAOException {

        java.util.Date expirationDate = entity.getExpirationDate();
        java.util.Date registrationDate = entity.getRegistrationDate();

        return queryMaster.executeUpdate(SQL_INSERT,
                entity.getNumber(),
                entity.getCvc(),
                entity.getPin(),
                new java.sql.Date(expirationDate.getTime()),
                new java.sql.Date(registrationDate.getTime()),
                entity.getBalance(),
                entity.getOverdraftMax(),
                entity.getOverdraftInterestRate(),
                entity.getUserId(),
                entity.getAccountId(),
                entity.getCardTypeId(),
                entity.getStatusId());
    }

    @Override
    public BankingCard findEntityById(Integer id) throws DAOException {
        return queryMaster.executeSingleEntityQuery(SQL_SELECT_BY_ID, id);
    }

    @Override
    public Integer delete(Integer id) throws DAOException {
        return queryMaster.executeUpdate(SQL_DELETE, id);
    }

    @Override
    public Integer delete(BankingCard entity) throws DAOException {
        return delete(entity.getId());
    }

    @Override
    public List<BankingCard> findAll() throws DAOException {
        return queryMaster.executeQuery(SQL_SELECT_ALL);
    }

    @Override
    public BankingCard findByNumberAndCvc(String number, String cvc) throws DAOException {
        return queryMaster.executeSingleEntityQuery(
                SQL_SELECT_BY_NUM_CVC,
                number,
                cvc);
    }

    @Override
    public List<BankingCard> findByType(Integer typeId) throws DAOException {
        return queryMaster.executeQuery(SQL_SELECT_BY_TYPE, typeId);
    }

    @Override
    public List<BankingCard> findByUser(Integer userId) throws DAOException {
        return queryMaster.executeQuery(SQL_SELECT_BY_USER, userId);
    }

    @Override
    public List<BankingCard> findByAccount(Integer accountId) throws DAOException {
        return queryMaster.executeQuery(SQL_SELECT_BY_ACC, accountId);
    }
}
