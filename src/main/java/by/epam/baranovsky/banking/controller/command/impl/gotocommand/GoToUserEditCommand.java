package by.epam.baranovsky.banking.controller.command.impl.gotocommand;

import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.SessionParamName;
import by.epam.baranovsky.banking.entity.User;
import by.epam.baranovsky.banking.service.exception.ServiceException;
import by.epam.baranovsky.banking.service.impl.UserServiceImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GoToUserEditCommand extends AbstractCommand {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer userId = (Integer) request.getSession().getAttribute(SessionParamName.USER_ID);

        try {
            User user = userService.getById(userId);
            request.setAttribute(RequestAttributeNames.USER_DATA, user);
        } catch (ServiceException e) {
            logger.error(e);
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.ERROR_PAGE);
            dispatcher.forward(request, response);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.EDIT_USER_PAGE);
        dispatcher.forward(request, response);
    }

}
