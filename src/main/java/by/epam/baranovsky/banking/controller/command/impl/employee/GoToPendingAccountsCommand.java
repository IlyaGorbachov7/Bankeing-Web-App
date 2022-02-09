package by.epam.baranovsky.banking.controller.command.impl.employee;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.SessionAttributeName;
import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Implementation of Command
 * used for forwarding user to the page that lists
 * all accounts that users have requested to create for them.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class GoToPendingAccountsCommand extends AbstractCommand {

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            Integer currentUserId = (Integer) request.getSession().getAttribute(SessionAttributeName.USER_ID);
            Integer currentUserRole = (Integer) request.getSession().getAttribute(SessionAttributeName.USER_ROLE_ID);

            List<Account> pendingAccs =  getPendingAccountsWithUsers();
            if(!currentUserRole.equals(DBMetadata.USER_ROLE_ADMIN)){
                pendingAccs.removeIf(account -> account.getUsers().contains(currentUserId));
            }

            request.setAttribute(
                    RequestAttributeNames.PENDING_ACCS,
                    pendingAccs);
            request.getRequestDispatcher(PageUrls.PENDING_ACCS_PAGE).forward(request,response);
        }catch (ServiceException e) {
            logger.error(e);
            request.getRequestDispatcher(PageUrls.ERROR_PAGE).forward(request, response);
        }
    }

    /**
     * Gets all accounts and their users' IDs.
     * @return List of instances of Account.
     * @throws ServiceException
     */
    private List<Account> getPendingAccountsWithUsers() throws ServiceException {
        List<Account> accountList = accountService.findByStatusId(DBMetadata.ACCOUNT_STATUS_PENDING);
        for(Account account : accountList){
            account.setUsers(accountService.findUsers(account.getId()));
        }
        return accountList;
    }

}
