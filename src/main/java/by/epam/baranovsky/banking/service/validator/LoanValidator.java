package by.epam.baranovsky.banking.service.validator;

import by.epam.baranovsky.banking.entity.Loan;

public class LoanValidator extends AbstractValidator{

    public boolean validate(Loan loan){

        if(!notNull(loan.getDueDate(), loan.getIssueDate(),
                loan.getUserId(), loan.getStartingValue(),
                loan.getTotalPaymentValue(), loan.getSinglePaymentValue(),
                loan.getYearlyInterestRate())){
            return false;
        }

        return loan.getStartingValue()>=0
                && loan.getSinglePaymentValue()>=0
                && loan.getTotalPaymentValue()>=0;
    }

}
