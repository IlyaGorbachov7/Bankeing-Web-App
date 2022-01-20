package by.epam.baranovsky.banking.dao;

import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.entity.User;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;

import java.util.List;

public interface UserDAO extends AbstractDAO<User> {

    User findByEmail(String userEmail) throws DAOException;

    List<User> findByCriteria(Criteria<? extends EntityParameters.UserParams> criteria) throws DAOException;

}
