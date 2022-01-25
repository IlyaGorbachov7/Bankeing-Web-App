package by.epam.baranovsky.banking.controller.command.impl;

import by.epam.baranovsky.banking.constant.CommandName;
import by.epam.baranovsky.banking.constant.Message;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionParamName;
import by.epam.baranovsky.banking.entity.User;
import by.epam.baranovsky.banking.service.UserService;
import by.epam.baranovsky.banking.service.exception.ServiceException;
import by.epam.baranovsky.banking.service.exception.ValidationException;
import by.epam.baranovsky.banking.service.impl.UserServiceImpl;

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

public class RegisterCommand extends AbstractCommand {

    private static final String REDIRECT_TO_HOME=String.format(
            "%s?%s=%s",
            RequestParamName.CONTROLLER,
            RequestParamName.COMMAND_NAME,
            CommandName.GOTO_MAIN);
    private final UserService service = UserServiceImpl.getInstance();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User user;

        if(validatePassword(request)){
            try{
               user = registerUser(request);
               HttpSession session = request.getSession();
               session.setAttribute(SessionParamName.USER_ID, user.getId());
               session.setAttribute(SessionParamName.USER_ROLE_ID, user.getRoleId());
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
        }else{
            request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.REGISTER_PASS_MISMATCH);
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.REGISTER_PAGE);
            dispatcher.forward(request,response);
        }

    }

    private boolean validatePassword(HttpServletRequest request){
        String pass = request.getParameter(RequestParamName.PARAM_NAME_PASSWORD);
        String passConfirm = request.getParameter(RequestParamName.PARAM_NAME_PASSWORD_CONFIRMATION);

        return !pass.isBlank() && pass.equals(passConfirm);
    }

    private User registerUser(HttpServletRequest request) throws ServiceException {
        String email = request.getParameter(RequestParamName.PARAM_NAME_EMAIL);
        String password = request.getParameter(RequestParamName.PARAM_NAME_PASSWORD);

        String firstName = request.getParameter(RequestParamName.PARAM_FIRST_NAME);
        String lastName = request.getParameter(RequestParamName.PARAM_LAST_NAME);
        String patronymic = request.getParameter(RequestParamName.PARAM_NAME_PATRONYMIC);

        String passportSeries = request.getParameter(RequestParamName.PARAM_NAME_PASSPORT_SERIES);
        String passportNumber = request.getParameter(RequestParamName.PARAM_NAME_PASSPORT_NUMBER);

        String birthdateStr = request.getParameter(RequestParamName.PARAM_NAME_BIRTHDATE);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date birthdate = null;
        try {
            birthdate = format.parse(birthdateStr);
        } catch (ParseException e) {
            logger.error("Error parsing birth date ",e);
        }

        return service.registerUser(email, password, lastName,
                firstName, patronymic, passportSeries, passportNumber, birthdate);
    }
}
