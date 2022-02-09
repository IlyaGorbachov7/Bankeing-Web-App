package by.epam.baranovsky.banking.dao;

import by.epam.baranovsky.banking.dao.connectionpool.ConnectionPool;
import by.epam.baranovsky.banking.dao.connectionpool.ConnectionPoolException;
import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.entity.Entity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 * Interface for DAOs.
 * @param <T> Type of entity operated by DAO.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public interface AbstractDAO<T extends Entity> {

    /**
     * Updates entity in data source.
     * @param entity Entity to update.
     * @return Result of data source update function.
     * @throws DAOException
     */
    Integer update(T entity) throws DAOException;

    /**
     * Saves entity in data source.
     * @param entity Entity to save.
     * @return Result of data source creation function.
     * @throws DAOException
     */
    Integer create(T entity) throws DAOException;

    /**
     * Retrieves entity from data source by its ID.
     * @param id ID of entity.
     * @return Retrieved entity or {@code null} if no entities were found
     * @throws DAOException
     */
    T findEntityById(Integer id) throws DAOException;

    /**
     * Deletes entity from data source.
     * @param id ID of entity to delete.
     * @return Result of data source removal function.
     * @throws DAOException
     */
    Integer delete(Integer id) throws DAOException;

    /**
     * Deletes entity from data source.
     * @param entity Entity to delete.
     * @return Result of data source removal function.
     * @throws DAOException
     */
    Integer delete(T entity) throws DAOException;

    /**
     * Retrieves all entities from data source.
     * @return List of all entities found in data source.
     * @throws DAOException
     */
    List<T> findAll() throws DAOException;


}