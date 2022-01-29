package by.epam.baranovsky.banking.controller.command.impl.transfer;

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
import by.epam.baranovsky.banking.entity.Loan;
import by.epam.baranovsky.banking.entity.Operation;
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
                request.getRequestDispatcher(PageUrls.TRANSFER_FAILURE_PAGE).forward(request,response);
                return;
            }

            operationService.create(operation);
            response.sendRedirect(REDIRECT_TO_SUCCESS);

        } catch (ServiceException e) {
            logger.error(e);
            request.getRequestDispatcher(PageUrls.ERROR_PAGE).forward(request, response);
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

        operation.setValue(value != null && !value.isEmpty() ? Double.valueOf(value) : null);
        operation.setBillId(billId != null && !billId.isEmpty() ? Integer.valueOf(billId) : null);
        operation.setPenaltyId(penaltyId != null && !penaltyId.isEmpty() ? Integer.valueOf(penaltyId) : null);
        operation.setTypeId(getTransferType(account, targetAccount, card, targetCard));
        operation.setAccountId(account != null && !account.isEmpty() ? Integer.valueOf(account) : null);
        operation.setTargetAccountId(targetAccount != null && !targetAccount.isEmpty() ? Integer.valueOf(targetAccount) : null);
        operation.setBankCardId(card != null && !card.isEmpty() ? Integer.valueOf(card) : null);
        operation.setTargetBankCardId(targetCard != null && !targetCard.isEmpty() ? Integer.valueOf(targetCard) : null);

        return operation;
    }

    private Integer getTransferType(String acc, String targetAcc, String card, String targetCard){

        if(acc != null && !acc.isEmpty()){
            if(targetAcc != null && !targetAcc.isEmpty()){
                return DBMetadata.OPERATION_TYPE_TRANSFER_A_A;
            }
            if(targetCard != null && !targetCard.isEmpty()){
                return DBMetadata.OPERATION_TYPE_TRANSFER_A_C;
            }
        }
        if(card != null && !card.isEmpty()){
            if(targetAcc != null && !targetAcc.isEmpty()){
                return DBMetadata.OPERATION_TYPE_TRANSFER_C_A;
            }
            if(targetCard != null && !targetCard.isEmpty()){
                return DBMetadata.OPERATION_TYPE_TRANSFER_C_C;
            }
        }
        return null;
    }

    private String checkOperation(Operation operation, Integer currentUser) throws ServiceException {

        if(checkNullParams(operation) != null){
            return checkNullParams(operation);
        }

        String lockedOrSuspendedError = checkIfAccLockedOrSuspended(operation);
        if( lockedOrSuspendedError != null){
            return lockedOrSuspendedError;
        }

        String targetLockedError = checkIfTargetIsLocked(operation);
        if( targetLockedError != null){
            return targetLockedError;
        }

        if(operation.getTypeId().equals(DBMetadata.OPERATION_TYPE_TRANSFER_A_A)){
            if(!accountService.findUsers(accountService.findById(operation.getAccountId()).getId()).contains(currentUser)){
                return Message.NOT_YOUR_ACCOUNT;
            }
            if(operation.getAccountId().equals(operation.getTargetAccountId())){
                return Message.OPERATION_TRANSFER_TO_SELF;
            }
            if(!checkAgainstAccountBalance(accountService.findById(operation.getAccountId()), operation.getValue())){
                return Message.NO_MONEY;
            }
        } else if(operation.getTypeId().equals(DBMetadata.OPERATION_TYPE_TRANSFER_A_C)){
            if(!accountService.findUsers(accountService.findById(operation.getAccountId()).getId()).contains(currentUser)){
                return Message.NOT_YOUR_ACCOUNT;
            }
            BankingCard target = cardService.findById(operation.getTargetBankCardId());
            if(operation.getAccountId().equals(target.getAccountId())){
                return Message.OPERATION_TRANSFER_TO_SELF;
            }
            if(!checkAgainstAccountBalance(accountService.findById(operation.getAccountId()), operation.getValue())){
                return Message.NO_MONEY;
            }
        } else if(operation.getTypeId().equals(DBMetadata.OPERATION_TYPE_TRANSFER_C_A)){
            BankingCard card = cardService.findById(operation.getBankCardId());
            if(!card.getUserId().equals(currentUser)){
                return Message.CARD_NOT_YOURS;
            }
            if(card.getAccountId().equals(operation.getTargetAccountId())){
                return Message.OPERATION_TRANSFER_TO_SELF;
            }
            if(!checkAgainstCardBalance(card, operation.getValue())){
                return Message.NO_MONEY;
            }
        }else if(operation.getTypeId().equals(DBMetadata.OPERATION_TYPE_TRANSFER_C_C)){
            BankingCard card = cardService.findById(operation.getBankCardId());
            BankingCard target = cardService.findById(operation.getTargetBankCardId());
            if(!card.getUserId().equals(currentUser)){
                return Message.CARD_NOT_YOURS;
            }
            if(card.getId().equals(target.getId())
                    || card.getAccountId().equals(target.getAccountId())){
                return Message.OPERATION_TRANSFER_TO_SELF;
            }
            if(!checkAgainstCardBalance(card, operation.getValue())){
                return Message.NO_MONEY;
            }
        }

        return null;
    }

    private String checkNullParams(Operation operation) throws ServiceException {
        if(operation.getTypeId() == null ){
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

    private String checkIfAccLockedOrSuspended(Operation operation) throws ServiceException {

        if(operation.getBillId() != null || operation.getPenaltyId() != null){
            return null;
        }
        int accId = 0;

        if(operation.getAccountId() != null){
            accId = operation.getAccountId();
        }

        if(operation.getBankCardId() != null){
            BankingCard card = cardService.findById(operation.getBankCardId());
            if(card.getAccountId() == null || card.getAccountId() == 0){
                return Message.CREDIT_CARD;
            }
            accId = card.getAccountId();
        }

        Account acc = accountService.findById(accId);
        if(acc != null && acc.getStatusId().equals(DBMetadata.ACCOUNT_STATUS_SUSPENDED)) {
            return Message.ACC_SUSPENDED;
        }
        if(acc != null && acc.getStatusId().equals(DBMetadata.ACCOUNT_STATUS_BLOCKED)) {
            return Message.ACC_LOCKED;
        }

        return null;
    }

    private String checkIfTargetIsLocked(Operation operation) throws ServiceException{
        int targetAcc = 0;

        if(operation.getTargetAccountId() != null){
            targetAcc = operation.getTargetAccountId();
        }
        if(operation.getTargetBankCardId() != null){
            BankingCard card = cardService.findById(operation.getTargetBankCardId());
            if(card.getAccountId() == null || card.getAccountId() == 0){
                return Message.CREDIT_CARD;
            }
            if(card.getStatusId().equals(DBMetadata.CARD_STATUS_LOCKED)
                    || card.getStatusId().equals(DBMetadata.CARD_STATUS_EXPIRED)){
                return Message.CARD_LOCKED;
            }
            targetAcc = card.getAccountId();
        }

        Account acc = accountService.findById(targetAcc);

        if(acc != null && acc.getStatusId().equals(DBMetadata.ACCOUNT_STATUS_BLOCKED)) {
            return Message.ACC_LOCKED;
        }


        return null;
    }


    private boolean checkAgainstAccountBalance(Account account, Double value){
        return account.getBalance()>value;
    }

    private boolean checkAgainstCardBalance(BankingCard card, Double value) throws ServiceException {
        if(card.getCardTypeId().equals(DBMetadata.CARD_TYPE_DEBIT)){
            if(accountService.findById(card.getAccountId()).getBalance()<value){
                return false;
            }
        }
        if(card.getCardTypeId().equals(DBMetadata.CARD_TYPE_CREDIT)){
            if(card.getBalance()<value){
                return false;
            }
        }
        if(card.getCardTypeId().equals(DBMetadata.CARD_TYPE_OVERDRAFT)){
            if(accountService.findById(card.getAccountId()).getBalance() + getUnspentOverdraft(card) < value){
                return false;
            }
        }

        return true;
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
}
