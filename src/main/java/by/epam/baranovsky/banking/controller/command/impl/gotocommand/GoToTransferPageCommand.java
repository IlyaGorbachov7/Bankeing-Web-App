package by.epam.baranovsky.banking.controller.command.impl.gotocommand;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.constant.Message;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionAttributeName;
import by.epam.baranovsky.banking.entity.*;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.entity.criteria.Range;
import by.epam.baranovsky.banking.entity.criteria.SingularValue;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of Command
 * used to forward user to the transfer page.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class GoToTransferPageCommand extends AbstractCommand {

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer currentUserId = (Integer) request.getSession().getAttribute(SessionAttributeName.USER_ID);

        try {
            setTargetAccIfIsThere(request);
            request.setAttribute(
                    RequestAttributeNames.USER_CARDS,
                    getUserCardsUpForTransferWithBalance(currentUserId));
            request.setAttribute(
                    RequestAttributeNames.USER_ACCOUNTS,
                    getUserAccountUpForTransferWithBalance(currentUserId));
            request.setAttribute(RequestAttributeNames.PREV_PAGE,
                    getPreviousRequestAddress(request));
            request.getRequestDispatcher(PageUrls.TRANSFER_PAGE).forward(request,response);

        } catch (ServiceException e) {
            logger.error(e);
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.ERROR_PAGE);
            dispatcher.forward(request, response);
        }

    }

    /**
     * Retrieves all cards of the user that can be used for transfer.
     * @param userId ID of the user.
     * @return Map of cards of the user that can be used for transfer,
     * where key is the balance of the cards and value is instance of BankingCard.
     * @throws ServiceException
     */
    private Map<String, BankingCard> getUserCardsUpForTransferWithBalance(Integer userId) throws ServiceException {
        List<BankingCard> cards = cardService.findByUser(userId);
        cards.removeIf(card -> !card.getStatusId().equals(DBMetadata.CARD_STATUS_UNLOCKED));
        Map<String, BankingCard> resultMap = new HashMap<>();
        for(BankingCard card : cards){
            if(card.getAccountId() != null
                    && accountService.findById(card.getAccountId())
                    .getStatusId().equals(DBMetadata.ACCOUNT_STATUS_BLOCKED)){
                continue;
            }

            String balance;
            card.setNumber(maskCardNumber(card.getNumber()));
            if(card.getCardTypeId().equals(DBMetadata.CARD_TYPE_DEBIT)){
                balance = accountService.findById(card.getAccountId()).getBalance().toString();
            } else if(card.getCardTypeId().equals(DBMetadata.CARD_TYPE_CREDIT)){
                balance = card.getBalance().toString();
            }else{
                balance = accountService.findById(card.getAccountId()).getBalance() + " + " + getUnspentOverdraft(card) + " overdraft";
            }
            resultMap.put(balance, card);

        }
        return resultMap;
    }

    /**
     * Retrieves all accounts of the user that can be used for transfer.
     * @param userId ID of the user.
     * @return Map of accounts of the user that can be used for transfer,
     * where key is the balance of the account and value is instance of Account.
     * @throws ServiceException
     */
    private Map<String, Account> getUserAccountUpForTransferWithBalance(Integer userId) throws ServiceException {
        List<Account> accounts = accountService.findByUserId(userId);
        accounts.removeIf(account -> !account.getStatusId().equals(DBMetadata.ACCOUNT_STATUS_UNLOCKED));
        Map<String, Account> resultMap = new HashMap<>();
        for(Account account : accounts){
            resultMap.put(account.getBalance().toString(), account);
        }
        return resultMap;
    }

    /**
     * Calculates overdraft sum that can be spent from a card.
     * @param card Card to check.
     * @return Sum available for overdraft.
     * @throws ServiceException
     */
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

    /**
     * If target account of a transfer is predetermined by some means,
     * puts its data into request. Target account can be predetermined by
     * bill id or penalty id parameters.
     * @param request Servlet request.
     * @throws ServiceException
     */
    private void setTargetAccIfIsThere(HttpServletRequest request) throws ServiceException {
        String billId = request.getParameter(RequestParamName.BILL_ID);
        String penaltyId = request.getParameter(RequestParamName.PENALTY_ID);

        if(billId != null && !billId.isEmpty() && penaltyId != null && !penaltyId.isEmpty()){
            request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.PENALTY_BILL_INTERSECTION);
            return;
        }

        Account account = null;
        if(billId != null  && !billId.isEmpty()){
            Bill bill = billService.findById(Integer.valueOf(billId));
            account = accountService.findById(bill.getPaymentAccountId());
            request.setAttribute(RequestAttributeNames.TRANSFER_VALUE, bill.getValue());
        }
        if(penaltyId != null && !penaltyId.isEmpty()){
            Penalty penalty = penaltyService.findById(Integer.valueOf(penaltyId));
            if(penalty.getTypeId().equals(DBMetadata.PENALTY_TYPE_FEE)){
                account = accountService.findById(penalty.getPaymentAccountId());
                request.setAttribute(RequestAttributeNames.TRANSFER_VALUE, penalty.getValue());
            }
        }
        request.setAttribute(RequestAttributeNames.ACCOUNT_DATA, account);
        request.setAttribute(RequestAttributeNames.BILL_ID, billId);
        request.setAttribute(RequestAttributeNames.PENALTY_ID, penaltyId);

    }

}
