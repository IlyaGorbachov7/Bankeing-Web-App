package by.epam.baranovsky.banking.service.impl;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.constant.Message;
import by.epam.baranovsky.banking.dao.UserDAO;
import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.dao.factory.impl.SqlDAOFactory;
import by.epam.baranovsky.banking.entity.User;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.service.UserService;
import by.epam.baranovsky.banking.service.exception.ServiceException;
import by.epam.baranovsky.banking.service.exception.ValidationException;
import by.epam.baranovsky.banking.service.validator.UserValidator;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementation of UserService.
 * Provides utils for working with User entities.
 *
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class UserServiceImpl implements UserService {

    private static final Integer DEFAULT_ROLE = DBMetadata.USER_ROLE_REGULAR;
    private final UserValidator validator = new UserValidator();
    private final UserDAO userDAO = SqlDAOFactory.getInstance().getUserDAO();

    /**
     * {@inheritDoc}
     */
    @Override
    public User loginUser(String email, String password) throws ServiceException {
        User user;

        try {
            user = userDAO.findByEmail(email);
            if (user == null || !BCrypt.checkpw(password, user.getPassword())) {
                throw new ValidationException(Message.WRONG_EMAIL_OR_PASS);
            }
            if(user.getRoleId().equals(DBMetadata.USER_ROLE_BANNED)){
                throw new ValidationException(Message.USER_BANNED);
            }
            user.setLastLogin(new Date());
            updateUser(user);
            user.setPassword(null);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User registerUser(String userEmail, String userPassword, String surname,
                             String name, String patronymic, String passportSeries,
                             String passportNumber, Date birthDate) throws ServiceException {

        User user = new User();
        user.setEmail(userEmail);
        user.setPassword(userPassword);
        user.setFirstName(name);
        user.setLastName(surname);
        user.setPatronymic(patronymic);
        user.setPassportNumber(passportNumber);
        user.setPassportSeries(passportSeries);
        user.setBirthDate(birthDate);
        user.setRoleId(DEFAULT_ROLE);

        try{
            if (!validator.validate(user)) {
                throw new ValidationException();
            }
            if(userDAO.findByEmail(user.getEmail()) != null){
                throw new ValidationException(Message.USER_EXISTS);
            }
            user.setPassword(BCrypt.hashpw(userPassword, BCrypt.gensalt()));
            user = userDAO.findEntityById(userDAO.create(user));
            user.setPassword(null);
        } catch (DAOException e){
            throw new ServiceException(e);
        }

        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getAll() throws ServiceException {

        List<User> users = new ArrayList<>();

        try {
            users = userDAO.findAll();
            for(User user : users){
                user.setPassword(null);
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return users;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getById(Integer id) throws ServiceException {

        User user;

        try{
            user = userDAO.findEntityById(id);
            user.setPassword(null);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getByCriteria(Criteria<EntityParameters.UserParams> criteria) throws ServiceException {
        List<User> users = new ArrayList<>();
        try {
            users = userDAO.findByCriteria(criteria);
            for(User user : users){
                user.setPassword(null);
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return users;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updateUser(User user) throws ServiceException {

        Integer result = 0;

        try{
            if(user.getPassword() == null){
                user.setPassword(userDAO.findEntityById(user.getId()).getPassword());
            }
            if(!validator.validate(user)){
                throw new ValidationException();
            }
            if(user.getId() == null || userDAO.findEntityById(user.getId())== null){
                throw new ValidationException(Message.NO_SUCH_USER);
            }
            result = userDAO.update(user);
            user.setPassword(null);

        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return result>0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteUser(User user) throws ServiceException {
        Integer result = 0;

        try{
            result = userDAO.delete(user);

        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return result>0;
    }
}