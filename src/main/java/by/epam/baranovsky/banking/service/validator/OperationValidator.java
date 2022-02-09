package by.epam.baranovsky.banking.service.validator;

import by.epam.baranovsky.banking.entity.Operation;

/**
 * A validator class for Operation entity.
 *
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class OperationValidator extends AbstractValidator{

    /**
     * Validates Operation entity.
     * @param operation Operation to validate.
     * @return {@code false} if:
     * <ul>
     *     <li>Object itself is null</li>
     *     <li>Type id is null</li>
     *     <li>Bill id and penalty id are not null at the same time</li>
     *     <li>Value is not null and value is infinite or NaN</li>
     * </ul>
     * {@code true} otherwise
     */
    public boolean validate(Operation operation){

        if(!notNull(operation, operation.getTypeId())){
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
