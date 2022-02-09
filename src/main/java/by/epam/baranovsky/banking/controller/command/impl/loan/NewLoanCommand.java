package by.epam.baranovsky.banking.controller.command.impl.loan;

import by.epam.baranovsky.banking.constant.ConfigManager;
import by.epam.baranovsky.banking.constant.ConfigParams;
import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.constant.Message;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionAttributeName;
import by.epam.baranovsky.banking.entity.Loan;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.entity.criteria.SingularValue;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Implementation of Command
 * used for taking a loan.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class NewLoanCommand extends AbstractCommand {

    /** Minimal loan term. */
    private static final Integer MIN_TIME = Integer.valueOf(
            ConfigManager.getInstance().getValue(ConfigParams.LOANS_MIN_TIME));
    /** Maximal loan term. */
    private static final Integer MAX_TIME = Integer.valueOf(
            ConfigManager.getInstance().getValue(ConfigParams.LOANS_MAX_TIME));
    /** Minimal loan interest rate. */
    private static final Double MAX_INTEREST = Double.valueOf(
            ConfigManager.getInstance().getValue(ConfigParams.LOANS_MAX_INTEREST));
    /** Minimal loan interest rate. */
    private static final Double MIN_INTEREST = Double.valueOf(
            ConfigManager.getInstance().getValue(ConfigParams.LOANS_MIN_INTEREST));

    /** Minimal loan value. */
    private static final Double MIN_VALUE = Double.valueOf(
            ConfigManager.getInstance().getValue(ConfigParams.LOAN_MIN_VALUE));

    /** Maximal amount of active loans for one user. */
    private static final Integer MAX_ACTIVE_LOANS = Integer.valueOf(
            ConfigManager.getInstance().getValue(ConfigParams.LOANS_MAX_LOANS));

    /**
    * {@inheritDoc}
     * <p>
     *     Forwards to previous request page if loan was taken unsuccessfully,
     *     redirects to previous request page otherwise.
     * </p>
    */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer currentUser = (Integer) request.getSession().getAttribute(SessionAttributeName.USER_ID);
        Integer accountId = Integer.valueOf(request.getParameter(RequestParamName.ACCOUNT_ID));
        Double startingValue = Double.parseDouble(request.getParameter(RequestParamName.LOAN_STARTING));

        int length = Integer.parseInt(request.getParameter(RequestParamName.LOAN_MONTHS));

        if(length<MIN_TIME || length>MAX_TIME){
            request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.LOAN_TOO_SHORT_OR_LONG);
            request.getRequestDispatcher(getPreviousRequestAddress(request)).forward(request,response);
            return;
        }
        if(startingValue<MIN_VALUE){
            request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.LOAN_VALUE_TOO_SMALL);
            request.getRequestDispatcher(getPreviousRequestAddress(request)).forward(request,response);
            return;
        }
        try{
            if(!accountService.findUsers(accountService.findById(accountId).getId()).contains(currentUser)){
                request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.LOAN_NOT_FOR_YOUR_ACCOUNT);
                request.getRequestDispatcher(getPreviousRequestAddress(request)).forward(request,response);
                return;
            }
            if(accountService.findById(DBMetadata.BANK_ACCOUNT_ID).getBalance()<startingValue){
                request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.NO_MONEY);
                request.getRequestDispatcher(getPreviousRequestAddress(request)).forward(request,response);
                return;
            }
            if(!checkIfCanGetLoan(currentUser)){
                request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.TOO_MANY_LOANS);
                request.getRequestDispatcher(getPreviousRequestAddress(request)).forward(request,response);
                return;
            }

            Loan loan = calculateLoan(startingValue, length);
            loan.setStatusId(DBMetadata.LOAN_STATUS_PENDING);
            loan.setIssueDate(new Date());
            loan.setAccountId(accountId);
            loan.setDueDate(Date.from((LocalDate.now().plusMonths(length)).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            loan.setUserId(currentUser);

            loanService.create(loan);
            response.sendRedirect(getPreviousRequestAddress(request));
        } catch (ServiceException e) {
            logger.error(e);
            request.getRequestDispatcher(PageUrls.ERROR_PAGE).forward(request,response);
        }

    }

    /**
     * Checks if user can take a new loan.
     * User with too much active loans can not take a new one.
     * @param currentUser Current user's ID.
     * @return {@code true} if current user can take a new loan,
     * {@code false} otherwise.
     * @throws ServiceException
     */
    private boolean checkIfCanGetLoan(Integer currentUser) throws ServiceException {
        Criteria<EntityParameters.LoanParams> criteria = new Criteria<>();
        criteria.add(EntityParameters.LoanParams.USER, new SingularValue<>(currentUser));
        criteria.add(EntityParameters.LoanParams.STATUS,
                new SingularValue<>(DBMetadata.LOAN_STATUS_PENDING));
        criteria.add(EntityParameters.LoanParams.STATUS,
                new SingularValue<>(DBMetadata.LOAN_STATUS_OVERDUE));

        return  loanService.findByCriteria(criteria).size()<MAX_ACTIVE_LOANS;
    }

    /**
     * Calculates loan parameters:
     * <ul>
     *     <li>Total value.</li>
     *     <li>Interest rate</li>
     *     <li>Singular payment value.</li>
     * </ul>
     * And builds a Loan entity with them.
     * @param startingValue Starting value of a loan.
     * @param time Term of a loan.
     * @return Instance of Loan.
     */
    private Loan calculateLoan(Double startingValue, Integer time){
        Loan loan = new Loan();
        loan.setStartingValue(startingValue);
        double deltaInterest = MAX_INTEREST-MIN_INTEREST;
        double deltaTime = MAX_TIME-MIN_TIME;
        double interest = MIN_INTEREST + deltaInterest*(1-((time-MIN_TIME)/deltaTime));
        loan.setYearlyInterestRate(interest);

        double mathematicalInterest = 1d + interest/100d;
        double timePower = time/12d;
        double totalValue = startingValue*Math.pow(mathematicalInterest, timePower);
        loan.setTotalPaymentValue(totalValue);

        loan.setSinglePaymentValue(totalValue/time);
        return loan;
    }
}
