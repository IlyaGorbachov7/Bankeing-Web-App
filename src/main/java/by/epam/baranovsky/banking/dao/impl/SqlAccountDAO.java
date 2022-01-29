package by.epam.baranovsky.banking.dao.impl;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.dao.AccountDAO;
import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.dao.query.Query;
import by.epam.baranovsky.banking.dao.query.QueryMaster;
import by.epam.baranovsky.banking.dao.query.impl.SqlQueryMaster;
import by.epam.baranovsky.banking.dao.rowmapper.RowMapper;
import by.epam.baranovsky.banking.dao.rowmapper.RowMapperFactory;
import by.epam.baranovsky.banking.entity.Account;

import java.util.*;

public class SqlAccountDAO implements AccountDAO {

    private static final RowMapper<Account> mapper = RowMapperFactory.getAccountRowMapper();
    private static final QueryMaster<Account> queryMaster = new SqlQueryMaster<>(mapper);

    private static final String SQL_FIND_ALL = String.format(
            "SELECT * FROM %s LEFT JOIN %s ON %s=%s",
            DBMetadata.ACCOUNTS_TABLE, DBMetadata.ACCOUNT_STATUS_TABLE,
            DBMetadata.ACCOUNT_STATUS_ID, DBMetadata.ACCOUNTS_ACCOUNT_STATUS_ID);

    private static final String SQL_FIND_BY_ID = String.format(
            "%s WHERE %s=? LIMIT 1",
            SQL_FIND_ALL, DBMetadata.ACCOUNTS_ID);

    private static final String SQL_FIND_BY_NUMBER = String.format(
            "%s WHERE %s=? LIMIT 1",
            SQL_FIND_ALL, DBMetadata.ACCOUNTS_NUMBER);

    private static final String SQL_FIND_BY_STATUS = String.format(
            "%s WHERE %s=?",
            SQL_FIND_ALL, DBMetadata.ACCOUNT_STATUS_ID);

    private static final String SQL_FIND_BY_USER= String.format(
            "SELECT * FROM %s LEFT JOIN (%s LEFT JOIN %s ON %s=%s) ON %s=%s WHERE %s=?",
            DBMetadata.USERS_HAS_ACCOUNTS_TABLE, DBMetadata.ACCOUNTS_TABLE,
            DBMetadata.ACCOUNT_STATUS_TABLE, DBMetadata.ACCOUNT_STATUS_ID,
            DBMetadata.ACCOUNTS_ACCOUNT_STATUS_ID, DBMetadata.ACCOUNTS_ID,
            DBMetadata.USERS_HAS_ACCOUNTS_ACCOUNT_ID, DBMetadata.USERS_HAS_ACCOUNTS_USER_ID);

    private static final String SQL_FIND_USERS = String.format(
            "SELECT * FROM %s LEFT JOIN %s ON %s=%s WHERE %s=?",
            DBMetadata.USERS_HAS_ACCOUNTS_TABLE, DBMetadata.ACCOUNTS_TABLE,
            DBMetadata.USERS_HAS_ACCOUNTS_ACCOUNT_ID, DBMetadata.ACCOUNTS_ID,
            DBMetadata.ACCOUNTS_ID);

    private static final String SQL_UPDATE_ACCOUNT = String.format(
            "UPDATE %s SET %s=?, %s=?, %s=?, %s=? WHERE %s=?",
            DBMetadata.ACCOUNTS_TABLE, DBMetadata.ACCOUNTS_BALANCE,
            DBMetadata.ACCOUNTS_NUMBER, DBMetadata.ACCOUNTS_INTEREST,
            DBMetadata.ACCOUNTS_ACCOUNT_STATUS_ID, DBMetadata.ACCOUNTS_ID
    );

    private static final String SQL_INSERT_ACCOUNT = String.format(
            "INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (DEFAULT, ?, ?, ?, ?)",
            DBMetadata.ACCOUNTS_TABLE, DBMetadata.ACCOUNTS_ID,
            DBMetadata.ACCOUNTS_BALANCE, DBMetadata.ACCOUNTS_NUMBER,
            DBMetadata.ACCOUNTS_INTEREST, DBMetadata.ACCOUNTS_ACCOUNT_STATUS_ID
    );

    private static final String SQL_CREATE_ACCOUNT_USERS=String.format(
            "INSERT INTO %s (%s,%s) VALUES (?,(SELECT %s FROM %s WHERE %s=? LIMIT 1))",
            DBMetadata.USERS_HAS_ACCOUNTS_TABLE,
            DBMetadata.USERS_HAS_ACCOUNTS_USER_ID, DBMetadata.USERS_HAS_ACCOUNTS_ACCOUNT_ID,
            DBMetadata.ACCOUNTS_ID, DBMetadata.ACCOUNTS_TABLE, DBMetadata.ACCOUNTS_NUMBER);

    private static final String SQL_INSERT_ACCOUNT_USERS = String.format(
            "INSERT INTO %s (%s, %s) VALUES (?,?)",
            DBMetadata.USERS_HAS_ACCOUNTS_TABLE,
            DBMetadata.USERS_HAS_ACCOUNTS_USER_ID, DBMetadata.USERS_HAS_ACCOUNTS_ACCOUNT_ID
    );

    private static final String SQL_DELETE_ACCOUNT= String.format(
            "DELETE FROM %s WHERE %s=? LIMIT 1",
            DBMetadata.ACCOUNTS_TABLE, DBMetadata.ACCOUNTS_ID
    );

    private static final String SQL_DELETE_USER_HAS_ACCOUNT_BY_ACC_AND_USER = String.format(
            "DELETE FROM %s WHERE %s=? AND %s=? LIMIT 1",
            DBMetadata.USERS_HAS_ACCOUNTS_TABLE,
            DBMetadata.USERS_HAS_ACCOUNTS_ACCOUNT_ID,
            DBMetadata.USERS_HAS_ACCOUNTS_USER_ID
    );


    @Override
    public Integer update(Account entity) throws DAOException {

        List<Query> queries = new ArrayList<>();
        List<Integer> updatedUserList = entity.getUsers();

        if(updatedUserList != null){
            List<Integer> userListDB = findUsers(entity.getId());
            List<Integer> commonElems = new ArrayList<>(userListDB);
            commonElems.retainAll(updatedUserList);

            updatedUserList.removeAll(commonElems);
            userListDB.removeAll(commonElems);

            if(!userListDB.isEmpty()){
                for(Integer userId : userListDB){
                    queries.add(new Query(
                            SQL_DELETE_USER_HAS_ACCOUNT_BY_ACC_AND_USER,
                            entity.getId(),
                            userId));
                }
            }

            if(!updatedUserList.isEmpty()){
                for(Integer userId : updatedUserList){
                    queries.add(new Query(
                            SQL_INSERT_ACCOUNT_USERS,
                            userId,
                            entity.getId()));
                }
            }
        }

        queries.add(new Query( SQL_UPDATE_ACCOUNT,
                entity.getBalance(),
                entity.getAccountNumber(),
                entity.getYearlyInterestRate(),
                entity.getStatusId(),
                entity.getId()));

        return queryMaster.executeTransaction(queries);
    }

    @Override
    public Integer create(Account entity) throws DAOException {

        List<Query> queries = new ArrayList<>();

        queries.add(new Query(SQL_INSERT_ACCOUNT,
                entity.getBalance(),
                entity.getAccountNumber(),
                entity.getYearlyInterestRate(),
                entity.getStatusId()));

        for(Integer userId : entity.getUsers()){
            queries.add(new Query(SQL_CREATE_ACCOUNT_USERS,
                    userId, entity.getAccountNumber()));
        }

        return queryMaster.executeTransaction(queries);
    }

    @Override
    public Account findEntityById(Integer id) throws DAOException {
        return queryMaster.executeSingleEntityQuery(SQL_FIND_BY_ID, id);
    }

    @Override
    public Integer delete(Integer id) throws DAOException {
        return queryMaster.executeUpdate(SQL_DELETE_ACCOUNT, id);
    }

    @Override
    public Integer delete(Account entity) throws DAOException {
        return delete(entity.getId());
    }

    @Override
    public List<Account> findAll() throws DAOException {
        return queryMaster.executeQuery(SQL_FIND_ALL);
    }

    @Override
    public Account findByNumber(String number) throws DAOException {
        return queryMaster.executeSingleEntityQuery(SQL_FIND_BY_NUMBER, number);
    }

    @Override
    public List<Account> findByUserId(Integer id) throws DAOException {
        return queryMaster.executeQuery(SQL_FIND_BY_USER, id);
    }

    @Override
    public List<Account> findByStatusId(Integer id) throws DAOException {
        return queryMaster.executeQuery(SQL_FIND_BY_STATUS,id);
    }

    @Override
    public List<Integer> findUsers(Integer id) throws DAOException {

        RowMapper<Integer> userIdMapper = resultSet ->
                resultSet.getInt(DBMetadata.USERS_HAS_ACCOUNTS_USER_ID);

        QueryMaster<Integer> userIdQueryMaster = new SqlQueryMaster<>(userIdMapper);

        return userIdQueryMaster.executeQuery(SQL_FIND_USERS, id);
    }
}
