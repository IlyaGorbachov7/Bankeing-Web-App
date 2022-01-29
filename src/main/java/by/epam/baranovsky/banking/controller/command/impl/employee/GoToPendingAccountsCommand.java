package by.epam.baranovsky.banking.controller.command.impl.employee;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class GoToPendingAccountsCommand extends AbstractCommand {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{

            request.setAttribute(
                    RequestAttributeNames.PENDING_ACCS,
                    getAccountsWithUsers());
            request.getRequestDispatcher(PageUrls.PENDING_ACCS_PAGE).forward(request,response);
        }catch (ServiceException e) {
            logger.error(e);
            request.getRequestDispatcher(PageUrls.ERROR_PAGE).forward(request, response);
        }
    }

    private List<Account> getAccountsWithUsers() throws ServiceException {
        List<Account> accountList = accountService.findByStatusId(DBMetadata.ACCOUNT_STATUS_PENDING);
        for(Account account : accountList){
            account.setUsers(accountService.findUsers(account.getId()));
        }
        return accountList;
    }

}
