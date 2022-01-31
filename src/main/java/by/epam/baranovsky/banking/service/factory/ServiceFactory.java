package by.epam.baranovsky.banking.service.factory;

import by.epam.baranovsky.banking.service.*;

public interface ServiceFactory {

    UserService getUserService();

    BankCardService getBankCardService();

    AccountService getAccountService();

    BillService getBillService();

    OperationService getOperationService();

    LoanService getLoanService();

    PenaltyService getPenaltyService();


}
