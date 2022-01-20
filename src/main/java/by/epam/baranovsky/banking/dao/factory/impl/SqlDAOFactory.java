package by.epam.baranovsky.banking.dao.factory.impl;

import by.epam.baranovsky.banking.dao.*;
import by.epam.baranovsky.banking.dao.factory.DAOFactory;
import by.epam.baranovsky.banking.dao.impl.*;

public class SqlDAOFactory implements DAOFactory {

    private static volatile SqlDAOFactory instance;
    private final UserDAO userDAO = new SqlUserDAO();
    private final AccountDAO accountDAO = new SqlAccountDAO();
    private final OperationDAO operationDAO = new SqlOperationDAO();
    private final BankCardDAO bankCardDAO = new SqlBankCardDAO();
    private final BillDAO billDAO = new SqlBillDAO();
    private final LoanDAO loanDAO = new SqlLoanDAO();
    private final PenaltyDAO penaltyDAO = new SqlPenaltyDAO();


    private SqlDAOFactory(){}

    public static SqlDAOFactory getInstance(){
        if (instance == null) {
            synchronized (SqlDAOFactory.class) {
                if (instance == null) {
                    instance = new SqlDAOFactory();
                }
            }
        }
        return instance;
    }

    @Override
    public UserDAO getUserDAO() {
        return userDAO;
    }

    @Override
    public AccountDAO getAccountDAO() {
        return accountDAO;
    }

    @Override
    public OperationDAO getOperationDAO() {
        return operationDAO;
    }

    @Override
    public BankCardDAO getBankCardDAO() {
        return bankCardDAO;
    }

    @Override
    public BillDAO getBillDAO() {
        return billDAO;
    }

    @Override
    public LoanDAO getLoanDAO() {
        return loanDAO;
    }

    @Override
    public PenaltyDAO getPenaltyDAO() {
        return penaltyDAO;
    }
}
