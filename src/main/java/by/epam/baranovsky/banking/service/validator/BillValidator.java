package by.epam.baranovsky.banking.service.validator;

import by.epam.baranovsky.banking.entity.Bill;

/**
 * A validator class for Bill entity.
 *
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class BillValidator extends AbstractValidator {

    /**
     * Validates Bill entity.
     * @param william Bill to validate.
     * @return {@code false} if:
     * <ul>
     *     <li>Issue date, status id, user id, payment account id or value are null</li>
     *     <li>Value is negative</li>
     * </ul>
     * {@code true} otherwise
     */
    public boolean validate(Bill william){

        if(!notNull( william.getIssueDate(), william.getStatusId(),
                william.getUserId(), william.getPaymentAccountId(),
                william.getValue())){
            return false;
        }

        return william.getValue()>=0;
    }

}
