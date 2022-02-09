package by.epam.baranovsky.banking.controller.command.impl.employee;

import by.epam.baranovsky.banking.constant.CommandName;
import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.constant.Message;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Implementation of Command
 * used for deleting account creation requests.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class DeletePendingAccountCommand extends AbstractCommand {

    private static final String BACK_TO_PENDING_REQUESTS=String.format("%s?%s=%s",
            RequestParamName.CONTROLLER, RequestParamName.COMMAND_NAME,
            CommandName.GOTO_PENDING_ACCOUNTS);

    /**
     * {@inheritDoc}
     * <p>
     *     Forwards back to pending account requests page in case of failure,
     *     redirects there otherwise.
     * </p>
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer accId = Integer.valueOf(request.getParameter(RequestParamName.ACCOUNT_ID));

        try{
            Account account = accountService.findById(accId);
            if(!checkIfCanDelete(account)){
                request.setAttribute(RequestAttributeNames.ERROR_MSG,Message.ACCOUNT_NOT_PENDING);
                request.getRequestDispatcher(PageUrls.PENDING_ACCS_PAGE).forward(request,response);
            } else{
                accountService.delete(account.getId());
                response.sendRedirect(BACK_TO_PENDING_REQUESTS);
            }
        } catch (ServiceException e) {
            logger.error(e);
            request.getRequestDispatcher(PageUrls.ERROR_PAGE).forward(request, response);
        }
    }

    /**
     * Checks if account that is being deleted has 'pending' status.
     * @param account Account to check.
     * @return {@code true} if account that is being deleted has 'pending' status,
     * {@code false} otherwise.
     */
    public boolean checkIfCanDelete(Account account){
        return account.getStatusId().equals(DBMetadata.ACCOUNT_STATUS_PENDING);
    }
}
