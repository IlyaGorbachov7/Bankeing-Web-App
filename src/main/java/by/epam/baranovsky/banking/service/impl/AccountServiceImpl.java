package by.epam.baranovsky.banking.service.impl;

import by.epam.baranovsky.banking.dao.AccountDAO;
import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.dao.factory.impl.SqlDAOFactory;
import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.service.AccountService;
import by.epam.baranovsky.banking.service.exception.ServiceException;
import by.epam.baranovsky.banking.service.exception.ValidationException;
import by.epam.baranovsky.banking.service.validator.AccountValidator;

import java.util.List;

public class AccountServiceImpl implements AccountService {

    private final AccountValidator validator = new AccountValidator();
    private final AccountDAO accountDAO = SqlDAOFactory.getInstance().getAccountDAO();

    @Override
    public Account findById(Integer id) throws ServiceException {
        Account account;

        try {
            account = accountDAO.findEntityById(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return account;
    }

    @Override
    public Account findByNumber(String number) throws ServiceException {
        Account account;

        try {
            if(!validator.validateNumber(number)){
               throw new ValidationException("Wrong account number");
            }
            account = accountDAO.findByNumber(number);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return account;
    }

    @Override
    public List<Account> findByUserId(Integer id) throws ServiceException {
        List<Account> accounts;

        try{
            accounts = accountDAO.findByUserId(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return accounts;
    }

    @Override
    public List<Account> findByStatusId(Integer id) throws ServiceException {
        List<Account> accounts;

        try{
            accounts = accountDAO.findByStatusId(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return accounts;
    }

    @Override
    public List<Integer> findUsers(Integer id) throws ServiceException {
        List<Integer> users;

        try{
            users = accountDAO.findUsers(id);
        } catch (DAOException e) {
            throw new ServiceException("Unable to retrieve users of account from DB",e);
        }

        return users;
    }

    @Override
    public boolean update(Account account) throws ServiceException {
        Integer result = 0;

        try {
            if(!validator.validate(account)){
                throw new ValidationException();
            }
            if(account.getId() == null || accountDAO.findEntityById(account.getId())== null){
                throw new ValidationException("No account with such ID.");
            }
            result = accountDAO.update(account);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return result>0;
    }

    @Override
    public Account create(Account account) throws ServiceException {
        Account created;

        try {
            if(!validator.validate(account)){
            throw new ValidationException();
        }
            created = accountDAO.findEntityById(accountDAO.create(account));
            created.setUsers(accountDAO.findUsers(created.getId()));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return created;
    }

    @Override
    public boolean delete(Integer id) throws ServiceException {
        try {
            return delete(accountDAO.findEntityById(id));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean delete(Account account) throws ServiceException {
        Integer result = 0;

        try {
            if(!validator.validate(account)){
                throw new ValidationException();
            }
            result = accountDAO.delete(account);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return result>0;
    }

    @Override
    public List<Account> findAll() throws ServiceException {
        List<Account> accounts;

        try{
            accounts = accountDAO.findAll();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return accounts;
    }
}
