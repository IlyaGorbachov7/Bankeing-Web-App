package by.epam.baranovsky.banking.controller.command.impl.transfer;

import by.epam.baranovsky.banking.constant.CommandName;
import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.constant.Message;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionParamName;
import by.epam.baranovsky.banking.entity.BankingCard;
import by.epam.baranovsky.banking.entity.Operation;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TransferCommand extends AbstractCommand {

    private static final String REDIRECT_TO_SUCCESS = String.format("%s?%s=%s",
            RequestParamName.CONTROLLER, RequestParamName.COMMAND_NAME,
            CommandName.GOTO_TRANSFER_SUCCESS_COMMAND);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            Integer currentUser = (Integer) request.getSession().getAttribute(SessionParamName.USER_ID);
            Operation operation = buildOperation(request);
            String errorMessage = checkOperation(operation, currentUser);

            if(errorMessage != null){
                request.setAttribute(RequestAttributeNames.ERROR_MSG, errorMessage);
                response.sendRedirect(getPreviousRequestAddress(request));
                return;
            }

            operationService.create(operation);
            response.sendRedirect(REDIRECT_TO_SUCCESS);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    private Operation buildOperation(HttpServletRequest request){
        Operation operation = new Operation();

        String account = request.getParameter(RequestParamName.ACCOUNT_ID);
        String targetAccount = request.getParameter(RequestParamName.TRANSFER_TARGET_ACC);
        String card = request.getParameter(RequestParamName.CARD_ID);
        String targetCard = request.getParameter(RequestParamName.TRANSFER_TARGET_CARD);
        String penaltyId = request.getParameter(RequestParamName.PENALTY_ID);
        String billId = request.getParameter(RequestParamName.BILL_ID);
        String value = request.getParameter(RequestParamName.TRANSFER_VALUE);

        operation.setValue(value != null ? Double.valueOf(value) : null);
        operation.setBillId(billId != null ? Integer.valueOf(billId) : null);
        operation.setPenaltyId(penaltyId != null ? Integer.valueOf(penaltyId) : null);
        operation.setTypeId(getTransferType(account, targetAccount, card, targetCard));
        operation.setAccountId(account != null ? Integer.valueOf(account) : null);
        operation.setTargetAccountId(targetAccount != null ? Integer.valueOf(targetAccount) : null);
        operation.setBankCardId(card != null ? Integer.valueOf(card) : null);
        operation.setTargetBankCardId(targetCard != null ? Integer.valueOf(targetCard) : null);

        return operation;
    }

    private Integer getTransferType(String acc, String targetAcc, String card, String targetCard){

        if(acc != null){
            if(targetAcc != null){
                return DBMetadata.OPERATION_TYPE_TRANSFER_A_A;
            }
            if(targetCard != null){
                return DBMetadata.OPERATION_TYPE_TRANSFER_A_C;
            }
        }
        if(card != null){
            if(targetAcc != null){
                return DBMetadata.OPERATION_TYPE_TRANSFER_C_A;
            }
            if(targetCard != null){
                return DBMetadata.OPERATION_TYPE_TRANSFER_C_C;
            }
        }
        return null;
    }

    private String checkOperation(Operation operation, Integer currentUser) throws ServiceException {

        if(operation.getTypeId() == null){
            return Message.OPERATION_ILLEGAL;
        }

        if(operation.getValue()<0){
            return Message.OPERATION_INVALID_VALUE;
        }

        if(operation.getBillId() != null && operation.getPenaltyId() != null){
            return Message.PENALTY_BILL_INTERSECTION;
        }

        if((operation.getAccountId()== null && operation.getBankCardId() == null)
                || (operation.getTargetAccountId() == null && operation.getTargetBankCardId() == null)){
            return Message.OPERATION_NOT_ENOUGH_DATA;
        }

        if(operation.getTypeId().equals(DBMetadata.OPERATION_TYPE_TRANSFER_A_A)){
            if(!accountService.findUsers(accountService.findById(operation.getAccountId()).getId()).contains(currentUser)){
                return Message.NOT_YOUR_ACCOUNT;
            }
            if(operation.getAccountId().equals(operation.getTargetAccountId())){
                return Message.OPERATION_TRANSFER_TO_SELF;
            }
        } else if(operation.getTypeId().equals(DBMetadata.OPERATION_TYPE_TRANSFER_A_C)){
            if(!accountService.findUsers(accountService.findById(operation.getAccountId()).getId()).contains(currentUser)){
                return Message.NOT_YOUR_ACCOUNT;
            }
            BankingCard target = cardService.findById(operation.getTargetBankCardId());
            if(operation.getAccountId().equals(target.getAccountId())){
                return Message.OPERATION_TRANSFER_TO_SELF;
            }
        } else if(operation.getTypeId().equals(DBMetadata.OPERATION_TYPE_TRANSFER_C_A)){
            BankingCard card = cardService.findById(operation.getBankCardId());
            if(!card.getUserId().equals(currentUser)){
                return Message.CARD_NOT_YOURS;
            }
            if(card.getAccountId().equals(operation.getTargetAccountId())){
                return Message.OPERATION_TRANSFER_TO_SELF;
            }
        }else if(operation.getTypeId().equals(DBMetadata.OPERATION_TYPE_TRANSFER_C_C)){
            BankingCard card = cardService.findById(operation.getBankCardId());
            if(!card.getUserId().equals(currentUser)){
                return Message.CARD_NOT_YOURS;
            }
            if(card.getId().equals(operation.getTargetBankCardId())){
                return Message.OPERATION_TRANSFER_TO_SELF;
            }
        }

        if(operation.getTargetAccountId() != null && operation.getBillId() != null){
            if(!billService.findById(operation.getBillId()).getPaymentAccountId().equals(operation.getTargetAccountId())){
                return Message.OPERATION_ILLEGAL;
            }
        }

        if(operation.getTargetAccountId() != null && operation.getPenaltyId() != null){
            if(!penaltyService.findById(operation.getPenaltyId()).getPaymentAccountId().equals(operation.getTargetAccountId())){
                return Message.OPERATION_ILLEGAL;
            }
        }

        return null;
    }
}
