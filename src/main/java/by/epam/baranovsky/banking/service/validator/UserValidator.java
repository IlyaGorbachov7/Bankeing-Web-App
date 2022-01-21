package by.epam.baranovsky.banking.service.validator;

import by.epam.baranovsky.banking.entity.User;
import org.apache.commons.validator.routines.EmailValidator;


public class UserValidator extends AbstractValidator {

    private static final String PASSPORT_NUMBER_PATTERN ="[0-9]{7}";
    private static final String PASSPORT_SERIES_PATTERN ="[A-Z]{2}";

    public boolean validate(User user) {

        if(!notNull(user.getFirstName(), user.getLastName(), user.getRoleId(),
                user.getEmail(), user.getPassportNumber(), user.getPassportSeries(),
                user.getBirthDate(), user.getPassword())){
            return false;
        }

        return EmailValidator.getInstance().isValid(user.getEmail())
                && user.getPassportNumber().matches(PASSPORT_NUMBER_PATTERN)
                && user.getPassportSeries().matches(PASSPORT_SERIES_PATTERN);
    }


}
