package by.epam.baranovsky.banking.controller.command.impl.gotocommand;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.constant.Message;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionParamName;
import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.entity.BankingCard;
import by.epam.baranovsky.banking.entity.Loan;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.entity.criteria.Range;
import by.epam.baranovsky.banking.entity.criteria.SingularValue;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class GoToCardInfoCommand extends AbstractCommand {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int cardId = Integer.parseInt(request.getParameter(RequestParamName.CARD_ID));

        if(cardId > 0){
            try{
                BankingCard card = cardService.findById(cardId);
                Integer currentUserId = (Integer) request.getSession().getAttribute(SessionParamName.USER_ID);

                if(!canAccessInfo(currentUserId, card)){
                    request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.CANT_ACCESS_CARD_INFO);
                    request.getRequestDispatcher(PageUrls.CARDS_PAGE).forward(request, response);
                    return;
                }

                if(!currentUserId.equals(card.getUserId())){
                    card.setPin("");
                    card.setCvc("");
                }

                if(card.getCardTypeId().equals(DBMetadata.CARD_TYPE_OVERDRAFT)){
                    request.setAttribute(RequestAttributeNames.CARD_OVERDRAFTED, getOverdraftSum(card));
                }

                request.setAttribute(RequestAttributeNames.CARD_DATA, card);
                request.setAttribute(RequestAttributeNames.CARD_USER, userService.getById(card.getUserId()));
                request.setAttribute(RequestAttributeNames.CARD_ACCOUNT, accountService.findById(card.getAccountId()));
                request.setAttribute(RequestAttributeNames.PREVIOUS_PAGE, getPreviousRequestAddress(request));
                request.getRequestDispatcher(PageUrls.CARD_INFO_PAGE).forward(request, response);
            } catch (ServiceException e) {
                logger.error(e);
                request.getRequestDispatcher(PageUrls.ERROR_PAGE).forward(request, response);
            }
        }else {
            request.getRequestDispatcher(PageUrls.ERROR_PAGE).forward(request, response);
        }

    }

    private boolean canAccessInfo(Integer currentUser, BankingCard card) throws ServiceException {
        Account cardAccount = accountService.findById(card.getAccountId());

        return card.getUserId().equals(currentUser)
                || accountService.findUsers(cardAccount.getId()).contains(currentUser);
    }

    private Double getOverdraftSum(BankingCard card) throws ServiceException {
        Double result = 0d;
        Criteria<EntityParameters.LoanParams> criteria = new Criteria<>();
        criteria.add(EntityParameters.LoanParams.CARD_ID, new SingularValue<>(card.getId()));
        criteria.add(
                EntityParameters.LoanParams.STATUS,
                new Range<>(DBMetadata.LOAN_STATUS_PENDING, DBMetadata.LOAN_STATUS_OVERDUE));

        List<Loan> loans = loanService.findByCriteria(criteria);
        for(Loan loan : loans){
            result += loan.getStartingValue();
        }

        return result;
    }
}
