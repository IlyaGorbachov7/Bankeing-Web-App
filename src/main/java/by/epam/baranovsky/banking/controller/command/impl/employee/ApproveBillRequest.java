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

public class ApproveBillRequest extends AbstractCommand {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer billId = Integer.valueOf(request.getParameter(RequestParamName.BILL_ID));

        try{
            Bill bill = billService.findById(billId);

            if(checkApprovalValidity(bill)){
                bill.setStatusId(DBMetadata.BILL_STATUS_PENDING);
                billService.update(bill);
                response.sendRedirect(getPreviousRequestAddress(request));
            } else{
                request.setAttribute(
                        RequestAttributeNames.ERROR_MSG,
                        Message.BILL_CANNOT_APPROVE);
                request.getRequestDispatcher(getPreviousRequestAddress(request)).forward(request,response);
            }

        } catch (ServiceException e) {
            logger.error(e);
            request.getRequestDispatcher(PageUrls.ERROR_PAGE).forward(request,response);
        }
    }

    private boolean checkApprovalValidity(Bill bill) {
        return bill.getStatusId().equals(DBMetadata.BILL_STATUS_REQUESTED);
    }
}
