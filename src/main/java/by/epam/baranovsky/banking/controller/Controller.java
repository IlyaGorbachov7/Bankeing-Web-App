package by.epam.baranovsky.banking.controller;

import by.epam.baranovsky.banking.controller.command.Command;
import by.epam.baranovsky.banking.controller.command.CommandProvider;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Controller extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        process(req, resp);
    }

    private void process(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        Command command = CommandProvider.getInstance().getCommand(request);
        command.execute(request,response);
    }
}
