package by.epam.baranovsky.banking.dao.query;

import by.epam.baranovsky.banking.dao.exception.DAOException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface for a class that is used to execute SQL Statements.
 * @param <T> Type of entity to operate in DB.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public interface QueryMaster<T> {

    /**
     * Fills parameters of a PreparedStatement with values from params array.
     * @param statement PreparedStatement to fill
     * @param params objects to put into a statement
     * @throws SQLException
     */
    void setStatementParams(PreparedStatement statement, Object... params) throws SQLException;

    /**
     * Executes SQL queries with given parameters.
     * @param query SQL SELECT statement to execute
     *              (should be a prepared statement if there are parameters)
     * @param params parameters to be put into a statement
     * @return List of entities extracted from BD with this query.
     * @throws DAOException if SQLException or ConnectionPoolException occur
     */
    List<T> executeQuery(String query, Object... params) throws DAOException;

    /**
     * Executes SQL queries with given parameters,
     * retrieving only the first entity in result set.
     * @param query SQL SELECT statement to execute
     *              (should be a prepared statement if there are parameters)
     * @param params parameters to be put into a statement
     * @return Instance of entity extracted from BD with this query.
     * @throws DAOException if SQLException or ConnectionPoolException occur
     */
    T executeSingleEntityQuery(String query, Object... params) throws DAOException;

    /**
     * Executes SQL data manipulation statements with given parameters.
     * @param query SQL DML statement to execute
     *              (should be a prepared statement if there are parameters)
     * @param params parameters to be put into statement
     * @return Generated key of an inserted row,
     * or number of rows affected in DB for statements other than INSERT.
     * @throws DAOException if SQLException or ConnectionPoolException occur
     */
    int executeUpdate(String query, Object... params) throws DAOException;

    /**
     * Executes transactional update in DB.
     * @param queries List of Query objects, in order.
     * @return Generated key of the first executed INSERT statement,
     * -1 if no inserts were done
     * @throws DAOException if SQLException or ConnectionPoolException occur
     * @see Query
     */
    int executeTransaction(List<Query> queries) throws DAOException;

}
