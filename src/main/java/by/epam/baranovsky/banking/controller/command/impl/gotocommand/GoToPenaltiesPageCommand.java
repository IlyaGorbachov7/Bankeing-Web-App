package by.epam.baranovsky.banking.controller.command.impl.gotocommand;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.controller.command.AbstractCommand;
import by.epam.baranovsky.banking.controller.constant.PageUrls;
import by.epam.baranovsky.banking.controller.constant.RequestAttributeNames;
import by.epam.baranovsky.banking.controller.constant.RequestParamName;
import by.epam.baranovsky.banking.controller.constant.SessionParamName;
import by.epam.baranovsky.banking.entity.Penalty;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.entity.criteria.SingularValue;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class GoToPenaltiesPageCommand extends AbstractCommand {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer currentUser = (Integer) request.getSession().getAttribute(SessionParamName.USER_ID);

        try{
            request.setAttribute(
                    RequestAttributeNames.USER_PENALTIES,
                    getUserPenalties(currentUser));
            request.getRequestDispatcher(PageUrls.PENALTIES_PAGE).forward(request,response);
        } catch (ServiceException e) {
            logger.error(e);
            request.getRequestDispatcher(PageUrls.ERROR_PAGE).forward(request,response);
        }
    }

    private List<Penalty> getUserPenalties(Integer userId) throws ServiceException {
        Criteria<EntityParameters.PenaltyParams> criteria = new Criteria<>();
        criteria.add(EntityParameters.PenaltyParams.USER, new SingularValue<>(userId));
        criteria.add(
                EntityParameters.PenaltyParams.STATUS_ID,
                new SingularValue<>(DBMetadata.PENALTY_STATUS_PENDING));

        return penaltyService.findByCriteria(criteria);
    }
}
