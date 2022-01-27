package by.epam.baranovsky.banking.controller.command.impl.gotocommand;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.constant.Message;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionParamName;
import by.epam.baranovsky.banking.entity.*;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.entity.criteria.Range;
import by.epam.baranovsky.banking.entity.criteria.SingularValue;
import by.epam.baranovsky.banking.service.*;
import by.epam.baranovsky.banking.service.exception.ServiceException;
import by.epam.baranovsky.banking.service.impl.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoToTransferPageCommand extends AbstractCommand {


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer currentUserId = (Integer) request.getSession().getAttribute(SessionParamName.USER_ID);

        try {
            setTargetAccIfIsThere(request);
            request.setAttribute(
                    RequestAttributeNames.USER_CARDS,
                    getUserCardsUpForTransferWithBalance(currentUserId));
            request.setAttribute(
                    RequestAttributeNames.USER_ACCOUNTS,
                    getUserAccountUpForTransferWithBalance(currentUserId));
            request.getRequestDispatcher(PageUrls.TRANSFER_PAGE).forward(request,response);
        } catch (ServiceException e) {
            logger.error(e);
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.ERROR_PAGE);
            dispatcher.forward(request, response);
        }

    }

    private Map<BankingCard, String> getUserCardsUpForTransferWithBalance(Integer userId) throws ServiceException {
        List<BankingCard> cards = cardService.findByUser(userId);
        cards.removeIf(card -> !card.getStatusId().equals(DBMetadata.CARD_STATUS_UNLOCKED));

        Map<BankingCard, String> resultMap = new HashMap<>();
        for(BankingCard card : cards){
            String balance;
            card.setNumber(maskNumber(card.getNumber()));
            if(card.getCardTypeId().equals(DBMetadata.CARD_TYPE_DEBIT)){
                balance = accountService.findById(card.getAccountId()).getBalance().toString();
            } else if(card.getCardTypeId().equals(DBMetadata.CARD_TYPE_CREDIT)){
                balance = card.getBalance().toString();
            }else{
                balance = accountService.findById(card.getAccountId()).getBalance() + " + " + getUnspentOverdraft(card) + " overdraft";
            }
            resultMap.put(card, balance);

        }
        return resultMap;
    }

    private Map<Account, String> getUserAccountUpForTransferWithBalance(Integer userId) throws ServiceException {
        List<Account> accounts = accountService.findByUserId(userId);
        accounts.removeIf(account -> !account.getStatusId().equals(DBMetadata.ACCOUNT_STATUS_UNLOCKED));
        Map<Account, String> resultMap = new HashMap<>();
        for(Account account : accounts){
            resultMap.put(account, account.getBalance().toString());
        }
        return resultMap;
    }

    private Double getUnspentOverdraft(BankingCard card) throws ServiceException {
        Double overdraftSum = 0d;
        Criteria<EntityParameters.LoanParams> criteria = new Criteria<>();
        criteria.add(EntityParameters.LoanParams.CARD_ID, new SingularValue<>(card.getId()));
        criteria.add(
                EntityParameters.LoanParams.STATUS,
                new Range<>(DBMetadata.LOAN_STATUS_PENDING, DBMetadata.LOAN_STATUS_OVERDUE));

        List<Loan> loans = loanService.findByCriteria(criteria);
        for(Loan loan : loans){
            overdraftSum += loan.getStartingValue();
        }

        return card.getOverdraftMax()-overdraftSum;
    }

    private void setTargetAccIfIsThere(HttpServletRequest request) throws ServiceException {
        String billId = request.getParameter(RequestParamName.BILL_ID);
        String penaltyId = request.getParameter(RequestParamName.PENALTY_ID);

        if(billId != null && penaltyId != null){
            request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.PENALTY_BILL_INTERSECTION);
            return;
        }

        Account account = null;
        if(billId != null){
            Bill bill = billService.findById(Integer.valueOf(billId));
            account = accountService.findById(bill.getPaymentAccountId());
        }
        if(penaltyId != null){
            Penalty penalty = penaltyService.findById(Integer.valueOf(penaltyId));
            if(penalty.getTypeId().equals(DBMetadata.PENALTY_TYPE_FEE)){
                account = accountService.findById(penalty.getPaymentAccountId());
            }
        }
        request.setAttribute(RequestAttributeNames.ACCOUNT_DATA, account);

    }

}
