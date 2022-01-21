package by.epam.baranovsky.banking.controller.command;


import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class CommandProvider {

    private static final Logger logger = Logger.getLogger(CommandProvider.class);
    private static volatile CommandProvider instance;
    private final Map<String, Command> commandMap = new HashMap<>();

    private CommandProvider(){
        //todo commands

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
