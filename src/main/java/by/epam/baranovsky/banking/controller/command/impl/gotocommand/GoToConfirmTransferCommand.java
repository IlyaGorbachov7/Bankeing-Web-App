package by.epam.baranovsky.banking.controller.command.impl.gotocommand;

import by.epam.baranovsky.banking.constant.ConfigManager;
import by.epam.baranovsky.banking.constant.ConfigParams;
import by.epam.baranovsky.banking.constant.Message;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionParamName;
import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.entity.BankingCard;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class GoToConfirmTransferCommand extends AbstractCommand {

    private Double commissionPercentage = Double.valueOf(
            ConfigManager.getInstance().getValue(ConfigParams.TRANSFER_COMISSION));

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer currentUser = (Integer) request.getSession().getAttribute(SessionParamName.USER_ID);
        String targetAccountNumber = request.getParameter(RequestParamName.TRANSFER_TARGET_ACC_NUMBER);
        String targetCardNumber = request.getParameter(RequestParamName.TRANSFER_TARGET_CARD_NUMBER);
        String expireDateString = request.getParameter(RequestParamName.TRANSFER_TARGET_CARD_EXPIRATION);
        String ownAccountId=request.getParameter(RequestParamName.ACCOUNT_ID);
        String ownCardId=request.getParameter(RequestParamName.CARD_ID);
        String valueStr = request.getParameter(RequestParamName.TRANSFER_VALUE);

        String errorMsgForParams = checkParams(valueStr, targetAccountNumber, targetCardNumber, ownAccountId, ownCardId);
        if(errorMsgForParams!=null){
            request.setAttribute(RequestAttributeNames.ERROR_MSG, errorMsgForParams);
            request.getRequestDispatcher(PageUrls.TRANSFER_PAGE).forward(request,response);
            return;
        }

        double value = Double.parseDouble(valueStr);

        try{
            Account ownAccount = null;
            if(ownAccountId != null){
                ownAccount = getOwnAccount(Integer.parseInt(ownAccountId), currentUser);
            }
            BankingCard ownCard = null;
            if (ownCardId != null) {
                ownCard = getOwnCard(Integer.parseInt(ownCardId), currentUser);
            }
            Account targetAccount = getTargetAccount(targetAccountNumber);
            BankingCard targetCard = getTargetCard(targetCardNumber, expireDateString);

            if(ownAccount == null && ownCard == null){
                request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.OPERATION_ILLEGAL);
                request.getRequestDispatcher(PageUrls.TRANSFER_PAGE).forward(request,response);
                return;
            }

            if(targetAccount == null && targetCard == null){
                request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.NO_SUCH_RECEIVER);
                request.getRequestDispatcher(PageUrls.TRANSFER_PAGE).forward(request,response);
                return;
            }

            request.setAttribute(RequestAttributeNames.TARGET_CARD, targetCard);
            request.setAttribute(RequestAttributeNames.TARGET_ACC, targetAccount);
            request.setAttribute(RequestAttributeNames.OWN_ACC, ownAccount);
            request.setAttribute(RequestAttributeNames.OWN_CARD, ownCard);
            request.setAttribute(RequestAttributeNames.TRANSFER_VALUE, value);

            request.setAttribute(
                    RequestAttributeNames.BILL_ID,
                    request.getParameter(RequestParamName.BILL_ID));
            request.setAttribute(
                    RequestAttributeNames.PENALTY_ID,
                    request.getParameter(RequestParamName.PENALTY_ID));
            request.getRequestDispatcher(PageUrls.TRANSFER_CONFIRM_PAGE).forward(request, response);
        } catch (ServiceException e) {
            logger.error(e);
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.ERROR_PAGE);
            dispatcher.forward(request, response);
        }


    }

    private String checkParams(String valueStr, String targetAccountNumber,
                               String targetCardNumber, String ownAccountId,
                               String ownCardId){

        if(valueStr == null){
            return Message.OPERATION_INVALID_VALUE;
        }
        double value = Double.parseDouble(valueStr);
        if(value < 0){
            return  Message.OPERATION_INVALID_VALUE;
        }

        if(targetAccountNumber == null && targetCardNumber == null){
            return Message.OPERATION_NOT_ENOUGH_DATA;
        }

        if(ownAccountId == null && ownCardId == null){
            return Message.OPERATION_NOT_ENOUGH_DATA;
        }

        return null;
    }


    private BankingCard getTargetCard(String number, String expireDateString) throws ServiceException {
        if(number == null || expireDateString == null){
            return null;
        }

        List<BankingCard> fittingCards = cardService.findByNumber(number);
        if(fittingCards.isEmpty()){
            return null;
        }

        LocalDate expDate = null;
        try {
            Date tempDate = new SimpleDateFormat("yyyy-MM-dd").parse(expireDateString);
            expDate = new java.sql.Date(tempDate.getTime()).toLocalDate();
        } catch (ParseException e) {
            logger.error("Error parsing expiration date ",e);
        }
        if (expDate == null) {
            return null;
        }

        for(BankingCard card : fittingCards){
            LocalDate cardExpirationDate = new java.sql.Date(card.getExpirationDate().getTime()).toLocalDate();
            if(expDate.getMonth().equals(cardExpirationDate.getMonth())
                    && expDate.getYear() == cardExpirationDate.getYear()){
                return card;
            }
        }

        return null;
    }

    private Account getTargetAccount(String number) throws ServiceException {
        if(number == null){
            return null;
        }
        return accountService.findByNumber(number);
    }

    private Account getOwnAccount(Integer accountId, Integer currentUser) throws ServiceException {
        Account account = accountService.findById(accountId);

        if(!accountService.findUsers(account.getId()).contains(currentUser)){
            return null;
        }

        return account;
    }

    private BankingCard getOwnCard(Integer cardId, Integer currentUser) throws ServiceException {

        BankingCard card = cardService.findById(cardId);

        if(!card.getUserId().equals(currentUser)){
            return null;
        }

        return card;
    }

}
