package by.epam.baranovsky.banking.controller.command.impl.account;

import by.epam.baranovsky.banking.constant.CommandName;
import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.constant.Message;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionAttributeName;
import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Implementation of Command
 * used for removing current user
 * from account's list of users.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class RemoveSelfFromAccCommand extends AbstractCommand {

    private static final String REDIRECT_TO_ACCS=String.format(
            "%s?%s=%s",
            RequestParamName.CONTROLLER,
            RequestParamName.COMMAND_NAME,
            CommandName.GOTO_ACCOUNTS);

    /**
     * {@inheritDoc}
     * <p>
     *     Forwards to previous request in case of failure,
     *     redirects to previous request otherwise.
     * </p>
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer userId = (Integer) request.getSession().getAttribute(SessionAttributeName.USER_ID);

        Integer accountId = Integer.valueOf(request.getParameter(RequestParamName.ACCOUNT_ID));
        try{
            if(accountService.findById(accountId).getStatusId().equals(DBMetadata.ACCOUNT_STATUS_BLOCKED)){
                request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.ACCOUNT_LOCKED);
                RequestDispatcher dispatcher = request.getRequestDispatcher(getPreviousRequestAddress(request));
                dispatcher.forward(request, response);
                return;
            }

            if(isOnlyUser(userId, accountId)){
                request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.ONLY_USER);
                RequestDispatcher dispatcher = request.getRequestDispatcher(getPreviousRequestAddress(request));
                dispatcher.forward(request, response);
                return;
            }

            Account account = accountService.findById(Integer.valueOf(request.getParameter(RequestParamName.ACCOUNT_ID)));
            account.setUsers(accountService.findUsers(account.getId()));
            account.removeUser(userId);
            accountService.update(account);
            response.sendRedirect(REDIRECT_TO_ACCS);
        }catch (ServiceException e){
            logger.error(e);
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.ERROR_PAGE);
            dispatcher.forward(request, response);
        }
    }

    /**
     * Checks if current user is the only user left in account's list of users.
     * @param userId ID of current user.
     * @param accountId ID of the account in question.
     * @return {@code true} if current user is the only user left in account's list of users,
     * {@code false} otherwise.
     * @throws ServiceException
     */
    private boolean isOnlyUser(Integer userId, Integer accountId) throws ServiceException {
        List<Integer> users = accountService.findUsers(accountId);
        if(users.contains(userId) && users.size()==1){
            return true;
        }
        return false;
    }
}
