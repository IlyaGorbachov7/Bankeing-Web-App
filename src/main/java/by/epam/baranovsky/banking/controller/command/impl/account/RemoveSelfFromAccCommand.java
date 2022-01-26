package by.epam.baranovsky.banking.controller.command.impl.account;

import by.epam.baranovsky.banking.constant.CommandName;
import by.epam.baranovsky.banking.constant.Message;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionParamName;
import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.service.AccountService;
import by.epam.baranovsky.banking.service.exception.ServiceException;
import by.epam.baranovsky.banking.service.impl.AccountServiceImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class RemoveSelfFromAccCommand extends AbstractCommand {

    private static final String REDIRECT_TO_ACCS=String.format(
            "%s?%s=%s",
            RequestParamName.CONTROLLER,
            RequestParamName.COMMAND_NAME,
            CommandName.GOTO_ACCOUNTS);
    private static final AccountService accountService = AccountServiceImpl.getInstance();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer userId = (Integer) request.getSession().getAttribute(SessionParamName.USER_ID);

        try{
            if(isOnlyUser(userId, Integer.valueOf(request.getParameter(RequestParamName.ACCOUNT_ID)))){
                request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.ONLY_USER);
                RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.ERROR_PAGE);
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

    private boolean isOnlyUser(Integer userId, Integer accountId) throws ServiceException {
        List<Integer> users = accountService.findUsers(accountId);
        if(users.contains(userId) && users.size()==1){
            return true;
        }
        return false;
    }
}
