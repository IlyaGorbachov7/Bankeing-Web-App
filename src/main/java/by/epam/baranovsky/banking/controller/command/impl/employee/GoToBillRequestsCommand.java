package by.epam.baranovsky.banking.controller.command.impl.employee;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.controller.command.impl.gotocommand.GoToBillsPageCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.SessionAttributeName;
import by.epam.baranovsky.banking.entity.Bill;
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

/**
 * Implementation of Command
 * used for forwarding user to the page that lists
 * all bills that users have requested to create.
 * <p>Is made a subclass of GoToBillsPageCommand to re-use some of superclass methods.</p>
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class GoToBillRequestsCommand extends GoToBillsPageCommand {

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Integer currentUser = (Integer) request.getSession().getAttribute(SessionAttributeName.USER_ID);
        Integer currentRole = (Integer) request.getSession().getAttribute(SessionAttributeName.USER_ROLE_ID);

        try{
            request.setAttribute(
                    RequestAttributeNames.BILL_REQUESTS,
                    getBillDTOs(getRequestedBills(currentUser, currentRole)));
            request.getRequestDispatcher(PageUrls.PENDING_BILLS_PAGE).forward(request,response);
        } catch (ServiceException e) {
            logger.error(e);
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.ERROR_PAGE);
            dispatcher.forward(request, response);
        }

    }

    /**
     * Gets all bill requests. If current user is not an administrator,
     * does not collect bill requests left by (and for) current user.
     * @param currentUser Current user ID.
     * @param currentRole Current user's role ID.
     * @return List of bill requests available to browse.
     * @throws ServiceException
     */
    private List<Bill> getRequestedBills(Integer currentUser, Integer currentRole) throws ServiceException {
        Criteria<EntityParameters.BillParam> criteria = new Criteria<>();
        criteria.add(EntityParameters.BillParam.STATUS_ID, new SingularValue<>(DBMetadata.BILL_STATUS_REQUESTED));

        List<Bill> bills = billService.findByCriteria(criteria);
        if(!currentRole.equals(DBMetadata.USER_ROLE_ADMIN)) {
            bills.removeIf(bill -> bill.getBearerId().equals(currentUser) || bill.getUserId().equals(currentUser));
        }
        return bills;
    }

}
