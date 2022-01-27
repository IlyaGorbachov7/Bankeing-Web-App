package by.epam.baranovsky.banking.controller.command.impl.gotocommand;

import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionParamName;
import by.epam.baranovsky.banking.entity.*;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.entity.criteria.SingularValue;
import by.epam.baranovsky.banking.service.AccountService;
import by.epam.baranovsky.banking.service.BankCardService;
import by.epam.baranovsky.banking.service.OperationService;
import by.epam.baranovsky.banking.service.exception.ServiceException;
import by.epam.baranovsky.banking.service.impl.AccountServiceImpl;
import by.epam.baranovsky.banking.service.impl.BankCardServiceImpl;
import by.epam.baranovsky.banking.service.impl.OperationServiceImpl;
import by.epam.baranovsky.banking.service.impl.UserServiceImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

public class GoToHomePageCommand extends AbstractCommand {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute(SessionParamName.USER_ID);

        if(userId != null){
            try {
                User user = UserServiceImpl.getInstance().getById(userId);
                request.setAttribute(RequestAttributeNames.USER_DATA, user);
                request.setAttribute(
                        RequestAttributeNames.OPERATIONS_DATA,
                        getUserOperationsMapWithOutput(userId));

            } catch (ServiceException e) {
                logger.error(e);
                RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.ERROR_PAGE);
                dispatcher.forward(request, response);
            }
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.HOME_PAGE);
        dispatcher.forward(request, response);
    }

    private Map<Operation, String> getUserOperationsMapWithOutput(Integer id) throws ServiceException{

        List<Operation> operations = getOperations(getUserAccountIds(id), getUserCardsIds(id));

        Map<Operation, String> finalMap = new TreeMap<>(Comparator.comparing(Operation::getOperationDate).reversed());

        for(Operation operation : operations){
            finalMap.put(operation, getOutputParams(operation));
        }
        
       return finalMap;
    }

    private List<Operation> getOperations(List<Integer> accountIds,
                                          List<Integer> cardIds) throws ServiceException {
        Criteria<EntityParameters.OperationParam> criteria = new Criteria<>(Criteria.SQL_OR);

        for(Integer id : cardIds){
            criteria.add(
                    EntityParameters.OperationParam.CARD,
                    new SingularValue<>(id));
            criteria.add(
                    EntityParameters.OperationParam.TARGET_CARD,
                    new SingularValue<>(id));
        }

        for(Integer id : accountIds){
            criteria.add(
                    EntityParameters.OperationParam.ACCOUNT,
                    new SingularValue<>(id));
            criteria.add(
                    EntityParameters.OperationParam.TARGET_ACCOUNT,
                    new SingularValue<>(id));
        }

        return operationService.findByCriteria(criteria);

    }

    private List<Integer> getUserAccountIds(Integer id) throws ServiceException {
        List<Account> userAccounts =  accountService.findByUserId(id);

        List<Integer> userAccsIds = new ArrayList<>();
        for(Account account : userAccounts){
            userAccsIds.add(account.getId());
        }

        return userAccsIds;
    }

    private List<Integer> getUserCardsIds(Integer id) throws ServiceException {
        List<BankingCard> userCards =  cardService.findByUser(id);

        List<Integer> userCardIds = new ArrayList<>();
        for(BankingCard card : userCards){
            userCardIds.add(card.getId());
        }

        return userCardIds;
    }

    private String getOutputParams(Operation operation) throws ServiceException {
        StringBuilder builder = new StringBuilder();

        builder.append(operation.getTypeName()).append(": ");

        if(operation.getAccountId()!=0){
            builder.append("Account №")
                    .append(accountService.findById(operation.getAccountId()).getAccountNumber())
                    .append(" ");
        }

        if(operation.getBankCardId()!=0){
            builder.append("Card №")
                    .append(maskNumber(cardService
                            .findById(operation.getBankCardId()).getNumber()))
                    .append(" ");
        }

        if(operation.getTargetAccountId()!=0){
            builder.append(" to Account №")
                    .append(accountService.findById(operation.getTargetAccountId()).getAccountNumber())
                    .append(" ");
        }

        if(operation.getTargetBankCardId()!=0){
            builder.append(" to Card №")
                    .append(maskNumber(cardService
                            .findById(operation.getTargetBankCardId()).getNumber()))
                    .append(" ");
        }

        return builder.toString();
    }
}
