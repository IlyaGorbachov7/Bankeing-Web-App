package by.epam.baranovsky.banking.controller.command.impl.account;

import by.epam.baranovsky.banking.constant.CommandName;
import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.constant.Message;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionParamName;
import by.epam.baranovsky.banking.entity.Account;
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

public class AddUserToAccCommand extends AbstractCommand {

    private static final String REDIRECT_TO_ACC_INFO=String.format(
            "%s?%s=%s",
            RequestParamName.CONTROLLER,
            RequestParamName.COMMAND_NAME,
            CommandName.GOTO_ACC_INFO_COMMAND);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user;
        try{
            Account account = accountService.findById(Integer.valueOf(request.getParameter(RequestParamName.ACCOUNT_ID)));
            account.setUsers(accountService.findUsers(account.getId()));
            Integer currentUser = (Integer) request.getSession().getAttribute(SessionParamName.USER_ID);
            if(account.getStatusId().equals(DBMetadata.ACCOUNT_STATUS_BLOCKED)){
                request.setAttribute(RequestAttributeNames.ERROR_MSG, Message.ACCOUNT_LOCKED);
                request.getRequestDispatcher(PageUrls.ACCOUNT_INFO_PAGE).forward(request,response);
                return;
            }

            if(!account.getUsers().contains(currentUser)){
                RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.ERROR_PAGE);
                dispatcher.forward(request, response);
                return;
            }

            List<User> list = userService.getByCriteria(createUserCriteria(request));
            if(!handleQueryErrors(list, request, response)){
                request.getRequestDispatcher(PageUrls.ACCOUNT_INFO_PAGE).forward(request,response);
                return;
            }
            account.addUser(list.get(0).getId());
            accountService.update(account);
            response.sendRedirect(REDIRECT_TO_ACC_INFO+"&"+RequestParamName.ACCOUNT_ID+"="+account.getId());
        }catch (ServiceException e){
            logger.error(e);
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageUrls.ERROR_PAGE);
            dispatcher.forward(request, response);
        }

    }

    private boolean handleQueryErrors(List<User> list, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
}
