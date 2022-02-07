package by.epam.baranovsky.banking.service;

import by.epam.baranovsky.banking.entity.Operation;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.service.exception.ServiceException;
import by.epam.baranovsky.banking.service.exception.ValidationException;

import java.util.List;
/**
 * Interface for operations service.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public interface OperationService {

    /**
     * Retrieves Operations from data source by criteria filled with parameters.
     * @param criteria Criteria to search by.
     * @return List of Operations satisfying given criteria.
     * @throws ServiceException if DAOException occurs.
     */
    List<Operation> findByCriteria(Criteria<? extends EntityParameters.OperationParam> criteria) throws ServiceException;

    /**
     * Retrieves Operation from data source by its id.
     * @param id id of Operation to retrieve.
     * @return Instance of Operation with this id or {@code null} if not found.
     * @throws ServiceException if DAOException occurs.
     */
    Operation findById(Integer id) throws ServiceException;

    /**
     * Updates Operation in data source.
     * @param operation Operation to update
     * @return {@code true} if update is successful, {@code false} otherwise
     * @throws ServiceException if DAOException occurs.
     * @throws ValidationException if passed object did not pass validation.
     */
    boolean update(Operation operation) throws ServiceException;

    /**
     * Saves Operation to data source.
     * @param operation Operation to save
     * @return Created Operation if creation is successful, {@code null} otherwise
     * @throws ServiceException if DAOException occurs.
     * @throws ValidationException if passed object did not pass validation.
     */
    Operation create(Operation operation) throws ServiceException;

    /**
     * Deletes Operation from data source.
     * @param id id of Operation to delete
     * @return {@code true} if removal is successful, {@code false} otherwise
     * @throws ServiceException if DAOException occurs.
     * @throws ValidationException if object with such id did not pass validation.
     */
    boolean delete(Integer id) throws ServiceException;

    /**
     * Deletes Operation from data source.
     * @param operation Operation to delete
     * @return {@code true} if removal is successful, {@code false} otherwise
     * @throws ServiceException if DAOException occurs.
     * @throws ValidationException if passed object did not pass validation.
     */
    boolean delete(Operation operation) throws ServiceException;

    /**
     * Retrieves all Operations from data source.
     * @return List of all Operations stored in data source.
     * @throws ServiceException if DAOException occurs.
     */
    List<Operation> findAll() throws ServiceException;

}
