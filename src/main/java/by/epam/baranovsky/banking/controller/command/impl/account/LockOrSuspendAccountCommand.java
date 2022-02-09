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
import by.epam.baranovsky.banking.entity.Operation;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Implementation of Command
 * used for locking or suspending own account.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class LockOrSuspendAccountCommand extends AbstractCommand {

    private static final String REDIRECT_TO_ACCS=String.format(
            "%s?%s=%s",
            RequestParamName.CONTROLLER,
            RequestParamName.COMMAND_NAME,
            CommandName.GOTO_ACCOUNTS);

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try{
            Account account = accountService.findById(Integer.valueOf(request.getParameter(RequestParamName.ACCOUNT_ID)));
            Integer newStatus = Integer.valueOf(request.getParameter(RequestParamName.ACCOUNT_NEW_STATUS));
            Integer userId = (Integer) request.getSession().getAttribute(SessionAttributeName.USER_ID);

            if(isNewStatusValid(newStatus) && isUserValid(userId, account)){
                operationService.create(buildOperation(newStatus, account.getId()));
                response.sendRedirect(REDIRECT_TO_ACCS);
            } else{
                request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.WRONG_NEW_STATUS);
                request.getRequestDispatcher(PageUrls.ACCOUNTS_PAGE).forward(request,response);
            }

        } catch (ServiceException e) {
            logger.error(e);
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.ERROR_PAGE);
            dispatcher.forward(request, response);
        }
    }

    /**
     * Checks if new status is valid.
     * @param newStatus ID of new status.
     * @return {@code true if new status is 'locked' or 'suspended'.
     */
    private boolean isNewStatusValid(Integer newStatus){

        return DBMetadata.ACCOUNT_STATUS_BLOCKED.equals(newStatus)
                || DBMetadata.ACCOUNT_STATUS_SUSPENDED.equals(newStatus);
    }

    /**
     * Checks if user can lock or suspend this account.
     * @param userId ID of a user.
     * @param account Account in question.
     * @return {@code true} if user is among account's users, {@code false} otherwise.
     * @throws ServiceException
     */
    private boolean isUserValid(Integer userId, Account account) throws ServiceException {
        return accountService.findUsers(account.getId()).contains(userId);
    }

    /**
     * Creates Operation entity for account update.
     * @param newStatus ID of new status.
     * @param accId ID of account.
     * @return Instance of Operation.
     */
    private Operation buildOperation(Integer newStatus, Integer accId){
        Operation operation = new Operation();
        if(newStatus.equals(DBMetadata.ACCOUNT_STATUS_BLOCKED)){
            operation.setTypeId(DBMetadata.OPERATION_TYPE_ACC_LOCK);
        } else if(newStatus.equals(DBMetadata.ACCOUNT_STATUS_SUSPENDED)){
            operation.setTypeId(DBMetadata.OPERATION_TYPE_ACC_SUSP);
        }
        operation.setAccountId(accId);
        return operation;
    }
}
