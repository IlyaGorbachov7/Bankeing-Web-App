package by.epam.baranovsky.banking.dao.factory;

import by.epam.baranovsky.banking.dao.*;

/**
 * Interface for DAO factory.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public interface DAOFactory {

    UserDAO getUserDAO();

    AccountDAO getAccountDAO();

    OperationDAO getOperationDAO();

    BankCardDAO getBankCardDAO();

    BillDAO getBillDAO();

    LoanDAO getLoanDAO();

    PenaltyDAO getPenaltyDAO();

}
