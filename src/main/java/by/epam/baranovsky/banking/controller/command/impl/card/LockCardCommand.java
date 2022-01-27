package by.epam.baranovsky.banking.controller.command.impl.card;

import by.epam.baranovsky.banking.constant.CommandName;
import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.constant.Message;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionParamName;
import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.entity.BankingCard;
import by.epam.baranovsky.banking.service.BankCardService;
import by.epam.baranovsky.banking.service.exception.ServiceException;
import by.epam.baranovsky.banking.service.impl.BankCardServiceImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LockCardCommand extends AbstractCommand {

    private static final String REDIRECT_TO_CARDS=String.format(
            "%s?%s=%s",
            RequestParamName.CONTROLLER,
            RequestParamName.COMMAND_NAME,
            CommandName.GOTO_CARDS);


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            BankingCard card = cardService.findById(Integer.valueOf(request.getParameter(RequestParamName.CARD_ID)));

            if(!isUserValid(request, card)){
                request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.CARD_NOT_YOURS);
                RequestDispatcher dispatcher = request.getRequestDispatcher(REDIRECT_TO_CARDS);
                dispatcher.forward(request, response);
                return;
            }

            card.setStatusId(DBMetadata.CARD_STATUS_LOCKED);
            cardService.update(card);
            response.sendRedirect(REDIRECT_TO_CARDS);
        } catch (ServiceException e) {
            logger.error(e);
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.ERROR_PAGE);
            dispatcher.forward(request, response);
        }
    }


    private boolean isUserValid(HttpServletRequest request, BankingCard card){
        Integer userId = (Integer) request.getSession().getAttribute(SessionParamName.USER_ID);
        return card.getUserId().equals(userId);
    }
}
