package by.epam.baranovsky.banking.service.validator;

import by.epam.baranovsky.banking.entity.BankingCard;

public class BankCardValidator extends AbstractValidator{

    private static final String NUMBER_PATTERN = "[0-9]{16}";

    public boolean validate(BankingCard card){

        if(!notNull(card.getCardTypeId(), card.getStatusId(),
                card.getCvc(), card.getNumber(),
                card.getExpirationDate(), card.getRegistrationDate(),
                card.getPin(), card.getUserId())){
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

        return card.getPin()>0 && card.getPin()<10000
                && card.getCvc()>0 && card.getCvc()<1000
                && card.getNumber().matches(NUMBER_PATTERN);
    }
}
