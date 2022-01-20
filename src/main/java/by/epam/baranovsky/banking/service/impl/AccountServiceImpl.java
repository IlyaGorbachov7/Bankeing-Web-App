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

    private volatile static AccountServiceImpl instance = null;
    private final AccountValidator validator = new AccountValidator();
    private final AccountDAO accountDAO = SqlDAOFactory.getInstance().getAccountDAO();

    private AccountServiceImpl() {}

    public static AccountServiceImpl getInstance() {
        if (instance == null) {
            synchronized (AccountServiceImpl.class) {
                if (instance == null) {
                    instance = new AccountServiceImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public Account findById(Integer id) throws ServiceException {
        Account account;

        try {
            account = accountDAO.findEntityById(id);
        } catch (DAOException e) {
            throw new ServiceException("Unable to retrieve account from DB",e);
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
            throw new ServiceException("Unable to retrieve account from DB",e);
        }

        return account;
    }

    @Override
    public List<Account> findByUserId(Integer id) throws ServiceException {
        List<Account> accounts;

        try{
            accounts = accountDAO.findByUserId(id);
        } catch (DAOException e) {
            throw new ServiceException("Unable to retrieve accounts from DB",e);
        }

        return accounts;
    }

    @Override
    public List<Account> findByStatusId(Integer id) throws ServiceException {
        List<Account> accounts;

        try{
            accounts = accountDAO.findByStatusId(id);
        } catch (DAOException e) {
            throw new ServiceException("Unable to retrieve accounts from DB",e);
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
    public Integer update(Account account) throws ServiceException {
        Integer result = 0;

        try {
            if(!validator.validate(account)){
                throw new ValidationException("Wrong input.");
            }
            if(account.getId() == null || accountDAO.findEntityById(account.getId())== null){
                throw new ValidationException("No account with such ID.");
            }
            result = accountDAO.update(account);
        } catch (DAOException e) {
            throw new ServiceException("Unable to update account in DB",e);
        }

        return result;
    }

    @Override
    public Account create(Account account) throws ServiceException {
        Account created;

        try {
            if(!validator.validate(account)){
            throw new ValidationException("Wrong input.");
        }
            created = accountDAO.findEntityById(accountDAO.create(account));
            created.setUsers(accountDAO.findUsers(created.getId()));
        } catch (DAOException e) {
            throw new ServiceException("Unable to create account in DB",e);
        }

        return created;
    }

    @Override
    public Integer delete(Integer id) throws ServiceException {
        try {
            return delete(accountDAO.findEntityById(id));
        } catch (DAOException e) {
            throw new ServiceException("Unable to delete account from DB",e);
        }
    }

    @Override
    public Integer delete(Account account) throws ServiceException {
        Integer result = 0;

        try {
            if(!validator.validate(account)){
                throw new ValidationException("Wrong input.");
            }
            result = accountDAO.delete(account);
        } catch (DAOException e) {
            throw new ServiceException("Unable to delete account from DB",e);
        }

        return result;
    }

    @Override
    public List<Account> findAll() throws ServiceException {
        List<Account> accounts;

        try{
            accounts = accountDAO.findAll();
        } catch (DAOException e) {
            throw new ServiceException("Unable to retrieve accounts from DB",e);
        }

        return accounts;
    }
}
