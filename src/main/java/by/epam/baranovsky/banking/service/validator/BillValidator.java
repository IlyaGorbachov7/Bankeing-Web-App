package by.epam.baranovsky.banking.service.validator;

import by.epam.baranovsky.banking.entity.Bill;

public class BillValidator extends AbstractValidator {

    public boolean validate(Bill william){

        if(!notNull( william.getIssueDate(), william.getStatusId(),
                william.getUserId(), william.getPaymentAccountId(),
                william.getValue())){
            return false;
        }



        return william.getValue()>=0;
    }

}
