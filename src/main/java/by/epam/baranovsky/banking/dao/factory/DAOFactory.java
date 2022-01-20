package by.epam.baranovsky.banking.dao.factory;

import by.epam.baranovsky.banking.dao.*;

public interface DAOFactory {

    UserDAO getUserDAO();

    AccountDAO getAccountDAO();

    OperationDAO getOperationDAO();

    BankCardDAO getBankCardDAO();

    BillDAO getBillDAO();

    LoanDAO getLoanDAO();

    PenaltyDAO getPenaltyDAO();

}
