package by.epam.baranovsky.banking.service;

import by.epam.baranovsky.banking.entity.Bill;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.service.exception.ServiceException;
import by.epam.baranovsky.banking.service.exception.ValidationException;

import java.util.List;
/**
 * Interface for bills service.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public interface BillService {

    /**
     * Retrieves Bills from data source by criteria filled with parameters.
     * @param criteria Criteria to search by.
     * @return List of Bills satisfying given criteria.
     * @throws ServiceException if DAOException occurs.
     */
    List<Bill> findByCriteria(Criteria<? extends EntityParameters.BillParam> criteria) throws ServiceException;

    /**
     * Retrieves Bill from data source by its id.
     * @param id id of Bill to retrieve.
     * @return Instance of Bill with this id or {@code null} if not found.
     * @throws ServiceException if DAOException occurs.
     */
    Bill findById(Integer id) throws ServiceException;

    /**
     * Updates Bill in data source.
     * @param bill Bill to update
     * @return {@code true} if update is successful, {@code false} otherwise
     * @throws ServiceException if DAOException occurs.
     * @throws ValidationException if passed object did not pass validation.
     */
    boolean update(Bill bill) throws ServiceException;

    /**
     * Saves Bill to data source.
     * @param bill Bill to save
     * @return Created Bill if creation is successful, {@code null} otherwise
     * @throws ServiceException if DAOException occurs.
     * @throws ValidationException if passed object did not pass validation.
     */
    Bill create(Bill bill) throws ServiceException;

    /**
     * Deletes Bill from data source.
     * @param id id of Bill to delete
     * @return {@code true} if removal is successful, {@code false} otherwise
     * @throws ServiceException if DAOException occurs.
     * @throws ValidationException if object with such id did not pass validation.
     */
    boolean delete(Integer id) throws ServiceException;

    /**
     * Deletes Bill from data source.
     * @param bill Bill to delete
     * @return {@code true} if removal is successful, {@code false} otherwise
     * @throws ServiceException if DAOException occurs.
     * @throws ValidationException if passed object did not pass validation.
     */
    boolean delete(Bill bill) throws ServiceException;

    /**
     * Retrieves all Bills from data source.
     * @return List of all Bills stored in data source.
     * @throws ServiceException if DAOException occurs.
     */
    List<Bill> findAll() throws ServiceException;

}
