package by.epam.baranovsky.banking.controller.command.impl;

import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Implementation of Command
 * used for locale change.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class LocaleChangeCommand extends AbstractCommand {

    /**
     * {@inheritDoc}
     * <p>
     *     Redirects to the previous request executed by user.
     * </p>
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().setAttribute(
                RequestParamName.LOCALE,
                request.getParameter(RequestParamName.LOCALE));

        response.sendRedirect(getPreviousRequestAddress(request));
    }

}
