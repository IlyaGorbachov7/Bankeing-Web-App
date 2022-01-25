package by.epam.baranovsky.banking.controller.command;


import by.epam.baranovsky.banking.constant.CommandName;
import by.epam.baranovsky.banking.controller.command.impl.*;
import by.epam.baranovsky.banking.controller.command.impl.gotocommand.GoToHomePageCommand;
import by.epam.baranovsky.banking.controller.command.impl.gotocommand.GoToLoginPageCommand;
import by.epam.baranovsky.banking.controller.command.impl.gotocommand.GoToRegisterPageCommand;
import by.epam.baranovsky.banking.controller.command.impl.gotocommand.GoToUserEditCommand;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class CommandProvider {

    private static volatile CommandProvider instance;
    private final Map<String, Command> commandMap = new HashMap<>();

    private CommandProvider(){
        commandMap.put(CommandName.GOTO_LOGIN, new GoToLoginPageCommand());
        commandMap.put(CommandName.GOTO_REGISTER, new GoToRegisterPageCommand());
        commandMap.put(CommandName.GOTO_MAIN, new GoToHomePageCommand());
        commandMap.put(CommandName.LOGIN_COMMAND, new LoginCommand());
        commandMap.put(CommandName.LOGOUT_COMMAND, new LogoutCommand());
        commandMap.put(CommandName.REGISTER_COMMAND, new RegisterCommand());
        commandMap.put(CommandName.LOCALE_CHANGE_COMMAND, new LocaleChangeCommand());
        commandMap.put(CommandName.GOTO_USER_EDIT, new GoToUserEditCommand());
        commandMap.put(CommandName.EDIT_USER, new EditUserCommand());
    }

    public static CommandProvider getInstance(){
        if (instance == null) {
            synchronized (CommandProvider.class) {
                if (instance == null) {
                    instance = new CommandProvider();
                }
            }
        }
        return instance;
    }

    public Command getCommand(HttpServletRequest request){
        String param = request.getParameter(RequestParamName.COMMAND_NAME);
        return commandMap.get(param);
    }

}
