package by.epam.baranovsky.banking.controller.command.impl;

import by.epam.baranovsky.banking.constant.CommandName;
import by.epam.baranovsky.banking.constant.Message;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Implementation of Command
 * used for user registration.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class RegisterCommand extends AbstractCommand {

    private static final String REDIRECT_TO_HOME=String.format(
            "%s?%s=%s",
            RequestParamName.CONTROLLER,
            RequestParamName.COMMAND_NAME,
            CommandName.GOTO_MAIN);

    /**
     * {@inheritDoc}
     * <p>
     *     If registration is successful, redirects to home page,
     *     otherwise forwards back to registration page.
     * </p>
     * <p>
     *     Puts created user data to session.
     * </p>
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User user;

        if(validatePassword(request)){
            try{
               user = registerUser(request);
               HttpSession session = request.getSession();
               session.setAttribute(SessionAttributeName.USER_ID, user.getId());
               session.setAttribute(SessionAttributeName.USER_ROLE_ID, user.getRoleId());
               response.sendRedirect(REDIRECT_TO_HOME);
            } catch (ValidationException e){
                request.setAttribute(RequestAttributeNames.ERROR_MSG, e.getMessage());
                RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.REGISTER_PAGE);
                dispatcher.forward(request,response);
            } catch (ServiceException e) {
                logger.error("Authorisation error: ", e);
                request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.REGISTER_EXCEPTION);
                RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.REGISTER_PAGE);
                dispatcher.forward(request,response);
            }
        } else {
            request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.REGISTER_PASS_MISMATCH);
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.REGISTER_PAGE);
            dispatcher.forward(request,response);
        }

    }

    /**
     * Validates if password input matches password confirmation.
     * @param request Servlet request.
     * @return {@code true} if password input matches password confirmation,
     * {@code false} otherwise.
     */
    private boolean validatePassword(HttpServletRequest request){
        String pass = request.getParameter(RequestParamName.PASSWORD);
        String passConfirm = request.getParameter(RequestParamName.CONF_PASSWORD);

        return !pass.isBlank() && pass.equals(passConfirm);
    }

    /**
     * Builds user entity and saves it to database.
     * @param request Servlet request.
     * @return Registered user.
     * @throws ServiceException if userService throws ServiceException.
     */
    private User registerUser(HttpServletRequest request) throws ServiceException {
        String email = request.getParameter(RequestParamName.EMAIL);
        String password = request.getParameter(RequestParamName.PASSWORD);

        String firstName = request.getParameter(RequestParamName.NAME);
        String lastName = request.getParameter(RequestParamName.SURNAME);
        String patronymic = request.getParameter(RequestParamName.PATRONYMIC);

        String passportSeries = request.getParameter(RequestParamName.PASSPORT_SERIES);
        String passportNumber = request.getParameter(RequestParamName.PASSPORT_NUMBER);

        String birthdateStr = request.getParameter(RequestParamName.BIRTHDATE);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date birthdate = null;
        try {
            birthdate = format.parse(birthdateStr);
        } catch (ParseException e) {
            logger.error("Error parsing birth date ",e);
        }

        return userService.registerUser(email, password, lastName,
                firstName, patronymic, passportSeries, passportNumber, birthdate);
    }
}
