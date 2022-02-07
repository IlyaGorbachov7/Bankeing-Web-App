package by.epam.baranovsky.banking.service;

import by.epam.baranovsky.banking.entity.Penalty;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.service.exception.ServiceException;
import by.epam.baranovsky.banking.service.exception.ValidationException;

import java.util.List;
/**
 * Interface for penalties service.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public interface PenaltyService {

    /**
     * Retrieves Penalties from data source by criteria filled with parameters.
     * @param criteria Criteria to search by.
     * @return List of Penalties satisfying given criteria.
     * @throws ServiceException if DAOException occurs.
     */
    List<Penalty> findByCriteria(Criteria<? extends EntityParameters.PenaltyParams> criteria) throws ServiceException;

    /**
     * Retrieves Penalty from data source by its id.
     * @param id id of Penalty to retrieve.
     * @return Instance of Penalty with this id or {@code null} if not found.
     * @throws ServiceException if DAOException occurs.
     */
    Penalty findById(Integer id) throws ServiceException;

    /**
     * Updates Penalty in data source.
     * @param penalty Penalty to update
     * @return {@code true} if update is successful, {@code false} otherwise
     * @throws ServiceException if DAOException occurs.
     * @throws ValidationException if passed object did not pass validation.
     */
    boolean update(Penalty penalty) throws ServiceException;

    /**
     * Saves Penalty to data source.
     * @param penalty Penalty to save
     * @return Created Penalty if creation is successful, {@code null} otherwise
     * @throws ServiceException if DAOException occurs.
     * @throws ValidationException if passed object did not pass validation.
     */
    Penalty create(Penalty penalty) throws ServiceException;

    /**
     * Deletes Penalty from data source.
     * @param id id of Penalty to delete
     * @return {@code true} if removal is successful, {@code false} otherwise
     * @throws ServiceException if DAOException occurs.
     * @throws ValidationException if object with such id did not pass validation.
     */
    boolean delete(Integer id) throws ServiceException;

    /**
     * Deletes Penalty from data source.
     * @param penalty Penalty to delete
     * @return {@code true} if removal is successful, {@code false} otherwise
     * @throws ServiceException if DAOException occurs.
     * @throws ValidationException if passed object did not pass validation.
     */
    boolean delete(Penalty penalty) throws ServiceException;

    /**
     * Retrieves all Penalties from data source.
     * @return List of all Penalties stored in data source.
     * @throws ServiceException if DAOException occurs.
     */
    List<Penalty> findAll() throws ServiceException;

}
