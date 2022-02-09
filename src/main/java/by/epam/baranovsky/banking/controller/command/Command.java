package by.epam.baranovsky.banking.controller.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Interface for servlet commands.
 * <p>
 *     Servlet commands are the essential parts of the system.
 *     They provide tools for interaction with user.
 * </p>
 * @author Baranovsky E. K.
 * @version 1.0.0
 * @see by.epam.baranovsky.banking.controller.Controller#process(HttpServletRequest, HttpServletResponse)
 */
public interface Command {

    /**
     * Controller calls this method to process request.
     * If any critical exception has occurred while command was executing,
     * forwards user to error page.
     * @param request Servlet request.
     * @param response Servlet response.
     * @throws ServletException
     * @throws IOException
     */
    void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
