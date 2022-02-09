package by.epam.baranovsky.banking.controller.command.impl.employee;

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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Implementation of Command
 * used for updating account info (interest rate, status).
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class UpdateAccount extends AbstractCommand {

    /**
     * {@inheritDoc}
     * <p>
     *     Redirects to previous page in case of success,
     *     forwards there otherwise.
     * </p>
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer accountId = Integer.valueOf(request.getParameter(RequestParamName.ACCOUNT_ID));
        Integer newStatusId = Integer.valueOf(request.getParameter(RequestParamName.ACCOUNT_NEW_STATUS));
        Integer currentUserRole = (Integer) request.getSession().getAttribute(SessionAttributeName.USER_ROLE_ID);

        String newPercentageStr = request.getParameter(RequestParamName.ACC_NEW_INTEREST);
        double newPercentage = 0d;
        if(newPercentageStr != null && !newPercentageStr.isEmpty()){
            newPercentage = Double.parseDouble(newPercentageStr);
        }

        try{
            Account account = accountService.findById(accountId);
            if(checkStatusChangeValidity(currentUserRole, newStatusId, account)){

                if(checkIfItsPendingAccountApproval(newStatusId, account)){
                    account.setStatusId(newStatusId);
                    account.setYearlyInterestRate(newPercentage);
                    accountService.update(account);
                } else{
                    Operation operation = buildStatusChangeOperation(newStatusId, accountId);
                    operationService.create(operation);
                }
                response.sendRedirect(getPreviousRequestAddress(request));
            } else{
                request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.WRONG_NEW_STATUS);
                request.getRequestDispatcher(getPreviousRequestAddress(request)).forward(request, response);
            }
        } catch (ServiceException e) {
            logger.error(e);
            request.getRequestDispatcher(PageUrls.ERROR_PAGE).forward(request, response);
        }
    }

    /**
     * Checks if the update is an approval of a pending account.
     * @param newStatus ID of account new status.
     * @param account Account in question.
     * @return {@code true} if the update is an approval of a pending account,
     * {@code false} otherwise.
     */
    private boolean checkIfItsPendingAccountApproval(Integer newStatus, Account account){

        return newStatus.equals(DBMetadata.ACCOUNT_STATUS_UNLOCKED)
                && account.getStatusId().equals(DBMetadata.ACCOUNT_STATUS_PENDING);
    }

    /**
     * Checks if current user has rights to update passed account.
     * <p>
     *     Employee can only approve pending accounts
     *     and make previously locked accounts suspended.
     * </p>
     * @param role Role ID of current user.
     * @param newStatus ID of new status of the account.
     * @param oldAcc Account in question.
     * @return {@code true} if current user has rights to update passed account,
     * {@code false} otherwise.
     */
    private boolean checkStatusChangeValidity(Integer role, Integer newStatus, Account oldAcc){

        if(role.equals(DBMetadata.USER_ROLE_EMPLOYEE)){
            if(oldAcc.getStatusId().equals(DBMetadata.ACCOUNT_STATUS_PENDING)){
                return true;
            }
            return newStatus.equals(DBMetadata.ACCOUNT_STATUS_SUSPENDED)
                    && !oldAcc.getStatusId().equals(DBMetadata.ACCOUNT_STATUS_BLOCKED);
        }

        return role.equals(DBMetadata.USER_ROLE_ADMIN) && !newStatus.equals(DBMetadata.ACCOUNT_STATUS_PENDING);
    }

    /**
     * Builds an Operation entity for account status change.
     * @param newStatus ID of new account status.
     * @param accId ID of an account subjected to operation.
     * @return Instance of Operation.
     */
    private Operation buildStatusChangeOperation(Integer newStatus, Integer accId){
        Operation operation = new Operation();
        if(newStatus.equals(DBMetadata.ACCOUNT_STATUS_BLOCKED)){
            operation.setTypeId(DBMetadata.OPERATION_TYPE_ACC_LOCK);
        } else if(newStatus.equals(DBMetadata.ACCOUNT_STATUS_SUSPENDED)){
            operation.setTypeId(DBMetadata.OPERATION_TYPE_ACC_SUSP);
        }else if(newStatus.equals(DBMetadata.ACCOUNT_STATUS_UNLOCKED)){
            operation.setTypeId(DBMetadata.OPERATION_TYPE_ACC_UNLOCK);
        }
        operation.setAccountId(accId);
        return operation;
    }
}
