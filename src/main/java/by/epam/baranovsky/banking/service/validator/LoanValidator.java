package by.epam.baranovsky.banking.service.validator;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.entity.Loan;
import by.epam.baranovsky.banking.service.exception.ServiceException;
import by.epam.baranovsky.banking.service.factory.impl.SqlServiceFactory;

public class LoanValidator extends AbstractValidator{

    public boolean validate(Loan loan) throws ServiceException {

        if(!notNull(loan.getDueDate(), loan.getIssueDate(),
                loan.getUserId(), loan.getStartingValue(),
                loan.getTotalPaymentValue(), loan.getSinglePaymentValue(),
                loan.getYearlyInterestRate(), loan.getAccountId())){
            return false;
        }

        if(SqlServiceFactory.getInstance()
                .getAccountService().findById(DBMetadata.BANK_ACCOUNT_ID)
                .getBalance() < loan.getStartingValue()){
            return false;
        }

        return loan.getStartingValue()>=0
                && loan.getSinglePaymentValue()>=0
                && loan.getTotalPaymentValue()>=0;
    }

}
