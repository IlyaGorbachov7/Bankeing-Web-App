package by.epam.baranovsky.banking.dao;

import by.epam.baranovsky.banking.dao.connectionpool.ConnectionPool;
import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.entity.Entity;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface AbstractDAO<T extends Entity> {

    Integer update(T entity) throws DAOException;

    Integer create(T entity) throws DAOException;

    T findEntityById(Integer id) throws DAOException;

    Integer delete(Integer id) throws DAOException;

    Integer delete(T entity) throws DAOException;

    List<T> findAll() throws DAOException;

}