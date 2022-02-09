package by.epam.baranovsky.banking.controller.command.impl.employee;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.constant.Message;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.entity.Bill;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Implementation of Command
 * used for denying (and deleting) requests of bills.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class DeleteBillRequest extends AbstractCommand {

    /**
     * {@inheritDoc}
     * <p>
     *     Forwards to previous request in case of failure,
     *     redirects to previous request otherwise.
     * </p>
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer billId = Integer.valueOf(request.getParameter(RequestParamName.BILL_ID));

        try{
            Bill bill = billService.findById(billId);

            if(checkDeletionValidity(bill)){
                if(bill.getPenaltyId() != null && bill.getPenaltyId() != 0) {
                    penaltyService.delete(bill.getPenaltyId());
                } else{
                    billService.delete(bill);
                }
                response.sendRedirect(getPreviousRequestAddress(request));
            } else{
                request.setAttribute(
                        RequestAttributeNames.ERROR_MSG,
                        Message.BILL_CANNOT_DELETE);
                request.getRequestDispatcher(getPreviousRequestAddress(request)).forward(request,response);
            }

        } catch (ServiceException e) {
            logger.error(e);
            request.getRequestDispatcher(PageUrls.ERROR_PAGE).forward(request,response);
        }

    }

    /**
     * Check if the bill that is being deleted has 'requested' status.
     * @param bill Bill to check.
     * @return {@code true} if bill has 'requested' status and thus can be deleted,
     * {@code false} otherwise.
     */
    private boolean checkDeletionValidity(Bill bill) {
        return bill.getStatusId().equals(DBMetadata.BILL_STATUS_REQUESTED);
    }
}
