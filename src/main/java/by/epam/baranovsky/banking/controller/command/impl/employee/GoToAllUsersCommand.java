package by.epam.baranovsky.banking.controller.command.impl.employee;

import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionAttributeName;
import by.epam.baranovsky.banking.entity.User;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Implementation of Command
 * used for forwarding user to the page that lists all users registered in the system.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class GoToAllUsersCommand extends AbstractCommand {

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            String searchEmail = request.getParameter(RequestParamName.EMAIL);
            Integer currentId = (Integer) request.getSession().getAttribute(SessionAttributeName.USER_ID);

            List<User> users = getAllUsersAvailableToBrowse(currentId);
            if(searchEmail != null && !searchEmail.isBlank()){
                users.removeIf(user -> !user.getEmail().contains(searchEmail));
            }
            request.setAttribute(RequestAttributeNames.ALL_USERS, users);
            request.getRequestDispatcher(PageUrls.ALL_USERS_PAGE).forward(request,response);
        }catch (ServiceException e) {
            logger.error(e);
            request.getRequestDispatcher(PageUrls.ERROR_PAGE).forward(request, response);
        }
    }

    /**
     * Retrieves all users except the one browsing.
     * @return List of all users except the one browsing.
     * @throws ServiceException
     */
    private List<User> getAllUsersAvailableToBrowse(Integer currentUserID)
            throws ServiceException {

        List<User> result = userService.getAll();
        result.removeIf(user -> user.getId().equals(currentUserID));
        return result;
    }

}
