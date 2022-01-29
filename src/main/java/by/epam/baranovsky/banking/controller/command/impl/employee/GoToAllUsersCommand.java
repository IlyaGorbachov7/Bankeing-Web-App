package by.epam.baranovsky.banking.controller.command.impl.employee;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionParamName;
import by.epam.baranovsky.banking.entity.User;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.entity.criteria.SingularValue;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class GoToAllUsersCommand extends AbstractCommand {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            String searchEmail = request.getParameter(RequestParamName.EMAIL);

            Integer currentRole = (Integer) request.getSession().getAttribute(SessionParamName.USER_ROLE_ID);
            Integer currentId = (Integer) request.getSession().getAttribute(SessionParamName.USER_ID);

            List<User> users = getAllUsersAvailableToBrowse(currentRole);
            users.removeIf(user -> user.getId().equals(currentId));
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

    private List<User> getAllUsersAvailableToBrowse(Integer curentUserRole) throws ServiceException {
        Criteria<EntityParameters.UserParams> criteria = new Criteria<>(Criteria.SQL_OR);
        criteria.add(EntityParameters.UserParams.ROLE_ID, new SingularValue<>(DBMetadata.USER_ROLE_REGULAR));
        criteria.add(EntityParameters.UserParams.ROLE_ID, new SingularValue<>(DBMetadata.USER_ROLE_BANNED));

        if(curentUserRole.equals(DBMetadata.USER_ROLE_ADMIN)){
            criteria.add(EntityParameters.UserParams.ROLE_ID, new SingularValue<>(DBMetadata.USER_ROLE_EMPLOYEE));
            criteria.add(EntityParameters.UserParams.ROLE_ID, new SingularValue<>(DBMetadata.USER_ROLE_ADMIN));
        }
        return userService.getByCriteria(criteria);
    }

}
