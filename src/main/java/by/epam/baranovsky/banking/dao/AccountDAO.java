package by.epam.baranovsky.banking.dao;

import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.entity.Account;

import java.util.List;

/**
 * Interface for Account entity DAO.
 * @author Baranovsky E. K.
 * @version 1.0.0
 * @see Account
 */
public interface AccountDAO extends AbstractDAO<Account> {

    /**
     * Retrieves Account by its number.
     * @param number Number of account.
     * @return Instance of account with this number or {@code null} if none were found.
     * @throws DAOException
     */
    Account findByNumber(String number) throws DAOException;

    /**
     * Retrieves Accounts by their user.
     * @param id ID of user of account.
     * @return List of all Accounts possessed by given user.
     * @throws DAOException
     */
    List<Account> findByUserId(Integer id) throws DAOException;

    /**
     * Retrieves Accounts by their status ID.
     * @param id ID of status of an account.
     * @return List of all Accounts with such status ID.
     * @throws DAOException
     */
    List<Account> findByStatusId(Integer id) throws DAOException;

    /**
     * Retrieves IDs of users of an account with specified id.
     * @param id ID of account.
     * @return List of IDs of users that control the account.
     * @throws DAOException
     */
    List<Integer> findUsers(Integer id) throws DAOException;

}
