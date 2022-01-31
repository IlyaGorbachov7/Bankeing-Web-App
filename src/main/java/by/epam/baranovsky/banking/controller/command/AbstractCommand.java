package by.epam.baranovsky.banking.controller.command;

import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionParamName;
import by.epam.baranovsky.banking.service.*;
import by.epam.baranovsky.banking.service.factory.ServiceFactory;
import by.epam.baranovsky.banking.service.factory.impl.SqlServiceFactory;
import by.epam.baranovsky.banking.service.impl.*;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;


public abstract class AbstractCommand implements Command{

    private static final ServiceFactory FACTORY = SqlServiceFactory.getInstance();

    protected static final UserService userService = FACTORY.getUserService();
    protected static final BillService billService = FACTORY.getBillService();
    protected static final LoanService loanService = FACTORY.getLoanService();
    protected static final AccountService accountService = FACTORY.getAccountService();
    protected static final OperationService operationService = FACTORY.getOperationService();
    protected static final PenaltyService penaltyService = FACTORY.getPenaltyService();
    protected static final BankCardService cardService = FACTORY.getBankCardService();

    protected static final Logger logger = Logger.getLogger(AbstractCommand.class);


    protected String getPreviousRequestAddress(HttpServletRequest request) throws IOException {

        Map<String, String[]> prevRequestParams =
                (Map<String, String[]>) request.getSession().getAttribute(SessionParamName.LAST_REQUEST);

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

    protected String maskNumber(String number){
        StringBuffer buffer = new StringBuffer(number);

        buffer.replace(2, 12, "**********");
        return buffer.toString();
    }
}
