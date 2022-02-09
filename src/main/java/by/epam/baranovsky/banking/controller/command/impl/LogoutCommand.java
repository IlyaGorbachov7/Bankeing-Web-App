package by.epam.baranovsky.banking.controller.command.impl;

import by.epam.baranovsky.banking.constant.CommandName;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionAttributeName;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Implementation of Command
 * used for logging out.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class LogoutCommand extends AbstractCommand {

    private static final String HOME = String.format(
            "%s?%s=%s", RequestParamName.CONTROLLER,
            RequestParamName.COMMAND_NAME, CommandName.GOTO_MAIN);

    /**
     * {@inheritDoc}
     * <p>
     *     If logging out was successful, redirects to home page.
     * </p>
     * <p>
     *     Removes user data from session.
     * </p>
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        session.setAttribute(SessionAttributeName.USER_ID, null);
        session.setAttribute(SessionAttributeName.USER_ROLE_ID, null);

        response.sendRedirect(HOME);
    }
}
