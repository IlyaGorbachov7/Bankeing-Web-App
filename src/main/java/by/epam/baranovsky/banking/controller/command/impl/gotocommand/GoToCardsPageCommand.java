package by.epam.baranovsky.banking.controller.command.impl.gotocommand;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.SessionParamName;
import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.entity.BankingCard;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GoToCardsPageCommand extends AbstractCommand {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute(SessionParamName.USER_ID);
        try{
            List<BankingCard> userCards = cardService.findByUser(userId);
            userCards.removeIf(card ->
                    card.getStatusId().equals(DBMetadata.CARD_STATUS_LOCKED)
                            || card.getStatusId().equals(DBMetadata.CARD_STATUS_EXPIRED)
                            || card.getStatusId().equals(DBMetadata.CARD_STATUS_PENDING));
            for(BankingCard card : userCards){
                card.setNumber(maskNumber(card.getNumber()));
            }
            request.setAttribute(RequestAttributeNames.USER_CARDS, userCards);
            request.setAttribute(RequestAttributeNames.USER_ACCOUNTS, getAccountNumbers(userId));
        }catch (ServiceException e){
            logger.error(e);
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.ERROR_PAGE);
            dispatcher.forward(request, response);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.CARDS_PAGE);
        dispatcher.forward(request, response);
    }

    private List<String> getAccountNumbers(Integer userId) throws ServiceException {
        List<String> numbers = new ArrayList<>();
        for(Account account : accountService.findByUserId(userId)){
            if(account.getStatusId().equals(DBMetadata.ACCOUNT_STATUS_PENDING)
                    || account.getStatusId().equals(DBMetadata.ACCOUNT_STATUS_BLOCKED)){
                continue;
            }
            numbers.add(account.getAccountNumber());
        }
        return numbers;
    }
}
