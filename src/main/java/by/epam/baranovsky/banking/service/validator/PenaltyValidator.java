package by.epam.baranovsky.banking.service.validator;

import by.epam.baranovsky.banking.entity.Penalty;

public class PenaltyValidator extends AbstractValidator{

    public boolean validate(Penalty penalty){

        if(!notNull(penalty.getTypeId(), penalty.getUserId(), penalty.getStatusId())){
            return false;
        }

        return penalty.getValue() == null || penalty.getValue() > 0;
    }
}
