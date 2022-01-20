package by.epam.baranovsky.banking.service;

import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import java.util.List;

public interface AccountService {

    Account findById(Integer id) throws ServiceException;

    Account findByNumber(String number) throws ServiceException;

    List<Account> findByUserId(Integer id) throws ServiceException;

    List<Account> findByStatusId(Integer id) throws ServiceException;

    List<Integer> findUsers(Integer id) throws ServiceException;

    Integer update(Account account) throws ServiceException;

    Account create(Account account) throws ServiceException;

    Integer delete(Integer id) throws ServiceException;

    Integer delete(Account account) throws ServiceException;

    List<Account> findAll() throws ServiceException;

}
