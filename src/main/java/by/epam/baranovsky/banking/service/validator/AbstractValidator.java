package by.epam.baranovsky.banking.service.validator;

public class AbstractValidator {

    public boolean notNull(Object... objects){
        for(Object obj : objects){
            if(obj == null){
                return false;
            }
        }
        return true;
    }
}
