package by.epam.baranovsky.banking.controller.command.impl.admin;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.constant.Message;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.entity.User;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ChangeUserRoleCommand extends AbstractCommand {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Integer newRole = Integer.valueOf(request.getParameter(RequestParamName.USER_NEW_ROLE));
        Integer userToModify = Integer.valueOf(request.getParameter(RequestParamName.ID_CHECKED_USER));

        try {
            if(checkRoleChangeValidity(newRole)){
                User user = userService.getById(userToModify);
                user.setRoleId(newRole);
                userService.updateUser(user);
                response.sendRedirect(getPreviousRequestAddress(request));
            } else {
                request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.WRONG_NEW_ROLE);
                request.getRequestDispatcher(getPreviousRequestAddress(request)).forward(request,response);
            }

        } catch (ServiceException e) {
            logger.error(e);
            request.getRequestDispatcher(PageUrls.ERROR_PAGE).forward(request, response);

        }

    }


    private boolean checkRoleChangeValidity(Integer newRole){
        return newRole.equals(DBMetadata.USER_ROLE_ADMIN)
                || newRole.equals(DBMetadata.USER_ROLE_EMPLOYEE)
                || newRole.equals(DBMetadata.USER_ROLE_REGULAR)
                || newRole.equals(DBMetadata.USER_ROLE_BANNED);
    }
}
