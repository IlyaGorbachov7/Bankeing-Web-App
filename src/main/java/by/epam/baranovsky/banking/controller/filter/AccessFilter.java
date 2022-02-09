package by.epam.baranovsky.banking.controller.filter;

import by.epam.baranovsky.banking.constant.CommandName;
import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionAttributeName;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet filter that controls access to servlet commands depending on user's role.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class AccessFilter implements Filter {

    /** List of commands accessible to regular user*/
    private final List<String> regularUserCommands = new ArrayList<>();
    /** List of commands accessible to an employee*/
    private final List<String> employeeCommands = new ArrayList<>();

    /** List of commands accessible to guest*/
    private final List<String> guestCommands = new ArrayList<>();

    /**
     * {@inheritDoc}
     * <p>Initializes filter and puts command names into access lists.</p>
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        guestCommands.add(CommandName.LOGIN_COMMAND);
        guestCommands.add(CommandName.REGISTER_COMMAND);
        guestCommands.add(CommandName.GOTO_LOGIN);
        guestCommands.add(CommandName.GOTO_REGISTER);
        guestCommands.add(CommandName.GOTO_MAIN);
        guestCommands.add(CommandName.LOCALE_CHANGE_COMMAND);

        employeeCommands.add(CommandName.GOTO_PENDING_ACCOUNTS);
        employeeCommands.add(CommandName.GOTO_ALL_USERS);
        employeeCommands.add(CommandName.GOTO_USER_INFO);
        employeeCommands.add(CommandName.UPDATE_ACCOUNT);
        employeeCommands.add(CommandName.DELETE_PENDING_ACC);
        employeeCommands.add(CommandName.LOCK_OR_UNLOCK_CARD_ADMIN);
        employeeCommands.add(CommandName.GOTO_BILL_REQUESTS);
        employeeCommands.add(CommandName.DELETE_BILL_REQUEST);
        employeeCommands.add(CommandName.APPROVE_BILL);

        regularUserCommands.add(CommandName.LOGOUT_COMMAND);
        regularUserCommands.add(CommandName.GOTO_ACC_INFO_COMMAND);
        regularUserCommands.add(CommandName.GOTO_ACCOUNTS);
        regularUserCommands.add(CommandName.GOTO_BILLS);
        regularUserCommands.add(CommandName.GOTO_CARD_INFO_COMMAND);
        regularUserCommands.add(CommandName.GOTO_CARDS);
        regularUserCommands.add(CommandName.GOTO_TRANSFER_COMMAND);
        regularUserCommands.add(CommandName.GOTO_TRANSFER_CONFIRM_COMMAND);
        regularUserCommands.add(CommandName.GOTO_LOANS);
        regularUserCommands.add(CommandName.GOTO_PENALTIES);
        regularUserCommands.add(CommandName.GOTO_TRANSFER_SUCCESS_COMMAND);
        regularUserCommands.add(CommandName.GOTO_USER_EDIT);
        regularUserCommands.add(CommandName.EDIT_USER);
        regularUserCommands.add(CommandName.TRANSFER_COMMAND);
        regularUserCommands.add(CommandName.LOCK_CARD_COMMAND);
        regularUserCommands.add(CommandName.NEW_CARD_COMMAND);
        regularUserCommands.add(CommandName.NEW_ACC_COMMAND);
        regularUserCommands.add(CommandName.LOCK_OR_SUSP_ACC_COMMAND);
        regularUserCommands.add(CommandName.ADD_USER_TO_ACC_COMMAND);
        regularUserCommands.add(CommandName.REMOVE_SELF_FROM_ACC_COMMAND);
        regularUserCommands.add(CommandName.NEW_BILL_NO_PENALTY_COMMAND);
        regularUserCommands.add(CommandName.NEW_BILL_WITH_PENALTY_COMMAND);
        regularUserCommands.add(CommandName.NEW_LOAN);
    }

    /**
     * {@inheritDoc}
     * <p>
     *     Determines if current user has access to the command they're trying to execute.
     *     If they have no access rights, they are forwarded to error page instead.
     * </p>
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        Integer currentUserRoleId = (Integer) request.getSession().getAttribute(SessionAttributeName.USER_ROLE_ID);
        String command = request.getParameter(RequestParamName.COMMAND_NAME);

        if(!checkIfAllowed(command, currentUserRoleId)){
            request.getRequestDispatcher(PageUrls.ERROR_PAGE).forward(request,servletResponse);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {

    }

    /**
     * Checks if execution of a command is allowed to current user.
     * @param command Name of command in question.
     * @param role Role of current user.
     * @return {@code true} if command is within the list
     * that correlates with current user's role or if current user is administrator,
     * {@code false} otherwise.
     */
    private boolean checkIfAllowed(String command, Integer role){
        if (command==null || command.isBlank()){
            return false;
        }

        if(role == null && !guestCommands.contains(command)){
           return false;
        }

        if(DBMetadata.USER_ROLE_REGULAR.equals(role)
                && !guestCommands.contains(command)
                && !regularUserCommands.contains(command)){
            return false;
        }

        if(DBMetadata.USER_ROLE_EMPLOYEE.equals(role)
                && !guestCommands.contains(command)
                && !regularUserCommands.contains(command)
                && !employeeCommands.contains(command)){
            return false;
        }

        return true;
    }
}
