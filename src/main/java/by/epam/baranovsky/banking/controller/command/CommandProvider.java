package by.epam.baranovsky.banking.controller.command;


import by.epam.baranovsky.banking.constant.CommandName;
import by.epam.baranovsky.banking.controller.command.impl.*;
import by.epam.baranovsky.banking.controller.command.impl.account.AddUserToAccCommand;
import by.epam.baranovsky.banking.controller.command.impl.account.LockOrSuspendAccountCommand;
import by.epam.baranovsky.banking.controller.command.impl.account.NewAccCommand;
import by.epam.baranovsky.banking.controller.command.impl.account.RemoveSelfFromAccCommand;
import by.epam.baranovsky.banking.controller.command.impl.admin.ChangeUserRoleCommand;
import by.epam.baranovsky.banking.controller.command.impl.card.LockCardCommand;
import by.epam.baranovsky.banking.controller.command.impl.card.NewCardCommand;
import by.epam.baranovsky.banking.controller.command.impl.employee.*;
import by.epam.baranovsky.banking.controller.command.impl.gotocommand.*;
import by.epam.baranovsky.banking.controller.command.impl.transfer.TransferCommand;
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
        commandMap.put(CommandName.GOTO_BILLS, new GoToBillsPageCommand());
        commandMap.put(CommandName.GOTO_LOANS, new GoToLoansPageCommand());
        commandMap.put(CommandName.GOTO_CARDS, new GoToCardsPageCommand());
        commandMap.put(CommandName.GOTO_PENALTIES, new GoToPenaltiesPageCommand());

        commandMap.put(CommandName.GOTO_ACCOUNTS, new GoToAccountsPageCommand());
        commandMap.put(CommandName.GOTO_ACC_INFO_COMMAND, new GoToAccountInfoCommand());
        commandMap.put(CommandName.NEW_ACC_COMMAND, new NewAccCommand());
        commandMap.put(CommandName.LOCK_OR_SUSP_ACC_COMMAND, new LockOrSuspendAccountCommand());
        commandMap.put(CommandName.REMOVE_SELF_FROM_ACC_COMMAND, new RemoveSelfFromAccCommand());
        commandMap.put(CommandName.ADD_USER_TO_ACC_COMMAND, new AddUserToAccCommand());

        commandMap.put(CommandName.GOTO_CARD_INFO_COMMAND, new GoToCardInfoCommand());
        commandMap.put(CommandName.NEW_CARD_COMMAND, new NewCardCommand());
        commandMap.put(CommandName.LOCK_CARD_COMMAND, new LockCardCommand());

        commandMap.put(CommandName.TRANSFER_COMMAND, new TransferCommand());
        commandMap.put(CommandName.GOTO_TRANSFER_COMMAND, new GoToTransferPageCommand());
        commandMap.put(CommandName.GOTO_TRANSFER_CONFIRM_COMMAND, new GoToConfirmTransferCommand());
        commandMap.put(CommandName.GOTO_TRANSFER_SUCCESS_COMMAND, new GoToTransferSuccessCommand());

        commandMap.put(CommandName.GOTO_USER_INFO, new GoToUserInfoCommand());
        commandMap.put(CommandName.UPDATE_ACCOUNT, new UpdateAccount());
        commandMap.put(CommandName.CHANGE_USER_ROLE, new ChangeUserRoleCommand());
        commandMap.put(CommandName.GOTO_ALL_USERS, new GoToAllUsersCommand());
        commandMap.put(CommandName.GOTO_PENDING_ACCOUNTS, new GoToPendingAccountsCommand());
        commandMap.put(CommandName.DELETE_PENDING_ACC, new DeletePendingAccountCommand());

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
