package by.epam.baranovsky.banking.service.validator;

import by.epam.baranovsky.banking.entity.User;
import org.apache.commons.validator.routines.EmailValidator;

/**
 * A validator class for User entity.
 *
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class UserValidator extends AbstractValidator {

    /**
     * Pattern for passport number.
     */
    private static final String PASSPORT_NUMBER_PATTERN ="[0-9]{7}";
    /**
     * Pattern for passport series.
     */
    private static final String PASSPORT_SERIES_PATTERN ="[A-Z]{2}";

    /**
     * Validates User entity.
     * @param user User to validate.
     * @return {@code false} if:
     * <ul>
     *     <li>Object itself is null</li>
     *     <li>First name, last name, role id, email, passport number, passport series,
     *     birth date or password are null.</li>
     *     <li>Email is invalid</li>
     *     <li>Passwordseries or password do not match corresponding patterns</li>
     * </ul>
     * {@code true} otherwise
     */
    public boolean validate(User user) {

        if(!notNull(user, user.getFirstName(), user.getLastName(), user.getRoleId(),
                user.getEmail(), user.getPassportNumber(), user.getPassportSeries(),
                user.getBirthDate(), user.getPassword())){
            return false;
        }

        return EmailValidator.getInstance().isValid(user.getEmail())
                && user.getPassportNumber().matches(PASSPORT_NUMBER_PATTERN)
                && user.getPassportSeries().matches(PASSPORT_SERIES_PATTERN);
    }


}
