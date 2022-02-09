package by.epam.baranovsky.banking.controller.command.impl.gotocommand;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.SessionAttributeName;
import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.entity.Bill;
import by.epam.baranovsky.banking.entity.Penalty;
import by.epam.baranovsky.banking.entity.User;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.entity.criteria.SingularValue;
import by.epam.baranovsky.banking.entity.dto.BillTransferObject;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of Command
 * used to forward user to the page that lists all their bills.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class GoToBillsPageCommand extends AbstractCommand {

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer currentUser = (Integer) request.getSession().getAttribute(SessionAttributeName.USER_ID);

        try {
            request.setAttribute(
                    RequestAttributeNames.SENT_BILLS,
                    getBillDTOs(getSentBills(currentUser)));
            request.setAttribute(
                    RequestAttributeNames.ACQUIRED_BILLS,
                    getBillDTOs(getAcquiredBills(currentUser)));
            request.setAttribute(
                    RequestAttributeNames.USER_ACCOUNTS,
                    getUserAccountsToAcceptPayments(currentUser));
            request.getRequestDispatcher(PageUrls.BILLS_PAGE).forward(request,response);
        } catch (ServiceException e) {
            logger.error(e);
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.ERROR_PAGE);
            dispatcher.forward(request, response);
        }
    }

    /**
     * Wraps bills with transfer objects.
     * @param bills List of bills to parse.
     * @return List of instances of BillTransferObject parsed from bills.
     * @throws ServiceException
     */
    protected List<BillTransferObject> getBillDTOs(List<Bill> bills) throws ServiceException {
        List<BillTransferObject> dtoList = new ArrayList<>();

        for(Bill bill : bills){
            BillTransferObject bto = new BillTransferObject();
            bto.setId(bill.getId());
            bto.setValue(bill.getValue());

            bto.setUserId(bill.getUserId());
            bto.setUserFullName(getFullNameOfUser(bill.getUserId()));

            bto.setBearerId(bill.getBearerId());
            bto.setBearerFullName(getFullNameOfUser(bill.getBearerId()));

            bto.setIssueDate(bill.getIssueDate());
            bto.setDueDate(bill.getDueDate());

            bto.setStatusId(bill.getStatusId());
            bto.setNotice(bill.getNotice());

            bto.setPenaltyId(bill.getPenaltyId());
            bto.setLoanId(bill.getLoanId());

            if(bill.getPenaltyId() != null && bill.getPenaltyId()>0){
                Penalty penalty = penaltyService.findById(bill.getPenaltyId());
                bto.setPenaltyTypeId(penalty.getTypeId());
                bto.setPenaltyValue(penalty.getValue());
            }

            dtoList.add(bto);
        }

        return dtoList;
    }

    /**
     * Retrieves bills where user is recipient.
     * @param userId User in question.
     * @return List of bills where user is recipient.
     * @throws ServiceException
     */
    private List<Bill> getAcquiredBills(Integer userId) throws ServiceException {
        Criteria<EntityParameters.BillParam> criteria = new Criteria<>();
        criteria.add(EntityParameters.BillParam.USER, new SingularValue<>(userId));
        criteria.add(EntityParameters.BillParam.STATUS_ID, new SingularValue<>(DBMetadata.BILL_STATUS_PENDING));
        criteria.add(EntityParameters.BillParam.STATUS_ID, new SingularValue<>(DBMetadata.BILL_STATUS_OVERDUE));

        List<Bill> bills = billService.findByCriteria(criteria);
        bills.sort((o1, o2) -> {
            if(o2.getStatusId().equals(DBMetadata.BILL_STATUS_OVERDUE)){
                return -1;
            }
            return o2.getIssueDate().compareTo(o1.getIssueDate());
        });

        return bills;
    }

    /**
     * Retrieves bills where user is bearer.
     * @param userId User in question.
     * @return List of bills where user is bearer.
     * @throws ServiceException
     */
    private List<Bill> getSentBills(Integer userId) throws ServiceException {
        Criteria<EntityParameters.BillParam> criteria = new Criteria<>();
        criteria.add(EntityParameters.BillParam.BEARER, new SingularValue<>(userId));
        criteria.add(EntityParameters.BillParam.STATUS_ID, new SingularValue<>(DBMetadata.BILL_STATUS_PENDING));
        criteria.add(EntityParameters.BillParam.STATUS_ID, new SingularValue<>(DBMetadata.BILL_STATUS_OVERDUE));

        List<Bill> bills = billService.findByCriteria(criteria);
        bills.sort((o1, o2) -> {
            if(o2.getStatusId().equals(DBMetadata.BILL_STATUS_OVERDUE)){
                return -1;
            }
            return o2.getIssueDate().compareTo(o1.getIssueDate());
        });

        return bills;
    }

    /**
     * Retrieves full name of the user.
     * @param userId User in question.
     * @return String representation of user's full name.
     * @throws ServiceException
     */
    private String getFullNameOfUser(Integer userId) throws ServiceException {
        User user = userService.getById(userId);
        return user.getLastName() + " " + user.getFirstName() + " " + (user.getPatronymic() != null ? user.getPatronymic() :  "");
    }

    /**
     * Retrieves all accounts that belong to the user
     * and that can be used to receive payments at given moment.
     * @param userId User in question.
     * @return List of accounts ready to accept payments.
     * @throws ServiceException
     */
    private List<Account> getUserAccountsToAcceptPayments(Integer userId) throws ServiceException {
        List<Account> accounts = accountService.findByUserId(userId);

        accounts.removeIf(account ->
                account.getStatusId().equals(DBMetadata.ACCOUNT_STATUS_BLOCKED)
                        || account.getStatusId().equals(DBMetadata.ACCOUNT_STATUS_PENDING));

        return accounts;

    }
}
