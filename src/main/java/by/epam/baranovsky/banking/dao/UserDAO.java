package by.epam.baranovsky.banking.dao;

import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.entity.User;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import java.util.List;

/**
 * Interface for User entity DAO.
 * @author Baranovsky E. K.
 * @version 1.0.0
 * @see User
 */
public interface UserDAO extends AbstractDAO<User> {

    /**
     * Retrieves user from data source by email.
     * @param userEmail Email of the user
     * @return Instance of User with given email or {@code null} if none were found
     * @throws DAOException
     */
    User findByEmail(String userEmail) throws DAOException;

    /**
     * Retrieves users from data source by criteria filled with parameters.
     * @param criteria Criteria to search by.
     * @return List of Users satisfying given criteria.
     * @throws DAOException
     */
    List<User> findByCriteria(Criteria<? extends EntityParameters.UserParams> criteria) throws DAOException;

}
