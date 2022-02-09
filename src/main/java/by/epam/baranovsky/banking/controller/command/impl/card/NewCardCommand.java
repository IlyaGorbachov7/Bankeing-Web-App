package by.epam.baranovsky.banking.controller.command.impl.card;

import by.epam.baranovsky.banking.constant.*;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionAttributeName;
import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.entity.BankingCard;
import by.epam.baranovsky.banking.entity.User;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.entity.criteria.SingularValue;
import by.epam.baranovsky.banking.service.exception.ServiceException;

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

/**
 * Implementation of Command
 * used for creating new card.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class NewCardCommand extends AbstractCommand {

    public static final Integer MAX_CARDS = Integer.valueOf(ConfigManager.getInstance()
            .getValue(ConfigParams.CARDS_PER_USER));

    /**
     * {@inheritDoc}
     * <p>
     *     Forwards to previous request in case of failure,
     *     redirects to previous request otherwise.
     * </p>
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BankingCard newCard;

        try{
            Integer currentUser = (Integer) request.getSession().getAttribute(SessionAttributeName.USER_ID);
            String newUserParam = request.getParameter(RequestParamName.CARD_NEW_USER_ID);

            if(!canHaveMoreCards(currentUser)){
                request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.TOO_MANY_CARDS);
                request.getRequestDispatcher(getPreviousRequestAddress(request)).forward(request,response);
                return;
            }

            Integer newUserId;
            if(newUserParam != null && newUserParam.equals(String.valueOf(currentUser))){
                newUserId = currentUser;
            } else{
                newUserId = findUserIdByParamData(request);
            }
            if(newUserId==0){
                request.getRequestDispatcher(getPreviousRequestAddress(request)).forward(request,response);
                return;
            }

            newCard = buildDebitCard(request, newUserId);
            if(newCard == null){
                request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.NOT_YOUR_ACCOUNT);
                request.getRequestDispatcher(getPreviousRequestAddress(request)).forward(request,response);
            } else{
                cardService.create(newCard);
                response.sendRedirect(getPreviousRequestAddress(request));
            }
        } catch (ServiceException e) {
            logger.error(e);
            e.printStackTrace();
            request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.CARD_CREATE_EXCEPTION);
            request.getRequestDispatcher(getPreviousRequestAddress(request)).forward(request,response);
        }
    }

    /**
     * Generates number for a card.
     * @return String that depicts a 16-digit number of a card.
     */
    private String generateNumber(){
        Random random = new Random();
        return String.format("%04d%04d%04d%04d",
                random.nextInt(10000),
                random.nextInt(10000),
                random.nextInt(10000),
                random.nextInt(10000));
    }

    /**
     * Generates cvc code for a card.
     * @return String that depicts a 3-digit cvc code of a card.
     */
    private String generateCvc(){
        Random random = new Random();
        return String.format("%03d",  random.nextInt(1000));
    }

    /**
     * Generates pin code for a card.
     * @return String that depicts a 4-digit pin code of a card.
     */
    private String generatePin(){
        Random random = new Random();
        return String.format("%04d",  random.nextInt(10000));
    }

    /**
     * Builds new debit card.
     * @param request Servlet request.
     * @param newUser ID of user of a card.
     * @return Instance of BankingCard
     * or {@code null} if building was unsuccessful.
     * @throws ServiceException
     */
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

        Integer userId = (Integer) request.getSession().getAttribute(SessionAttributeName.USER_ID);

        newCard.setUserId(newUser);

        String accountNumber = request.getParameter(RequestParamName.ACCOUNT_NUMBER);
        if(accountNumber == null){
            return null;
        }
        Account account = accountService.findByNumber(accountNumber);

        if(!accountService.findUsers(account.getId()).contains(userId)){
                return null;
        }

        newCard.setAccountId(account.getId());

        return newCard;
    }

    /**
     * Retrieves users by data specified in request's parameters.
     * @param request Servlet request.
     * @return ID of a user fitting criteria or 0.
     * @throws ServiceException
     * @throws ServletException
     * @throws IOException
     * @see #handleQueryErrors(List, HttpServletRequest)
     * @see #createUserCriteria(HttpServletRequest)
     */
    private Integer findUserIdByParamData(HttpServletRequest request) throws ServiceException, ServletException, IOException {
        List<User> users = userService.getByCriteria(createUserCriteria(request));

        if(!handleQueryErrors(users,request)){
            return 0;
        }

        return users.get(0).getId();
    }

    /**
     * Checks if there are errors in results of a query
     * and sets error messages in request if there are.
     * @param list List of User objects retrieved by query.
     * @param request Servlet request.
     * @return {@code true} if only one unbanned user was retrieved,
     * {@code false} otherwise.
     */
    private boolean handleQueryErrors(List<User> list, HttpServletRequest request) {

        if(list.isEmpty()){
            request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.NO_SUCH_USER);
            return false;
        }

        if(list.size()>1){
            request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.AMBIGUOUS_USER_DATA);
            return false;
        }

        if(list.get(0).getRoleId().equals(DBMetadata.USER_ROLE_BANNED)){
            request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.USER_BANNED);
            return false;
        }
        return true;
    }

    /**
     * Generates Criteria object to find user by parameters
     * that are retrieved from request.
     * @param request Servlet request.
     * @return Instance of Criteria.
     */
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

    /**
     * Checks if user has reached his card limit.
     * @param userId ID of a user.
     * @return {@code true} if limit is not reached, {@code false} otherwise.
     * @throws ServiceException
     */
    private boolean canHaveMoreCards(Integer userId) throws ServiceException {
        return cardService.findByUser(userId).size()<MAX_CARDS;
    }
}
