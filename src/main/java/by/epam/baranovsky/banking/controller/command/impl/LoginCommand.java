package by.epam.baranovsky.banking.controller.command.impl;

import by.epam.baranovsky.banking.constant.CommandName;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.constant.Message;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionAttributeName;
import by.epam.baranovsky.banking.entity.User;
import by.epam.baranovsky.banking.service.exception.ServiceException;
import by.epam.baranovsky.banking.service.exception.ValidationException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Implementation of Command
 * used for user authentication.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class LoginCommand extends AbstractCommand {

    private static final String REDIRECT_TO_HOME=String.format(
            "%s?%s=%s",
            RequestParamName.CONTROLLER,
            RequestParamName.COMMAND_NAME,
            CommandName.GOTO_MAIN);

    /**
     * {@inheritDoc}
     * <p>
     *     If authentication was successful, redirects to home page.
     *     Otherwise, forwards back to authentication page.
     * </p>
     * <p>
     *     Puts retrieved user data to session.
     * </p>
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String email = request.getParameter(RequestParamName.EMAIL);
        String password = request.getParameter(RequestParamName.PASSWORD);

        User user;

        try{
            user = userService.loginUser(email, password);
            HttpSession session = request.getSession();
            session.setAttribute(SessionAttributeName.USER_ID, user.getId());
            session.setAttribute(SessionAttributeName.USER_ROLE_ID, user.getRoleId());
            response.sendRedirect(REDIRECT_TO_HOME);
        } catch (ValidationException e){
            request.setAttribute(RequestAttributeNames.ERROR_MSG, e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.LOGIN_PAGE);
            dispatcher.forward(request,response);
        }catch (ServiceException e) {
            logger.error("Authorisation error: ", e);
            request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.LOGIN_EXCEPTION);
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.LOGIN_PAGE);
            dispatcher.forward(request,response);
        }
    }
}
