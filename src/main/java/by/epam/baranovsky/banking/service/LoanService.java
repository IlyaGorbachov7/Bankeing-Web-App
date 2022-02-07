package by.epam.baranovsky.banking.service;

import by.epam.baranovsky.banking.entity.Loan;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.service.exception.ServiceException;
import by.epam.baranovsky.banking.service.exception.ValidationException;

import java.util.List;

/**
 * Interface for loans service.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public interface LoanService {

    /**
     * Retrieves Loans from data source by criteria filled with parameters.
     * @param criteria Criteria to search by.
     * @return List of Loans satisfying given criteria.
     * @throws ServiceException if DAOException occurs.
     */
    List<Loan> findByCriteria(Criteria<? extends EntityParameters.LoanParams> criteria) throws ServiceException;

    /**
     * Retrieves Loan from data source by its id.
     * @param id id of Loan to retrieve.
     * @return Instance of Loan with this id or {@code null} ifnot found.
     * @throws ServiceException if DAOException occurs.
     */
    Loan findById(Integer id) throws ServiceException;

    /**
     * Updates Loan in data source.
     * @param loan Loan to update
     * @return {@code true} if update is successful, {@code false} otherwise
     * @throws ServiceException if DAOException occurs.
     * @throws ValidationException if passed object did not pass validation.
     */
    boolean update(Loan loan) throws ServiceException;

    /**
     * Saves Loan to data source.
     * @param loan Loan to save
     * @return Created Loan if creation is successful, {@code null} otherwise
     * @throws ServiceException if DAOException occurs.
     * @throws ValidationException if passed object did not pass validation.
     */
    Loan create(Loan loan) throws ServiceException;

    /**
     * Deletes Loan from data source.
     * @param id id of Loan to delete
     * @return {@code true} if removal is successful, {@code false} otherwise
     * @throws ServiceException if DAOException occurs.
     * @throws ValidationException if object with such id did not pass validation.
     */
    boolean delete(Integer id) throws ServiceException;

    /**
     * Deletes Loan from data source.
     * @param loan Loan to delete
     * @return {@code true} if removal is successful, {@code false} otherwise
     * @throws ServiceException if DAOException occurs.
     * @throws ValidationException if passed object did not pass validation.
     */
    boolean delete(Loan loan) throws ServiceException;

    /**
     * Retrieves all Loans from data source.
     * @return List of all Loans stored in data source.
     * @throws ServiceException if DAOException occurs.
     */
    List<Loan> findAll() throws ServiceException;

}
