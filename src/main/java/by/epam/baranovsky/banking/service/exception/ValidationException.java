package by.epam.baranovsky.banking.service.exception;

import by.epam.baranovsky.banking.constant.Message;

public class ValidationException extends ServiceException{

    public ValidationException() {
        super(Message.WRONG_INPUT);
    }

    public ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }
}
