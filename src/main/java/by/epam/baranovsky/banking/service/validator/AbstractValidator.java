package by.epam.baranovsky.banking.service.validator;

/**
 * Abstract parent class for service validators.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public abstract class AbstractValidator {

    /**
     * Checks if all passed objects are not null.
     * @param objects Objects to check
     * @return {@code true} if all passed objects are not null, {@code false} otherwise
     */
    public boolean notNull(Object... objects){
        for(Object obj : objects){
            if(obj == null){
                return false;
            }
        }
        return true;
    }
}
