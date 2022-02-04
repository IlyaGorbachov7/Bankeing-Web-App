package by.epam.baranovsky.banking.controller.filter;

import by.epam.baranovsky.banking.constant.CommandName;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionParamName;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PreviousRequestFilter implements Filter {

    private static final List<String> ignoredCommands = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ignoredCommands.add(CommandName.LOCALE_CHANGE_COMMAND);
        ignoredCommands.add(CommandName.GOTO_USER_INFO);
        ignoredCommands.add(CommandName.REGISTER_COMMAND);
        ignoredCommands.add(CommandName.LOGIN_COMMAND);
        ignoredCommands.add(CommandName.NEW_CARD_COMMAND);
        ignoredCommands.add(CommandName.LOCK_CARD_COMMAND);
        ignoredCommands.add(CommandName.LOCK_OR_SUSP_ACC_COMMAND);
        ignoredCommands.add(CommandName.TRANSFER_COMMAND);
        ignoredCommands.add(CommandName.REMOVE_SELF_FROM_ACC_COMMAND);
        ignoredCommands.add(CommandName.ADD_USER_TO_ACC_COMMAND);
        ignoredCommands.add(CommandName.NEW_ACC_COMMAND);
        ignoredCommands.add(CommandName.GOTO_CARD_INFO_COMMAND);
        ignoredCommands.add(CommandName.GOTO_TRANSFER_COMMAND);
        ignoredCommands.add(CommandName.GOTO_TRANSFER_CONFIRM_COMMAND);
        ignoredCommands.add(CommandName.UPDATE_ACCOUNT);
        ignoredCommands.add(CommandName.CHANGE_USER_ROLE);
        ignoredCommands.add(CommandName.EDIT_USER);
        ignoredCommands.add(CommandName.DELETE_PENDING_ACC);
        ignoredCommands.add(CommandName.LOCK_OR_UNLOCK_CARD_ADMIN);
        ignoredCommands.add(CommandName.NEW_BILL_NO_PENALTY_COMMAND);
        ignoredCommands.add(CommandName.NEW_BILL_WITH_PENALTY_COMMAND);
        ignoredCommands.add(CommandName.NEW_LOAN);
        ignoredCommands.add(CommandName.APPROVE_BILL);
        ignoredCommands.add(CommandName.DELETE_BILL_REQUEST);

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String command = servletRequest.getParameter(RequestParamName.COMMAND_NAME);
        if (!ignoredCommands.contains(command)){
            HttpSession session = ((HttpServletRequest)servletRequest).getSession();
            session.setAttribute(
                    SessionParamName.LAST_REQUEST,
                    new HashMap<>(servletRequest.getParameterMap()));
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
