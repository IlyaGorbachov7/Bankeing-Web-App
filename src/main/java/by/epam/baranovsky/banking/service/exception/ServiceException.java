package by.epam.baranovsky.banking.service.exception;

/**
 * Service exception class.
 *
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class ServiceException extends Exception {

    private static final long serialVersionUID = 1L;

    public ServiceException() {
        super();
    }

    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

}