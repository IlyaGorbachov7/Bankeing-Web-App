package by.epam.baranovsky.banking.controller.command.impl.bill;

import by.epam.baranovsky.banking.constant.ConfigManager;
import by.epam.baranovsky.banking.constant.ConfigParams;
import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.constant.Message;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionParamName;
import by.epam.baranovsky.banking.entity.Bill;
import by.epam.baranovsky.banking.entity.Penalty;
import by.epam.baranovsky.banking.entity.User;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class NewBillWithPenaltyCommand extends NewBillCommandNoPenalty {

    private static final Integer MIN_LENGTH_MONTHS = Integer.valueOf(
            ConfigManager.getInstance().getValue(ConfigParams.BILL_MIN_LENGTH));

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer currentUser = (Integer) request.getSession().getAttribute(SessionParamName.USER_ID);
        Integer accountId = Integer.valueOf(request.getParameter(RequestParamName.ACCOUNT_ID));
        String notice = request.getParameter(RequestParamName.BILL_NOTICE);
        String dueDateStr = request.getParameter(RequestParamName.BILL_DUE_DATE);
        Double value = Double.valueOf(request.getParameter(RequestParamName.BILL_VALUE));

        Integer penaltyType = Integer.valueOf(request.getParameter(RequestParamName.PENALTY_TYPE_ID));
        String penaltyNotice = request.getParameter(RequestParamName.PENALTY_NOTICE);
        String penaltyValue = request.getParameter(RequestParamName.PENALTY_VALUE);

        if(value<0){
            request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.OPERATION_INVALID_VALUE);
            request.getRequestDispatcher(getPreviousRequestAddress(request)).forward(request,response);
            return;
        }

        try{
            if(!validateAccount(accountId, currentUser)){
                request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.ACCOUNT_LOCKED_OR_NOT_YOURS);
                request.getRequestDispatcher(getPreviousRequestAddress(request)).forward(request,response);
                return;
            }

            List<User> list = userService.getByCriteria(createUserCriteria(request));
            if(noQueryErrors(list, request)){

                if(!checkTooManyBills(currentUser, list.get(0).getId())){
                    request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.TOO_MANY_BILL_REQUESTS);
                    request.getRequestDispatcher(getPreviousRequestAddress(request)).forward(request,response);
                    return;
                }

                Bill bill = new Bill();
                bill.setBearerId(currentUser);
                bill.setUserId(list.get(0).getId());
                bill.setValue(value);
                bill.setPaymentAccountId(accountId);
                bill.setStatusId(DBMetadata.BILL_STATUS_REQUESTED);
                bill.setIssueDate(new Date());
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                Date dueDate = new Date();
                try {
                    dueDate = format.parse(dueDateStr);
                } catch (ParseException e) {
                    logger.error("Error parsing due date ",e);
                }
                if(!checkDueDate(dueDate, bill.getIssueDate())){
                    request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.DUE_DATE_TOO_CLOSE);
                    request.getRequestDispatcher(getPreviousRequestAddress(request)).forward(request,response);
                    return;
                }
                bill.setDueDate(dueDate);
                bill.setNotice(notice);
                bill.setPenaltyId(createPenalty(penaltyType, penaltyNotice, penaltyValue, bill));
                billService.create(bill);
                response.sendRedirect(getPreviousRequestAddress(request));
            } else{
                request.getRequestDispatcher(getPreviousRequestAddress(request)).forward(request,response);
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }


    }


    private boolean checkDueDate(Date dueDate, Date issueDate){

        if(issueDate.compareTo(dueDate)>=0){
            return false;
        }

        Period period = Period.between(
                issueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                dueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        return period.toTotalMonths() > MIN_LENGTH_MONTHS;
    }

    private Integer createPenalty(Integer typeId, String notice, String value, Bill bill) throws ServiceException {
        Penalty penalty = new Penalty();

        penalty.setNotice(notice);
        penalty.setStatusId(DBMetadata.PENALTY_STATUS_UNASSIGNED);
        penalty.setUserId(bill.getUserId());
        penalty.setTypeId(typeId);
        penalty.setValue(
                typeId.equals(DBMetadata.PENALTY_TYPE_FEE)
                ? Double.valueOf(value) : null);
        penalty.setPaymentAccountId(
                typeId.equals(DBMetadata.PENALTY_TYPE_FEE)
                ? bill.getPaymentAccountId() : null);

        return penaltyService.create(penalty).getId();
    }

}
