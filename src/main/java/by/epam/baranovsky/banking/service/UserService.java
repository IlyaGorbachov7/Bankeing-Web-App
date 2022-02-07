package by.epam.baranovsky.banking.service;

import by.epam.baranovsky.banking.entity.User;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.service.exception.ServiceException;
import by.epam.baranovsky.banking.service.exception.ValidationException;

import java.util.Date;
import java.util.List;
/**
 * Interface for user service.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public interface UserService {

    /**
     * Retrieves User from data source by email and password.
     * @param email email of the User
     * @param password password of the User
     * @return Retrieved User with password set null.
     * @throws ServiceException if DAOException occurs.
     * @throws ValidationException if password does not match password of a user with such email,
     * if there are no user with such email at all, or if user is banned.
     */
    User loginUser(String email, String password) throws ServiceException;

    /**
     * Saves User to data source by given parameters.
     * @param userEmail Email of user to create
     * @param userPassword Password of user to create (non-encrypted)
     * @param surname Surname of a User
     * @param name Name of a User
     * @param patronymic Patronymic of a user
     * @param passportSeries Passport series of a user
     * @param passportNumber Passport number of a user
     * @param birthDate Birth date of a user
     * @return Registered User
     * @throws ServiceException if DAOException occurs
     * @throws ValidationException if parameters are invalid or if user already exists.
     */
    User registerUser(String userEmail, String userPassword, String surname,
                      String name, String patronymic,
                      String passportSeries, String passportNumber,
                      Date birthDate) throws ServiceException;

    /**
     * Retrieves all Users from data source.
     * @return List of all Users stored in data source.
     * @throws ServiceException if DAOException occurs.
     */
    List<User> getAll() throws ServiceException;

    /**
     * Retrieves User from data source by its id.
     * @param id id of User to retrieve.
     * @return Instance of User with this id or {@code null} if not found.
     * @throws ServiceException if DAOException occurs.
     */
    User getById(Integer id) throws ServiceException;

    /**
     * Retrieves Users from data source by criteria filled with parameters.
     * @param criteria Criteria to search by.
     * @return List of Users satisfying given criteria.
     * @throws ServiceException if DAOException occurs.
     */
    List<User> getByCriteria (Criteria<EntityParameters.UserParams> criteria) throws ServiceException;

    /**
     * Updates User in data source.
     * @param user User to update
     * @return {@code true} if update is successful, {@code false} otherwise
     * @throws ServiceException if DAOException occurs.
     * @throws ValidationException if passed object did not pass validation.
     */
    boolean updateUser(User user) throws ServiceException;

    /**
     * Deletes User from data source.
     * @param user User to delete
     * @return {@code true} if removal is successful, {@code false} otherwise
     * @throws ServiceException if DAOException occurs.
     * @throws ValidationException if passed object did not pass validation.
     */
    boolean deleteUser(User user) throws ServiceException;

}
