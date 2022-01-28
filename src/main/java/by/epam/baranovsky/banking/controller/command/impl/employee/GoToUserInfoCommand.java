package by.epam.baranovsky.banking.controller.command.impl.employee;

import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.dao.rowmapper.impl.CardRowMapper;
import by.epam.baranovsky.banking.entity.*;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.entity.criteria.SingularValue;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoToUserInfoCommand extends AbstractCommand {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer userId = Integer.valueOf(request.getParameter(RequestParamName.ID_CHECKED_USER));

        try{
            User user = userService.getById(userId);

            List<Account> userAccounts = getUserAccounts(userId);
            Map<BankingCard, String> cards = getUserCards(userId);
            request.setAttribute(RequestAttributeNames.USER_DATA, user);
            request.setAttribute(RequestAttributeNames.USER_ACCOUNTS, userAccounts);
            request.setAttribute(RequestAttributeNames.USER_CARDS, cards);
            request.setAttribute(RequestAttributeNames.OPERATIONS_DATA,
                    getUserOperationDTOs(userId, userAccounts, cards));
            request.setAttribute(RequestAttributeNames.USER_LOANS, getUserLoans(userId));
            request.setAttribute(RequestAttributeNames.USER_BILLS, getUserBills(userId));
            request.setAttribute(RequestAttributeNames.USER_PENALTIES, getUserPenalties(userId));

            request.getRequestDispatcher(PageUrls.USER_INFO_PAGE).forward(request,response);
        } catch (ServiceException e) {
            logger.error(e);
            request.getRequestDispatcher(PageUrls.ERROR_PAGE).forward(request, response);

        }

    }

    private List<Account> getUserAccounts(Integer userId) throws ServiceException {
        return accountService.findByUserId(userId);
    }

    private List<Penalty> getUserPenalties(Integer userId) throws ServiceException{
        Criteria<EntityParameters.PenaltyParams> criteria = new Criteria<>();
        criteria.add(EntityParameters.PenaltyParams.USER, new SingularValue<>(userId));
        return penaltyService.findByCriteria(criteria);
    }

    private List<Loan> getUserLoans(Integer userId) throws ServiceException {
        Criteria<EntityParameters.LoanParams> criteria = new Criteria<>();
        criteria.add(EntityParameters.LoanParams.USER, new SingularValue<>(userId));
        return loanService.findByCriteria(criteria);
    }

    private List<Bill> getUserBills(Integer userId) throws ServiceException {
        Criteria<EntityParameters.BillParam> criteria = new Criteria<>();
        criteria.add(EntityParameters.BillParam.USER, new SingularValue<>(userId));
        return billService.findByCriteria(criteria);
    }


    private Map<BankingCard, String> getUserCards(Integer userId) throws ServiceException{
        List<BankingCard> cards = cardService.findByUser(userId);

        Map<BankingCard, String> finalMap = new HashMap<>();
        for(BankingCard card : cards){
            card.setNumber(maskNumber(card.getNumber()));
            if(card.getAccountId() != null && card.getAccountId() != 0){
                finalMap.put(card, accountService.findById(card.getAccountId()).getAccountNumber());
            } else{
                finalMap.put(card, null);
            }
        }
        return finalMap;
    }

    private List<OperationTransferObject> getUserOperationDTOs(Integer id, List<Account> accounts, Map<BankingCard, String> cards) throws ServiceException{

        List<Integer> userAccs = getUserAccountIds(accounts);
        List<Integer> userCards = getUserCardsIds(cards);
        List<Operation> operations = getOperations(accounts, cards);

        List<OperationTransferObject> operationPackages = new ArrayList<>();

        for(Operation operation : operations){
            OperationTransferObject oto = new OperationTransferObject();

            oto.setIsAccrual(!userCards.contains(operation.getBankCardId()) && !userAccs.contains(operation.getAccountId()));
            oto.setTypeId(operation.getTypeId());
            oto.setValue(operation.getValue());
            if(operation.getAccountId() != null && operation.getAccountId() != 0){
                oto.setAccountNumber(accountService.findById(
                        operation.getAccountId()).getAccountNumber());
            }
            if(operation.getTargetAccountId() != null && operation.getTargetAccountId() != 0){
                oto.setTargetAccountNumber(accountService.findById(
                        operation.getTargetAccountId()).getAccountNumber());
            }
            if(operation.getBankCardId() != null && operation.getBankCardId() != 0){
                oto.setCardNumber(maskNumber(cardService.findById(
                        operation.getBankCardId()).getNumber()));
            }
            if(operation.getTargetBankCardId() != null && operation.getTargetBankCardId() != 0){
                oto.setTargetCardNumber(maskNumber(cardService.findById(
                        operation.getTargetBankCardId()).getNumber()));
            }
            oto.setBill(operation.getBillId());
            oto.setPenalty(operation.getPenaltyId());
            oto.setDate(operation.getOperationDate());
            operationPackages.add(oto);
        }
        operationPackages.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));

        return operationPackages;
    }

    private List<Operation> getOperations(List<Account> accounts,
                                          Map<BankingCard, String> cards) throws ServiceException {
        Criteria<EntityParameters.OperationParam> criteria = new Criteria<>(Criteria.SQL_OR);

        for(BankingCard card : cards.keySet()){
            criteria.add(
                    EntityParameters.OperationParam.CARD,
                    new SingularValue<>(card.getId()));
            criteria.add(
                    EntityParameters.OperationParam.TARGET_CARD,
                    new SingularValue<>(card.getId()));
        }

        for(Account account : accounts){
            criteria.add(
                    EntityParameters.OperationParam.ACCOUNT,
                    new SingularValue<>(account.getId()));
            criteria.add(
                    EntityParameters.OperationParam.TARGET_ACCOUNT,
                    new SingularValue<>(account.getId()));
        }

        return operationService.findByCriteria(criteria);

    }

    private List<Integer> getUserAccountIds(List<Account> accounts) throws ServiceException {

        List<Integer> userAccsIds = new ArrayList<>();
        for(Account account : accounts){
            userAccsIds.add(account.getId());
        }

        return userAccsIds;
    }

    private List<Integer> getUserCardsIds(Map<BankingCard, String> cards) throws ServiceException {

        List<Integer> userCardIds = new ArrayList<>();
        for(BankingCard card : cards.keySet()){
            userCardIds.add(card.getId());
        }

        return userCardIds;
    }

}
