package by.epam.baranovsky.banking.service;

import by.epam.baranovsky.banking.entity.User;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import java.util.Date;
import java.util.List;

public interface UserService {

    User loginUser(String email, String password) throws ServiceException;

    User registerUser(String userEmail, String userPassword, String surname,
                      String name, String patronymic,
                      String passportSeries, String passportNumber,
                      Date birthDate) throws ServiceException;

    List<User> getAll() throws ServiceException;

    User getById(Integer id) throws ServiceException;

    List<User> getByCriteria (Criteria<EntityParameters.UserParams> criteria) throws ServiceException;

    Integer updateUser(User user) throws ServiceException;

    Integer deleteUser(User user) throws ServiceException;

}
