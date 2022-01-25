package by.epam.baranovsky.banking.controller.command.impl.gotocommand;

import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GoToLoginPageCommand extends AbstractCommand {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.LOGIN_PAGE);
        dispatcher.forward(request, response);
    }
}
