package by.epam.baranovsky.banking.controller.command.impl.gotocommand;

import by.epam.baranovsky.banking.constant.CommandName;
import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.constant.Message;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionAttributeName;
import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.entity.BankingCard;
import by.epam.baranovsky.banking.entity.User;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of Command
 * used to forward user to account info page.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class GoToAccountInfoCommand extends AbstractCommand {

    private static final String REDIRECT_TO_ACCS=String.format(
            "%s?%s=%s",
            RequestParamName.CONTROLLER,
            RequestParamName.COMMAND_NAME,
            CommandName.GOTO_ACCOUNTS);

    /**
     * {@inheritDoc}
     * <p>
     *     Redirects to all accounts page if user can not view this account,
     *     or forwards to the error page if no such account was found.
     * </p>
     * <p>
     *     Regular user can view full info on account and can manage the account,
     *     admins and employees can only view information on account
     *     and manipulate its status.
     * </p>
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int accountId = Integer.parseInt(request.getParameter(RequestParamName.ACCOUNT_ID));

        if(accountId > 0){
            try{
                Account account = accountService.findById(accountId);
                List<Integer> userIdList = accountService.findUsers(accountId);

                if(!checkIfCanView(request, userIdList)){
                    request.getRequestDispatcher(REDIRECT_TO_ACCS).forward(request, response);
                    return;
                }

                request.setAttribute(RequestAttributeNames.ACCOUNT_DATA, account);
                request.setAttribute(RequestAttributeNames.ACCOUNT_USERS_INFO, getUsersInfo(userIdList));
                request.setAttribute(RequestAttributeNames.ACCOUNT_CARDS_INFO, getCardsForThisAccount(accountId));
                request.getRequestDispatcher(PageUrls.ACCOUNT_INFO_PAGE).forward(request, response);
            } catch (ServiceException e) {
                logger.error(e);
                request.getRequestDispatcher(PageUrls.ERROR_PAGE).forward(request, response);
            }
        } else{
            request.getRequestDispatcher(PageUrls.ERROR_PAGE).forward(request, response);
        }

    }

    /**
     * Checks if user can view full or partial info on the account.
     * <p>
     *     If current user is an employee or admin viewing
     *     another user's account, view-only attribute is set in request.
     * </p>
     * @param request Servlet request.
     * @param userList List of users of given account.
     * @return {@code true} if user can view info on acount, {@code false} otherwise.
     */
    private boolean checkIfCanView(HttpServletRequest request, List<Integer> userList){
        Integer currentUserId = (Integer) request.getSession().getAttribute(SessionAttributeName.USER_ID);
        Integer currentUserRole = (Integer) request.getSession().getAttribute(SessionAttributeName.USER_ROLE_ID);

        if(!userList.contains(currentUserId)){
            if(!currentUserRole.equals(DBMetadata.USER_ROLE_REGULAR)){
                request.setAttribute(RequestAttributeNames.VIEW_ONLY, true);
            } else{
                request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.NOT_YOUR_ACCOUNT);
                return false;
            }
        }
        return true;
    }

    /**
     * Retrieves full names of users with given IDs.
     * @param userIds List of users IDs.
     * @return List of strings - full names of users.
     * @throws ServiceException
     */
    private List<String> getUsersInfo(List<Integer> userIds) throws ServiceException {
        List<String> userInfo = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for(Integer id: userIds){
            builder.setLength(0);
            User user = userService.getById(id);
            builder.append(user.getFirstName())
                    .append(" ")
                    .append(user.getLastName());

            if(user.getPatronymic() != null){
                builder.append(" ").append(user.getPatronymic());
            }
            userInfo.add(builder.toString());
        }
        return userInfo;
    }

    /**
     * Retrieves all cards linked to account with given ID.
     * @param accountId ID of account in question.
     * @return List of banking cards linked to account with given ID.
     * @throws ServiceException
     */
    private List<BankingCard> getCardsForThisAccount(Integer accountId) throws ServiceException{
        List<BankingCard> cards = cardService.findByAccount(accountId);

        for(BankingCard card : cards){
            card.setNumber(maskCardNumber(card.getNumber()));
        }

        return cards;
    }
}
