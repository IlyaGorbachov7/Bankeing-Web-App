package by.epam.baranovsky.banking.dao.query;

import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.entity.Entity;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public interface QueryMaster<T> {

    void setStatementParams(PreparedStatement statement, Object... params) throws SQLException;

    List<T> executeQuery(String query, Object... params) throws DAOException;

    T executeSingleEntityQuery(String query, Object... params) throws DAOException;

    int executeUpdate(String query, Object... params) throws DAOException;

    int executeTransaction(List<Query> queries) throws DAOException;

}
