package by.epam.baranovsky.banking.controller.command.impl.card;

import by.epam.baranovsky.banking.constant.CommandName;
import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.constant.Message;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionParamName;
import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.entity.BankingCard;
import by.epam.baranovsky.banking.entity.User;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.entity.criteria.SingularValue;
import by.epam.baranovsky.banking.service.AccountService;
import by.epam.baranovsky.banking.service.BankCardService;
import by.epam.baranovsky.banking.service.UserService;
import by.epam.baranovsky.banking.service.exception.ServiceException;
import by.epam.baranovsky.banking.service.impl.AccountServiceImpl;
import by.epam.baranovsky.banking.service.impl.BankCardServiceImpl;
import by.epam.baranovsky.banking.service.impl.UserServiceImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public class NewCardCommand extends AbstractCommand {

    private static final String REDIRECT_TO_CARDS=String.format(
            "%s?%s=%s",
            RequestParamName.CONTROLLER,
            RequestParamName.COMMAND_NAME,
            CommandName.GOTO_CARDS);

    public static final BankCardService cardService = BankCardServiceImpl.getInstance();
    public static final AccountService accountService = AccountServiceImpl.getInstance();
    private static final UserService userService = UserServiceImpl.getInstance();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BankingCard newCard;

        try{
            Integer currentUser = (Integer) request.getSession().getAttribute(SessionParamName.USER_ID);
            String newUserParam = request.getParameter(RequestParamName.CARD_NEW_USER_ID);
            Integer newUserId;
            if(newUserParam != null && newUserParam.equals(String.valueOf(currentUser))){
                newUserId = currentUser;
            } else{
                newUserId = findUserIdByParamData(request,response);
            }

            if(newUserId==0){
                RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.CARDS_PAGE);
                dispatcher.forward(request, response);
                return;
            }

            newCard = buildDebitCard(request, newUserId);
            if(newCard == null){
                request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.NOT_YOUR_ACCOUNT);
                request.getRequestDispatcher(PageUrls.CARDS_PAGE).forward(request,response);
            } else{
                cardService.create(newCard);
                response.sendRedirect(REDIRECT_TO_CARDS);
            }
        } catch (ServiceException e) {
            logger.error(e);
            request.getRequestDispatcher(PageUrls.ERROR_PAGE).forward(request,response);
        }
    }

    private String generateNumber(){
        Random random = new Random();
        return String.format("%04d%04d%04d%04d",
                random.nextInt(10000),
                random.nextInt(10000),
                random.nextInt(10000),
                random.nextInt(10000));
    }

    private String generateCvc(){
        Random random = new Random();
        return String.format("%03d",  random.nextInt(1000));
    }

    private String generatePin(){
        Random random = new Random();
        return String.format("%04d",  random.nextInt(10000));
    }

    private BankingCard buildDebitCard(HttpServletRequest request, Integer newUser) throws ServiceException {
        BankingCard newCard = new BankingCard();

        String number;
        String cvc;
        do{
            number = generateNumber();
            cvc = generateCvc();
        } while (cardService.findByNumberAndCvc(number, cvc) != null);
        newCard.setNumber(number);
        newCard.setCvc(cvc);
        newCard.setPin(generatePin());
        newCard.setCardTypeId(DBMetadata.CARD_TYPE_DEBIT);
        newCard.setStatusId(DBMetadata.CARD_STATUS_UNLOCKED);

        LocalDate today = LocalDate.now();
        LocalDate expiration = today.plusYears(4);

        newCard.setRegistrationDate(Date.valueOf(today));
        newCard.setExpirationDate(Date.valueOf(expiration));

        Integer userId = (Integer) request.getSession().getAttribute(SessionParamName.USER_ID);

        newCard.setUserId(newUser);

        String accountNumber = request.getParameter(RequestParamName.ACCOUNT_NUMBER);
        Account account = accountService.findByNumber(accountNumber);

        if(!accountService.findUsers(account.getId()).contains(userId)){
                return null;
        }

        newCard.setAccountId(account.getId());

        return newCard;
    }

    private Integer findUserIdByParamData(HttpServletRequest request, HttpServletResponse response) throws ServiceException, ServletException, IOException {
        List<User> users = userService.getByCriteria(createUserCriteria(request));

        if(!handleQueryErrors(users,request,response)){
            return 0;
        }

        return users.get(0).getId();
    }

    private boolean handleQueryErrors(List<User> list, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(list.isEmpty()){
            request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.NO_SUCH_USER);
            return false;
        }

        if(list.size()>1){
            request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.AMBIGUOUS_USER_DATA);
            return false;
        }

        if(list.get(0).getRoleId().equals(DBMetadata.USERS_ROLE_BANNED)){
            request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.USER_BANNED);
            return false;
        }
        return true;
    }

    private Criteria<EntityParameters.UserParams> createUserCriteria(HttpServletRequest request){
        Criteria<EntityParameters.UserParams> criteria = new Criteria<>();

        criteria.add(
                EntityParameters.UserParams.NAME,
                new SingularValue<>(request.getParameter(RequestParamName.NAME)));
        criteria.add(
                EntityParameters.UserParams.SURNAME,
                new SingularValue<>(request.getParameter(RequestParamName.SURNAME)));
        String patronymic = request.getParameter(RequestParamName.PATRONYMIC);
        if(patronymic != null && !patronymic.isBlank()){
            criteria.add(
                    EntityParameters.UserParams.PATRONYMIC,
                    new SingularValue<>(patronymic));
        }
        criteria.add(
                EntityParameters.UserParams.PASSPORT_SERIES,
                new SingularValue<>(request.getParameter(RequestParamName.PASSPORT_SERIES)));
        criteria.add(
                EntityParameters.UserParams.PASSPORT_NUMBER,
                new SingularValue<>(request.getParameter(RequestParamName.PASSPORT_NUMBER)));
        java.util.Date birthdate = null;
        try {
            birthdate = (new SimpleDateFormat("yyyy-MM-dd")).parse(request.getParameter(RequestParamName.BIRTHDATE));
        } catch (ParseException e) {
            logger.error("Error parsing birth date ",e);
        }
        criteria.add(
                EntityParameters.UserParams.BIRTHDATE,
                new SingularValue<java.util.Date>(birthdate));

        return criteria;
    }

}
