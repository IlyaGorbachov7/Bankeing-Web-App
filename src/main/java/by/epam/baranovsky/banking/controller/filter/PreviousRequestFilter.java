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
        ignoredCommands.add(CommandName.REGISTER_COMMAND);
        ignoredCommands.add(CommandName.LOGIN_COMMAND);
        ignoredCommands.add(CommandName.NEW_CARD_COMMAND);
        ignoredCommands.add(CommandName.REMOVE_SELF_FROM_ACC_COMMAND);
        ignoredCommands.add(CommandName.GOTO_CARD_INFO_COMMAND);
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
