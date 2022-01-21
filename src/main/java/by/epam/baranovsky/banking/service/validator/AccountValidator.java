package by.epam.baranovsky.banking.service.validator;

import by.epam.baranovsky.banking.entity.Account;

public class AccountValidator extends AbstractValidator{

    private static final String NUMBER_PATTERN = "[A-Z]{2}[0-9]{18}";

    public boolean validate(Account account){

        if(!notNull(account.getAccountNumber(), account.getBalance(),
                account.getStatusId(), account.getYearlyInterestRate())){
            return false;
        }

        if(account.getUsers()!= null && account.getUsers().isEmpty()){
            return false;
        }

        return account.getBalance()>=0 && validateNumber(account.getAccountNumber());
    }

    public boolean validateNumber(String number){
        return number.matches(NUMBER_PATTERN);
    }


}
