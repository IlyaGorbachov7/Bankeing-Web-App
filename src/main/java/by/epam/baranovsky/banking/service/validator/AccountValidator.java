package by.epam.baranovsky.banking.service.validator;

import by.epam.baranovsky.banking.entity.Account;

/**
 * A validator class for Account entity.
 *
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class AccountValidator extends AbstractValidator{

    /**
     * Regex pattern for Account number.
     */
    private static final String NUMBER_PATTERN = "[A-Z]{2}[0-9]{18}";

    /**
     * Validates Account entity.
     * @param account Account to validate.
     * @return {@code false} if:
     * <ul>
     *     <li>Object itself is null</li>
     *     <li>Number, balance, status id or interest rate are null.</li>
     *     <li>Account does not have users</li>
     *     <li>Account's number does not match NUMBER_PATTERN</li>
     *     <li>Balance is negative</li>
     * </ul>
     * {@code true} otherwise
     */
    public boolean validate(Account account){

        if(!notNull(account, account.getAccountNumber(), account.getBalance(),
                account.getStatusId(), account.getYearlyInterestRate())){
            return false;
        }
        if(account.getUsers()!= null && account.getUsers().isEmpty()){
            return false;
        }

        return account.getBalance()>=0 && validateNumber(account.getAccountNumber());
    }

    /**
     * Validates account's number separately
     * @param number number to check
     * @return {@code true} if number matches NUMBER_PATTERN, {@code false} otherwise
     */
    public boolean validateNumber(String number){
        return number.matches(NUMBER_PATTERN);
    }


}
