package by.epam.baranovsky.banking.controller.command.impl.gotocommand;

import by.epam.baranovsky.banking.constant.CommandName;
import by.epam.baranovsky.banking.constant.Message;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionParamName;
import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.entity.BankingCard;
import by.epam.baranovsky.banking.entity.User;
import by.epam.baranovsky.banking.service.AccountService;
import by.epam.baranovsky.banking.service.BankCardService;
import by.epam.baranovsky.banking.service.UserService;
import by.epam.baranovsky.banking.service.exception.ServiceException;
import by.epam.baranovsky.banking.service.impl.AccountServiceImpl;
import by.epam.baranovsky.banking.service.impl.BankCardServiceImpl;
import by.epam.baranovsky.banking.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GoToAccountInfoCommand extends AbstractCommand {

    private static final String REDIRECT_TO_ACCS=String.format(
            "%s?%s=%s",
            RequestParamName.CONTROLLER,
            RequestParamName.COMMAND_NAME,
            CommandName.GOTO_ACCOUNTS);


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int accountId = Integer.parseInt(request.getParameter(RequestParamName.ACCOUNT_ID));

        if(accountId > 0){
            try{
                Account account = accountService.findById(accountId);
                List<Integer> userIdList = accountService.findUsers(accountId);
                Integer currentUserId = (Integer) request.getSession().getAttribute(SessionParamName.USER_ID);
                if(!userIdList.contains(currentUserId)){
                    request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.NOT_YOUR_ACCOUNT);
                    request.getRequestDispatcher(REDIRECT_TO_ACCS).forward(request, response);
                    return;
                }
                request.setAttribute(RequestAttributeNames.ACCOUNT_DATA, account);
                request.setAttribute(RequestAttributeNames.ACCOUNT_USERS_INFO, getUsersInfo(userIdList));
                request.setAttribute(RequestAttributeNames.ACCOUNT_CARDS_INFO, getCardsForThisAccount(accountId));
                request.getRequestDispatcher(PageUrls.ACCOUNT_INFO_PAGE).forward(request, response);
            } catch (ServiceException e) {
                logger.error(e);
                request.getRequestDispatcher(PageUrls.ERROR_PAGE).forward(request, response);
            }
        } else{
            request.getRequestDispatcher(PageUrls.ERROR_PAGE).forward(request, response);

        }

    }

    private List<String> getUsersInfo(List<Integer> userIds) throws ServiceException {
        List<String> userInfo = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for(Integer id: userIds){
            builder.setLength(0);
            User user = userService.getById(id);
            builder.append(user.getFirstName())
                    .append(" ")
                    .append(user.getLastName());

            if(user.getPatronymic() != null){
                builder.append(" ").append(user.getPatronymic());
            }
            userInfo.add(builder.toString());
        }
        return userInfo;
    }

    private List<BankingCard> getCardsForThisAccount(Integer accountId) throws ServiceException{
        List<BankingCard> cards = cardService.findByAccount(accountId);

        for(BankingCard card : cards){
            card.setNumber(maskNumber(card.getNumber()));
        }

        return cards;
    }
}
