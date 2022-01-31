package by.epam.baranovsky.banking.service.factory.impl;

import by.epam.baranovsky.banking.service.*;
import by.epam.baranovsky.banking.service.factory.ServiceFactory;
import by.epam.baranovsky.banking.service.impl.*;

public class SqlServiceFactory implements ServiceFactory {

    private static volatile SqlServiceFactory instance;
    private final UserService userService = new UserServiceImpl();
    private final AccountService accountService = new AccountServiceImpl();
    private final BankCardService cardService = new BankCardServiceImpl();
    private final BillService billService = new BillServiceImpl();
    private final LoanService loanService = new LoanServiceImpl();
    private final PenaltyService penaltyService = new PenaltyServiceImpl();
    private final OperationService operationService = new OperationServiceImpl();

    private SqlServiceFactory(){}

    public static SqlServiceFactory getInstance(){
        if (instance == null) {
            synchronized (SqlServiceFactory.class) {
                if (instance == null) {
                    instance = new SqlServiceFactory();
                }
            }
        }
        return instance;
    }


    @Override
    public UserService getUserService() {
        return userService;
    }

    @Override
    public BankCardService getBankCardService() {
        return cardService;
    }

    @Override
    public AccountService getAccountService() {
        return accountService;
    }

    @Override
    public BillService getBillService() {
        return billService;
    }

    @Override
    public OperationService getOperationService() {
        return operationService;
    }

    @Override
    public LoanService getLoanService() {
        return loanService;
    }

    @Override
    public PenaltyService getPenaltyService() {
        return penaltyService;
    }
}
