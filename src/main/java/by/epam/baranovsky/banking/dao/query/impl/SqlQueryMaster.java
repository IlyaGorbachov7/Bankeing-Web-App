package by.epam.baranovsky.banking.dao.query.impl;

import by.epam.baranovsky.banking.dao.connectionpool.ConnectionPool;
import by.epam.baranovsky.banking.dao.connectionpool.ConnectionPoolException;
import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.dao.query.Query;
import by.epam.baranovsky.banking.dao.query.QueryMaster;
import by.epam.baranovsky.banking.dao.rowmapper.RowMapper;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlQueryMaster<T> implements QueryMaster<T> {
    Logger logger = Logger.getLogger(SqlQueryMaster.class);
    private final RowMapper<T> mapper;

    public SqlQueryMaster(RowMapper<T> mapper) {
        this.mapper = mapper;
    }

    @Override
    public void setStatementParams(PreparedStatement statement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
    }

    @Override
    public List<T> executeQuery(String query, Object... params) throws DAOException {
        List<T> result = new ArrayList<>();

        try (Connection connection = ConnectionPool.getInstance().takeConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            setStatementParams(statement, params);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                T entity = mapper.map(resultSet);
                result.add(entity);
            }

        } catch (SQLException e) {
            logger.error("Unable to execute select query.", e);
            throw new DAOException("Unable to execute select query.", e);
        } catch (ConnectionPoolException e) {
            logger.error("Unable to get connection.", e);
            throw new DAOException("Unable to get connection.", e);
        }
        return result;
    }

    @Override
    public T executeSingleEntityQuery(String query, Object... params) throws DAOException {

        List<T> result = executeQuery(query, params);
        if (result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }

    }

    @Override
    public int executeUpdate(String query, Object... params) throws DAOException {

        try (Connection connection = ConnectionPool.getInstance().takeConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setStatementParams(statement, params);
            int rowsAffected = statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys != null && generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                return rowsAffected;
            }

        } catch (SQLException e) {
            logger.error("Unable to execute update query.", e);
            throw new DAOException("Unable to execute update query.", e);
        } catch (ConnectionPoolException e) {
            logger.error("Unable to get connection.", e);
            throw new DAOException("Unable to get connection.", e);
        }

    }

    @Override
    public int executeTransaction(List<Query> queries) throws DAOException {
        Connection connection = null;

        try {
            connection = ConnectionPool.getInstance().takeConnection();
            connection.setAutoCommit(false);
            int firstQueryGeneratedKey = -1;
            boolean idSet = false;
            for (Query query : queries) {
                PreparedStatement statement = connection.prepareStatement(query.getSqlQueryString(), Statement.RETURN_GENERATED_KEYS);
                setStatementParams(statement, query.getParameters());
                statement.executeUpdate();
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (!idSet && generatedKeys != null && generatedKeys.next()) {
                    firstQueryGeneratedKey = generatedKeys.getInt(1);
                    idSet = true;
                }
            }
            connection.commit();
            return firstQueryGeneratedKey;
        } catch (SQLException e) {
            logger.error("Unable to execute update query.", e);
            rollbackTransaction(connection);
            throw new DAOException("Unable to execute update query.", e);
        } catch (ConnectionPoolException e) {
            logger.error("Unable to take connection.", e);
            throw new DAOException("Unable to take connection.", e);
        } finally {
            releaseConnection(connection);
        }
    }

    private void rollbackTransaction(Connection connection) throws DAOException {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                logger.error("Unable to rollback transaction", e);
                throw new DAOException("Unable to rollback transaction", e);
            }
        }
    }

    private void releaseConnection(Connection connection) throws DAOException {
        if (connection != null) {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException e) {
                logger.error("Unable to return connection to connection pool.", e);
                throw new DAOException("Unable to return connection to connection pool.", e);
            }
        }
    }
}
