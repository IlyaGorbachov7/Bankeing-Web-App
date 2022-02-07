package by.epam.baranovsky.banking.service;

import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.service.exception.ServiceException;
import by.epam.baranovsky.banking.service.exception.ValidationException;

import java.util.List;

/**
 * Interface for accounts service.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public interface AccountService {

    /**
     * Retrieves Account from data source by its id.
     * @param id id of Account
     * @return Instance of Account or {@code null} if not found.
     * @throws ServiceException if DAOException occurs.
     */
    Account findById(Integer id) throws ServiceException;

    /**
     * Retrieves Account from data source by its number.
     * @param number number of Account
     * @return Instance of Account or {@code null} if not found.
     * @throws ServiceException if DAOException occurs.
     */
    Account findByNumber(String number) throws ServiceException;

    /**
     * Retrieves Accounts from data source by their user.
     * @param id id of User
     * @return List of Accounts that belong to that user.
     * @throws ServiceException if DAOException occurs.
     */
    List<Account> findByUserId(Integer id) throws ServiceException;

    /**
     * Retrieves Accounts from data source by their status id.
     * @param id id of Account status
     * @return List of Accounts with that status id.
     * @throws ServiceException if DAOException occurs.
     */
    List<Account> findByStatusId(Integer id) throws ServiceException;

    /**
     * Retrieves ids of all Users in possession of an Account.
     * @param id id of Account
     * @return List of Users in possession of an Account.
     * @throws ServiceException if DAOException occurs.
     */
    List<Integer> findUsers(Integer id) throws ServiceException;

    /**
     * Updates Account in data source.
     * @param account Account to update
     * @return {@code true} if update is successful, {@code false} otherwise
     * @throws ServiceException if DAOException occurs.
     * @throws ValidationException if passed object did not pass validation.
     */
    boolean update(Account account) throws ServiceException;

    /**
     * Saves Account to data source.
     * @param account Account to save
     * @return Created Account if creation is successful, {@code null} otherwise
     * @throws ServiceException if DAOException occurs.
     * @throws ValidationException if passed object did not pass validation.
     */
    Account create(Account account) throws ServiceException;

    /**
     * Deletes Account from data source.
     * @param id id of Account to delete
     * @return {@code true} if removal is successful, {@code false} otherwise
     * @throws ServiceException if DAOException occurs.
     * @throws ValidationException if object with such id did not pass validation.
     */
    boolean delete(Integer id) throws ServiceException;

    /**
     * Deletes Account from data source.
     * @param account Account to delete
     * @return {@code true} if removal is successful, {@code false} otherwise
     * @throws ServiceException if DAOException occurs.
     * @throws ValidationException if passed object did not pass validation.
     */
    boolean delete(Account account) throws ServiceException;

    /**
     * Retrieves all Accounts from data source.
     * @return List of all Accounts stored in data source.
     * @throws ServiceException if DAOException occurs.
     */
    List<Account> findAll() throws ServiceException;
}
