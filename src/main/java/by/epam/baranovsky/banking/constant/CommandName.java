package by.epam.baranovsky.banking.constant;

public class CommandName {

    public static final String GOTO_LOGIN= "go_to_login_page";
    public static final String GOTO_REGISTER= "go_to_register_page";
    public static final String GOTO_MAIN= "go_to_main_page";
    public static final String GOTO_ACCOUNTS= "go_to_accounts_page";
    public static final String GOTO_CARDS= "go_to_cards_page";
    public static final String GOTO_PENALTIES= "go_to_penalties_page";
    public static final String GOTO_LOANS= "go_to_loans_page";
    public static final String GOTO_BILLS= "go_to_bills_page";
    public static final String GOTO_USER_EDIT= "go_to_user_edit";
    public static final String GOTO_ACC_INFO_COMMAND= "go_to_account_info";
    public static final String GOTO_CARD_INFO_COMMAND= "go_to_card_info";
    public static final String GOTO_TRANSFER_COMMAND= "go_to_transfer_page";
    public static final String GOTO_TRANSFER_CONFIRM_COMMAND= "go_to_transfer_confirm_page";
    public static final String GOTO_TRANSFER_SUCCESS_COMMAND= "go_to_transfer_success_page";


    public static final String LOGIN_COMMAND= "login";
    public static final String REGISTER_COMMAND= "register";
    public static final String LOGOUT_COMMAND= "logout";
    public static final String LOCALE_CHANGE_COMMAND= "change_locale";

    public static final String NEW_ACC_COMMAND= "new_account";
    public static final String LOCK_OR_SUSP_ACC_COMMAND= "lock_or_suspend_account";
    public static final String ADD_USER_TO_ACC_COMMAND= "add_account_user";
    public static final String REMOVE_SELF_FROM_ACC_COMMAND= "remove_account_user_self";

    public static final String NEW_CARD_COMMAND= "new_card";
    public static final String LOCK_CARD_COMMAND= "lock_card";

    public static final String TRANSFER_COMMAND= "transfer";

    public static final String EDIT_USER = "edit_user";

    public static final String GOTO_USER_INFO ="go_to_user_info";
    public static final String CHANGE_USER_ROLE="change_user_role";
    public static final String CHANGE_ACCOUNT_STATUS="change_account_status";
    public static final String GOTO_ALL_USERS="go_to_all_users";
    public static final String GOTO_PENDING_ACCOUNTS="go_to_pending_accounts";

    private CommandName(){}
}
