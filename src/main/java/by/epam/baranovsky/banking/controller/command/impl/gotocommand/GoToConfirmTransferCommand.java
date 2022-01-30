package by.epam.baranovsky.banking.controller.command.impl.gotocommand;

import by.epam.baranovsky.banking.constant.*;
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

    private static final String BACK = String.format("%s?%s=%s",
            RequestParamName.CONTROLLER, RequestParamName.COMMAND_NAME,
            CommandName.GOTO_TRANSFER_COMMAND);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer currentUser = (Integer) request.getSession().getAttribute(SessionParamName.USER_ID);
        String targetAccountNumber = request.getParameter(RequestParamName.TRANSFER_TARGET_ACC_NUMBER);
        String targetCardNumber = request.getParameter(RequestParamName.TRANSFER_TARGET_CARD_NUMBER);
        String expireDateString = request.getParameter(RequestParamName.TRANSFER_TARGET_CARD_EXPIRATION);
        String ownAccountId=request.getParameter(RequestParamName.ACCOUNT_ID);
        String ownCardId=request.getParameter(RequestParamName.CARD_ID);
        String valueStr = request.getParameter(RequestParamName.TRANSFER_VALUE);
        String billId = request.getParameter(RequestParamName.BILL_ID);
        String penaltyId = request.getParameter(RequestParamName.PENALTY_ID);

        String back = BACK
                + ((billId != null && !billId.isEmpty())
                ? "&"+RequestParamName.BILL_ID+"="+billId : "")
                + ((penaltyId != null && !penaltyId.isEmpty())
                ? "&"+RequestParamName.PENALTY_ID+"="+penaltyId : "");

        String errorMsgForParams = checkParams(valueStr, targetAccountNumber, targetCardNumber, ownAccountId, ownCardId);
        if(errorMsgForParams!=null){
            request.setAttribute(RequestAttributeNames.ERROR_MSG, errorMsgForParams);
            request.getRequestDispatcher(BACK).forward(request,response);
            return;
        }

        Double value = Double.parseDouble(valueStr);

        try{
            Account ownAccount = null;
            if(!(ownAccountId == null || ownAccountId.isEmpty())){
                ownAccount = getOwnAccount(Integer.parseInt(ownAccountId), currentUser);
            }
            BankingCard ownCard = null;
            if (!(ownCardId == null || ownCardId.isEmpty())) {
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
                request.getRequestDispatcher(BACK).forward(request,response);
                return;
            }

            request.setAttribute(RequestAttributeNames.TARGET_CARD, targetCard);
            request.setAttribute(RequestAttributeNames.TARGET_ACC, targetAccount);
            request.setAttribute(RequestAttributeNames.OWN_ACC, ownAccount);
            request.setAttribute(RequestAttributeNames.OWN_CARD, ownCard);
            request.setAttribute(RequestAttributeNames.TRANSFER_VALUE, value);
            double commission = value * Double.parseDouble(ConfigManager.getInstance().getValue(ConfigParams.TRANSFER_COMMISSION_RATE));
            Double commissionToSet=null;

            if(billId != null && !billId.isBlank()){
                if(billService.findById(Integer.valueOf(billId)).getLoanId() == null
                        || billService.findById(Integer.valueOf(billId)).getLoanId() == 0){
                    commissionToSet = commission;
                }
            } else if(billId == null || billId.isBlank()){
                commissionToSet = commission;
            }

            if (penaltyId != null && !penaltyId.isBlank()) {
                commissionToSet = null;
            }

            request.setAttribute(RequestAttributeNames.COMMISSION, commissionToSet);

            request.setAttribute(
                    RequestAttributeNames.BILL_ID,
                    billId);
            request.setAttribute(
                    RequestAttributeNames.PENALTY_ID,
                    penaltyId);

            request.setAttribute(RequestAttributeNames.PREV_PAGE, back);
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

        if(valueStr== null || valueStr.isEmpty()){
            return Message.OPERATION_INVALID_VALUE;
        }
        double value = Double.parseDouble(valueStr);
        if(value < 0){
            return  Message.OPERATION_INVALID_VALUE;
        }

        if((targetAccountNumber == null || targetAccountNumber.isEmpty())
                && (targetCardNumber == null || targetCardNumber.isEmpty())){
            return Message.OPERATION_NOT_ENOUGH_DATA;
        }

        if((ownAccountId == null || ownAccountId.isEmpty())
                && (ownCardId == null || ownCardId.isEmpty())){
            return Message.OPERATION_NOT_ENOUGH_DATA;
        }

        return null;
    }


    private BankingCard getTargetCard(String number, String expireDateString) throws ServiceException {
        if(number == null || number.isEmpty()
                || expireDateString== null || expireDateString.isEmpty()){
            return null;
        }

        List<BankingCard> fittingCards = cardService.findByNumber(number);
        if(fittingCards.isEmpty()){
            return null;
        }

        LocalDate expDate = null;
        try {
            Date tempDate = new SimpleDateFormat("yyyy-MM").parse(expireDateString);
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
        if(number == null || number.isEmpty()){
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
        if(!card.getCardTypeId().equals(DBMetadata.CARD_TYPE_CREDIT)){
            card.setBalance(accountService.findById(card.getAccountId()).getBalance());
        }

        return card;
    }

}
