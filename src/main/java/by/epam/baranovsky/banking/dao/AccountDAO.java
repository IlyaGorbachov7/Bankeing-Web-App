package by.epam.baranovsky.banking.dao;

import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.entity.Account;

import java.util.List;

public interface AccountDAO extends AbstractDAO<Account> {

    Account findByNumber(String number) throws DAOException;

    List<Account> findByUserId(Integer id) throws DAOException;

    List<Account> findByStatusId(Integer id) throws DAOException;

    List<Integer> findUsers(Integer id) throws DAOException;

}
