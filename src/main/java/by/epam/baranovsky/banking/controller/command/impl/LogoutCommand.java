package by.epam.baranovsky.banking.controller.command.impl;

import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.SessionParamName;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogoutCommand extends AbstractCommand {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        session.setAttribute(SessionParamName.USER_ID, null);
        session.setAttribute(SessionParamName.USER_ROLE_ID, null);

        RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.HOME_PAGE);
        dispatcher.forward(request, response);

    }
}
