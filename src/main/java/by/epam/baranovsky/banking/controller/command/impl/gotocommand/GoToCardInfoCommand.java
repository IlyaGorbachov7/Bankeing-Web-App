package by.epam.baranovsky.banking.controller.command.impl.gotocommand;

import by.epam.baranovsky.banking.constant.CommandName;
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
import java.util.List;

public class GoToCardInfoCommand extends AbstractCommand {

    private static final String REDIRECT_TO_CARDS=String.format(
            "%s?%s=%s",
            RequestParamName.CONTROLLER,
            RequestParamName.COMMAND_NAME,
            CommandName.GOTO_ACCOUNTS);

    public static final BankCardService cardService = BankCardServiceImpl.getInstance();
    private static final AccountService accountService = AccountServiceImpl.getInstance();
    private static final UserService userService = UserServiceImpl.getInstance();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int cardId = Integer.parseInt(request.getParameter(RequestParamName.CARD_ID));

        if(cardId >= 0){
            try{
                BankingCard card = cardService.findById(cardId);
                Integer currentUserId = (Integer) request.getSession().getAttribute(SessionParamName.USER_ID);

                if(!canAccessInfo(currentUserId, card)){
                    request.getRequestDispatcher(PageUrls.ERROR_PAGE).forward(request, response);
                    return;
                }

                request.setAttribute(RequestAttributeNames.CARD_DATA, card);
                request.setAttribute(RequestAttributeNames.CARD_USER, userService.getById(card.getUserId()));
                request.setAttribute(RequestAttributeNames.CARD_ACCOUNT, accountService.findById(card.getAccountId()));
                request.getRequestDispatcher(PageUrls.CARD_INFO_PAGE).forward(request, response);
            } catch (ServiceException e) {
                logger.error(e);
                request.getRequestDispatcher(PageUrls.ERROR_PAGE).forward(request, response);
            }
        }else {
            request.getRequestDispatcher(PageUrls.ERROR_PAGE).forward(request, response);
        }

    }

    public boolean canAccessInfo(Integer currentUser, BankingCard card) throws ServiceException {
        Account cardAccount = accountService.findById(card.getAccountId());

        return card.getUserId().equals(currentUser)
                || accountService.findUsers(cardAccount.getId()).contains(currentUser);
    }
}
