package by.epam.baranovsky.banking.service.validator;

import by.epam.baranovsky.banking.entity.Loan;
import by.epam.baranovsky.banking.service.exception.ServiceException;

/**
 * A validator class for Loan entity.
 *
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class LoanValidator extends AbstractValidator{

    /**
     * Validates Loan entity.
     * @param loan Loan to validate.
     * @return {@code false} if:
     * <ul>
     *     <li>Object itself is null</li>
     *     <li>Due date, issue date, user id, starting value, total payment value,
     *     single payment value, interest rate or account id are null</li>
     *     <li>Starting value, single payment value,
     *     interest rate or total payment value are negative</li>
     * </ul>
     * {@code true} otherwise
     */
    public boolean validate(Loan loan) throws ServiceException {

        if(!notNull(loan, loan.getDueDate(), loan.getIssueDate(),
                loan.getUserId(), loan.getStartingValue(),
                loan.getTotalPaymentValue(), loan.getSinglePaymentValue(),
                loan.getYearlyInterestRate(), loan.getAccountId())){
            return false;
        }

        return loan.getStartingValue()>=0
                && loan.getSinglePaymentValue()>=0
                && loan.getTotalPaymentValue()>=0
                && loan.getYearlyInterestRate()>=0;
    }

}
