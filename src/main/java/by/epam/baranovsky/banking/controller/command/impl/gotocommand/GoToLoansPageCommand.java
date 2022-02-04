package by.epam.baranovsky.banking.controller.command.impl.gotocommand;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionParamName;
import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.entity.Loan;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.entity.criteria.SingularValue;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class GoToLoansPageCommand extends AbstractCommand {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer currentUser = (Integer) request.getSession().getAttribute(SessionParamName.USER_ID);

        try{
            request.setAttribute(
                    RequestAttributeNames.USER_LOANS,
                    getUserLoans(currentUser));
            request.setAttribute(
                    RequestAttributeNames.USER_ACCOUNTS,
                    getUserAccountsToAcceptPayments(currentUser));
            request.getRequestDispatcher(PageUrls.LOANS_PAGE).forward(request,response);
        } catch (ServiceException e) {
            logger.error(e);
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.ERROR_PAGE);
            dispatcher.forward(request, response);
        }

    }

    private List<Loan> getUserLoans(Integer userId) throws ServiceException {
        Criteria<EntityParameters.LoanParams> criteria = new Criteria<>();
        criteria.add(EntityParameters.LoanParams.USER, new SingularValue<>(userId));

        List<Loan> loans = loanService.findByCriteria(criteria);
        loans.removeIf(loan -> loan.getCardId() != null && loan.getCardId() != 0);
        return loans;
    }

    private List<Account> getUserAccountsToAcceptPayments(Integer userId) throws ServiceException {
        List<Account> accounts = accountService.findByUserId(userId);

        accounts.removeIf(account ->
                account.getStatusId().equals(DBMetadata.ACCOUNT_STATUS_BLOCKED)
                        || account.getStatusId().equals(DBMetadata.ACCOUNT_STATUS_PENDING));

        return accounts;

    }

}
