package by.epam.baranovsky.banking.controller.command.impl.card;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.constant.Message;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionAttributeName;
import by.epam.baranovsky.banking.entity.BankingCard;
import by.epam.baranovsky.banking.entity.Operation;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Implementation of Command
 * used for locking bank card.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class LockCardCommand extends AbstractCommand {

    /**
     * {@inheritDoc}
     * <p>
     *     Forwards to previous request in case of failure,
     *     redirects to previous request otherwise.
     * </p>
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            BankingCard card = cardService.findById(Integer.valueOf(request.getParameter(RequestParamName.CARD_ID)));

            if(card.getStatusId().equals(DBMetadata.CARD_STATUS_EXPIRED)){
                request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.CANT_ALTER_EXPIRED_CARD);
                RequestDispatcher dispatcher = request.getRequestDispatcher(getPreviousRequestAddress(request));
                dispatcher.forward(request, response);
                return;
            }

            if(!isUserValid(request, card)){
                request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.CARD_NOT_YOURS);
                RequestDispatcher dispatcher = request.getRequestDispatcher(getPreviousRequestAddress(request));
                dispatcher.forward(request, response);
                return;
            }

            Operation operation = new Operation();
            operation.setBankCardId(card.getId());
            operation.setTypeId(DBMetadata.OPERATION_TYPE_CARD_LOCK);

            operationService.create(operation);
            response.sendRedirect(getPreviousRequestAddress(request));
        } catch (ServiceException e) {
            logger.error(e);
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.ERROR_PAGE);
            dispatcher.forward(request, response);
        }
    }

    /**
     * Checks if current user can lock this card.
     * @param request Servlet request.
     * @param card Card in question.
     * @return {@code true} if current user is admin, employee or the owner of the card.
     */
    private boolean isUserValid(HttpServletRequest request, BankingCard card){
        Integer userId = (Integer) request.getSession().getAttribute(SessionAttributeName.USER_ID);
        Integer roleId = (Integer) request.getSession().getAttribute(SessionAttributeName.USER_ROLE_ID);

        if(DBMetadata.USER_ROLE_ADMIN.equals(roleId) || DBMetadata.USER_ROLE_EMPLOYEE.equals(roleId)){
            return true;
        }

        return card.getUserId().equals(userId);
    }
}
