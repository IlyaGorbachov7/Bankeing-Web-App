package by.epam.baranovsky.banking.controller.command;

import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionAttributeName;
import by.epam.baranovsky.banking.service.*;
import by.epam.baranovsky.banking.service.factory.ServiceFactory;
import by.epam.baranovsky.banking.service.factory.impl.SqlServiceFactory;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * Abstract implementation of Command
 * that contains instances of Service classes
 * and some methods that are used by many of subclasses.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public abstract class AbstractCommand implements Command{

    /** Service factory. */
    private static final ServiceFactory FACTORY = SqlServiceFactory.getInstance();

    protected static final UserService userService = FACTORY.getUserService();
    protected static final BillService billService = FACTORY.getBillService();
    protected static final LoanService loanService = FACTORY.getLoanService();
    protected static final AccountService accountService = FACTORY.getAccountService();
    protected static final OperationService operationService = FACTORY.getOperationService();
    protected static final PenaltyService penaltyService = FACTORY.getPenaltyService();
    protected static final BankCardService cardService = FACTORY.getBankCardService();

    protected static final Logger logger = Logger.getLogger(AbstractCommand.class);

    /**
     * Retrieves and rebuilds previous request
     * which is put into session by PreviousRequestFilter.
     * @param request Servlet request.
     * @return String representation of previous request.
     * @throws IOException
     * @see by.epam.baranovsky.banking.controller.filter.PreviousRequestFilter
     */
    protected String getPreviousRequestAddress(HttpServletRequest request) throws IOException {

        Map<String, String[]> prevRequestParams =
                (Map<String, String[]>) request.getSession().getAttribute(SessionAttributeName.LAST_REQUEST);

        StringBuilder requestBuilder = new StringBuilder();
        requestBuilder.append(RequestParamName.CONTROLLER).append('?');
        for (Map.Entry<String, String[]> parameter : prevRequestParams.entrySet()){
            requestBuilder.append(parameter.getKey()).append('=');
            for (String parameterValue : parameter.getValue()){
                requestBuilder.append(parameterValue).append(',');
            }
            requestBuilder.deleteCharAt(requestBuilder.lastIndexOf(","));
            requestBuilder.append('&');
        }
        requestBuilder.deleteCharAt(requestBuilder.lastIndexOf("&"));
        return requestBuilder.toString();
    }

    /**
     * Applies a mask to card number. Used for outputting numbers of bank cards.
     * @param number Number of bank card.
     * @return Masked number.
     */
    protected String maskCardNumber(String number){
        StringBuffer buffer = new StringBuffer(number);

        buffer.replace(2, 12, "**********");
        return buffer.toString();
    }
}
