package by.epam.baranovsky.banking.controller.command.impl.bill;

import by.epam.baranovsky.banking.constant.ConfigManager;
import by.epam.baranovsky.banking.constant.ConfigParams;
import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.constant.Message;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionParamName;
import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.entity.Bill;
import by.epam.baranovsky.banking.entity.User;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.entity.criteria.SingularValue;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewBillCommandNoPenalty extends AbstractCommand {

    protected static final Integer MAX_BILLS = Integer.valueOf(
            ConfigManager.getInstance().getValue(ConfigParams.BILLS_REQUESTS_MAX));

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer currentUser = (Integer) request.getSession().getAttribute(SessionParamName.USER_ID);
        Integer accountId = Integer.valueOf(request.getParameter(RequestParamName.ACCOUNT_ID));
        String notice = request.getParameter(RequestParamName.BILL_NOTICE);
        Double value = Double.valueOf(request.getParameter(RequestParamName.BILL_VALUE));

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
                bill.setStatusId(DBMetadata.BILL_STATUS_PENDING);
                bill.setIssueDate(new Date());
                bill.setNotice(notice);
                billService.create(bill);
                response.sendRedirect(getPreviousRequestAddress(request));
            } else{
                request.getRequestDispatcher(getPreviousRequestAddress(request)).forward(request,response);
            }
        } catch (ServiceException e) {
            logger.error(e);
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.ERROR_PAGE);
            dispatcher.forward(request, response);
        }

    }

    protected Criteria<EntityParameters.UserParams> createUserCriteria(HttpServletRequest request){
        Criteria<EntityParameters.UserParams> criteria = new Criteria<>();

        criteria.add(
                EntityParameters.UserParams.NAME,
                new SingularValue<>(request.getParameter(RequestParamName.NAME)));
        criteria.add(
                EntityParameters.UserParams.SURNAME,
                new SingularValue<>(request.getParameter(RequestParamName.SURNAME)));
        criteria.add(
                EntityParameters.UserParams.EMAIL,
                new SingularValue<>(request.getParameter(RequestParamName.EMAIL)));
        String patronymic = request.getParameter(RequestParamName.PATRONYMIC);
        if(patronymic != null && !patronymic.isBlank()){
            criteria.add(
                    EntityParameters.UserParams.PATRONYMIC,
                    new SingularValue<>(patronymic));
        }
        Date birthdate = null;
        try {
            birthdate = (new SimpleDateFormat("yyyy-MM-dd")).parse(request.getParameter(RequestParamName.BIRTHDATE));
        } catch (ParseException e) {
            logger.error("Error parsing birth date ",e);
        }
        criteria.add(
                EntityParameters.UserParams.BIRTHDATE,
                new SingularValue<Date>(birthdate));

        return criteria;
    }

    protected boolean noQueryErrors(List<User> list, HttpServletRequest request) {
        Integer currentUser = (Integer) request.getSession().getAttribute(SessionParamName.USER_ID);

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

        if(list.get(0).getId().equals(currentUser)){
            request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.BILL_TO_SELF);
            return false;
        }

        return true;
    }

    protected boolean validateAccount(Integer accountId, Integer currentUser) throws ServiceException {
        Account account = accountService.findById(accountId);

        if(account == null){
            return false;
        }

        account.setUsers(accountService.findUsers(accountId));

        if(!account.getUsers().contains(currentUser)){
            return false;
        }

        return !account.getStatusId().equals(DBMetadata.ACCOUNT_STATUS_BLOCKED)
                && !account.getStatusId().equals(DBMetadata.ACCOUNT_STATUS_PENDING);
    }

    protected boolean checkTooManyBills(Integer currentUser, Integer payingDude) throws ServiceException {
        Criteria<EntityParameters.BillParam> criteria = new Criteria<>();
        criteria.add(EntityParameters.BillParam.BEARER, new SingularValue<>(currentUser));
        criteria.add(EntityParameters.BillParam.USER, new SingularValue<>(payingDude));
        criteria.add(EntityParameters.BillParam.STATUS_ID,
                new SingularValue<>(DBMetadata.BILL_STATUS_OVERDUE));
        criteria.add(EntityParameters.BillParam.STATUS_ID,
                new SingularValue<>(DBMetadata.BILL_STATUS_PENDING));

        return billService.findByCriteria(criteria).size()<MAX_BILLS;
    }
}
