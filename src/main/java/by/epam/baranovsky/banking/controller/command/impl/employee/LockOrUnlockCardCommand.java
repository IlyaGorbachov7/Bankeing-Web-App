package by.epam.baranovsky.banking.controller.command.impl.employee;

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

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LockOrUnlockCardCommand extends AbstractCommand {

    private static final String BACK_TO_INFO = String.format("%s?%s=%s&%s=",
            RequestParamName.CONTROLLER, RequestParamName.COMMAND_NAME,
            CommandName.GOTO_CARD_INFO_COMMAND, RequestParamName.CARD_ID);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            Integer newStatus = Integer.valueOf(request.getParameter(RequestParamName.CARD_NEW_STATUS));
            BankingCard card = cardService.findById(Integer.valueOf(request.getParameter(RequestParamName.CARD_ID)));
            Integer userRole = (Integer) request.getSession().getAttribute(SessionParamName.USER_ROLE_ID);

            if(userRole.equals(DBMetadata.USER_ROLE_EMPLOYEE) && newStatus.equals(DBMetadata.CARD_STATUS_UNLOCKED)){
                request.getRequestDispatcher(PageUrls.ERROR_PAGE).forward(request,response);
                return;
            }
            if(card.getStatusId().equals(DBMetadata.CARD_STATUS_EXPIRED) || newStatus.equals(DBMetadata.CARD_STATUS_EXPIRED)){
                request.getRequestDispatcher(PageUrls.ERROR_PAGE).forward(request,response);
                return;
            }

            Operation operation = buildOperation(card.getId(), newStatus);
            operationService.create(operation);
            response.sendRedirect(BACK_TO_INFO+card.getId());
        } catch (ServiceException e) {
            logger.error(e);
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.ERROR_PAGE);
            dispatcher.forward(request, response);
        }
    }

    private Operation buildOperation(Integer cardId, Integer newStatus){
        Operation operation = new Operation();
        operation.setBankCardId(cardId);

        if(newStatus.equals(DBMetadata.CARD_STATUS_LOCKED)){
            operation.setTypeId(DBMetadata.OPERATION_TYPE_CARD_LOCK);
        }
        if(newStatus.equals(DBMetadata.CARD_STATUS_UNLOCKED)){
            operation.setTypeId(DBMetadata.OPERATION_TYPE_CARD_UNLOCK);
        }

        return operation;
    }

}
