package by.epam.baranovsky.banking.controller.command.impl;

import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionParamName;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

public class LocaleChangeCommand extends AbstractCommand {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        request.getSession().setAttribute(
                RequestParamName.LOCALE,
                request.getParameter(RequestParamName.LOCALE));

        Map<String, String[]> prevRequestParams =
                (Map<String, String[]>) session.getAttribute(SessionParamName.LAST_REQUEST);

        StringBuilder requestBuilder = new StringBuilder();
        requestBuilder.append(RequestParamName.CONTROLLER).append('?');
        for (Map.Entry<String, String[]> parameter : prevRequestParams.entrySet()){
            requestBuilder.append(parameter.getKey()).append('=');
            for (String parameterValue : parameter.getValue()){
                requestBuilder.append(parameterValue).append(',');
            }
            requestBuilder.deleteCharAt(requestBuilder.lastIndexOf(","));
            requestBuilder.append('&');
        }
        requestBuilder.deleteCharAt(requestBuilder.lastIndexOf("&"));

        response.sendRedirect(requestBuilder.toString());
    }

}
