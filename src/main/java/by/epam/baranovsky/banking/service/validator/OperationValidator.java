package by.epam.baranovsky.banking.service.validator;

import by.epam.baranovsky.banking.entity.Operation;

public class OperationValidator extends AbstractValidator{

    public boolean validate(Operation operation){

        if(!notNull(operation.getTypeId())){
            return false;
        }

        if(operation.getBillId() != null && operation.getPenaltyId() != null){
            return false;
        }

        return operation.getValue() == null
                || !operation.getValue().isInfinite()
                || !operation.getValue().isNaN();
    }
}
