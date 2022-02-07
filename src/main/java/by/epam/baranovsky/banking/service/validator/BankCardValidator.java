package by.epam.baranovsky.banking.service.validator;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.entity.BankingCard;

/**
 * A validator class for BankingCard entity.
 *
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class BankCardValidator extends AbstractValidator{

    /**
     * Regex pattern for bank card number.
     */
    private static final String NUMBER_PATTERN = "[0-9]{16}";
    /**
     * Regex pattern for bank card pin code.
     */
    private static final String PIN_PATTERN = "[0-9]{4}";
    /**
     * Regex pattern for bank card cvc code.
     */
    private static final String CVC_PATTERN = "[0-9]{3}";

    /**
     * Validates BankingCard entity.
     * @param card BankingCard to validate.
     * @return {@code false} if:
     * <ul>
     *     <li>Number, status id, cvc code, pin code, expiration date, registration date are null.</li>
     *     <li>Card is debit or overdraft but has no account tied to it</li>
     *     <li>Card is credit but has no balance</li>
     *     <li>Card if overdraft but has no overdraft limit or interest rate</li>
     *     <li>Balance, interest rate, overdraft limit are negative</li>
     *     <li>Number, cvc, pin do not match corresponding patterns</li>
     * </ul>
     * {@code true} otherwise
     */
    public boolean validate(BankingCard card){

        if(!notNull(card.getCardTypeId(), card.getStatusId(),
                card.getCvc(), card.getNumber(),
                card.getExpirationDate(), card.getRegistrationDate(),
                card.getPin())){
            return false;
        }

        if(card.getCardTypeId().equals(DBMetadata.CARD_TYPE_DEBIT) && card.getAccountId() == null){
            return false;
        }

        if(card.getCardTypeId().equals(DBMetadata.CARD_TYPE_OVERDRAFT) && card.getAccountId() == null){
            return false;
        }

        if(card.getBalance() != null && card.getBalance() <0){
            return false;
        }
        if(card.getOverdraftMax() != null && card.getOverdraftMax() <0){
            return false;
        }
        if(card.getOverdraftInterestRate() != null && card.getOverdraftInterestRate() <0){
            return false;
        }

        return card.getPin().matches(PIN_PATTERN)
                && card.getCvc().matches(CVC_PATTERN)
                && card.getNumber().matches(NUMBER_PATTERN);
    }
}
