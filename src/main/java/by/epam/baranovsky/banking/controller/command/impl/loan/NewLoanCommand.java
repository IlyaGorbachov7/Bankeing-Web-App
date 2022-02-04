package by.epam.baranovsky.banking.controller.command.impl.loan;

import by.epam.baranovsky.banking.constant.ConfigManager;
import by.epam.baranovsky.banking.constant.ConfigParams;
import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.constant.Message;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionParamName;
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

public class NewLoanCommand extends AbstractCommand {

    private static final Integer MIN_TIME = Integer.valueOf(
            ConfigManager.getInstance().getValue(ConfigParams.LOANS_MIN_TIME));
    private static final Integer MAX_TIME = Integer.valueOf(
            ConfigManager.getInstance().getValue(ConfigParams.LOANS_MAX_TIME));
    private static final Double MAX_INTEREST = Double.valueOf(
            ConfigManager.getInstance().getValue(ConfigParams.LOANS_MAX_INTEREST));
    private static final Double MIN_INTEREST = Double.valueOf(
            ConfigManager.getInstance().getValue(ConfigParams.LOANS_MIN_INTEREST));
    private static final Double MIN_VALUE = Double.valueOf(
            ConfigManager.getInstance().getValue(ConfigParams.LOAN_MIN_VALUE));
    private static final Integer MAX_ACTIVE_LOANS = Integer.valueOf(
            ConfigManager.getInstance().getValue(ConfigParams.LOANS_MAX_LOANS));


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer currentUser = (Integer) request.getSession().getAttribute(SessionParamName.USER_ID);
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

    private boolean checkIfCanGetLoan(Integer currentUser) throws ServiceException {
        Criteria<EntityParameters.LoanParams> criteria = new Criteria<>();
        criteria.add(EntityParameters.LoanParams.USER, new SingularValue<>(currentUser));
        criteria.add(EntityParameters.LoanParams.STATUS,
                new SingularValue<>(DBMetadata.LOAN_STATUS_PENDING));
        criteria.add(EntityParameters.LoanParams.STATUS,
                new SingularValue<>(DBMetadata.LOAN_STATUS_OVERDUE));

        return  loanService.findByCriteria(criteria).size()<MAX_ACTIVE_LOANS;
    }

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
