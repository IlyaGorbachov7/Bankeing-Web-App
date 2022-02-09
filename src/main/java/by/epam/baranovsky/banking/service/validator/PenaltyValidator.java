package by.epam.baranovsky.banking.service.validator;

import by.epam.baranovsky.banking.entity.Penalty;

/**
 * A validator class for Penalty entity.
 *
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class PenaltyValidator extends AbstractValidator{

    /**
     * Validates Penalty entity.
     * @param penalty Penalty to validate.
     * @return {@code false} if:
     * <ul>
     *     <li>Object itself is null</li>
     *     <li>Type id, user id or status id are null.</li>
     *     <li>Penalty value is not null and is negative</li>
     * </ul>
     * {@code true} otherwise
     */
    public boolean validate(Penalty penalty){

        if(!notNull(penalty, penalty.getTypeId(), penalty.getUserId(), penalty.getStatusId())){
            return false;
        }

        return penalty.getValue() == null || penalty.getValue() > 0;
    }
}
