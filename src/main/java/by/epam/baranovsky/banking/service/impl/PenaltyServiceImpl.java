package by.epam.baranovsky.banking.service.impl;

import by.epam.baranovsky.banking.dao.PenaltyDAO;
import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.dao.factory.impl.SqlDAOFactory;
import by.epam.baranovsky.banking.entity.Penalty;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.service.PenaltyService;
import by.epam.baranovsky.banking.service.exception.ServiceException;
import by.epam.baranovsky.banking.service.exception.ValidationException;
import by.epam.baranovsky.banking.service.validator.PenaltyValidator;

import java.util.ArrayList;
import java.util.List;

public class PenaltyServiceImpl implements PenaltyService {

    private final PenaltyValidator validator = new PenaltyValidator();
    private final PenaltyDAO penaltyDAO = SqlDAOFactory.getInstance().getPenaltyDAO();

    @Override
    public List<Penalty> findByCriteria(Criteria<? extends EntityParameters.PenaltyParams> criteria) throws ServiceException {
        List<Penalty> penalties = new ArrayList<>();
        try {
            penalties = penaltyDAO.findByCriteria(criteria);
        } catch (DAOException e) {
            throw new ServiceException("Unable to retrieve penalties from DB",e);
        }
        return penalties;
    }

    @Override
    public Penalty findById(Integer id) throws ServiceException {
        Penalty penalty;
        try{
            penalty = penaltyDAO.findEntityById(id);
        } catch (DAOException e) {
            throw new ServiceException("Unable to retrieve penalty from DB",e);
        }
        return penalty;
    }

    @Override
    public boolean update(Penalty penalty) throws ServiceException {
        Integer result;
        try{
            if(!validator.validate(penalty)){
                throw new ValidationException("Invalid input!");
            }
            if(penalty.getId() == null || penaltyDAO.findEntityById(penalty.getId()) == null){
                throw new ValidationException("No penalty with such ID");
            }
            result = penaltyDAO.update(penalty);
        } catch (DAOException e) {
            throw  new ServiceException("Unable to update penalty in DB.",e);
        }
        return result>0;
    }

    @Override
    public Penalty create(Penalty penalty) throws ServiceException {
        Penalty result;
        try{
            if(!validator.validate(penalty)){
                throw new ValidationException("Invalid input!");
            }
            result = penaltyDAO.findEntityById(penaltyDAO.create(penalty));
        } catch (DAOException e) {
            throw  new ServiceException("Unable to create penalty in DB.",e);
        }
        return result;
    }

    @Override
    public boolean delete(Integer id) throws ServiceException {
        try {
            return delete(penaltyDAO.findEntityById(id));
        } catch (DAOException e) {
            throw  new ServiceException("Unable to delete penalty from DB.",e);
        }
    }

    @Override
    public boolean delete(Penalty penalty) throws ServiceException {
        Integer res;
        try {
            if(!validator.validate(penalty)){
                throw new ValidationException("Wrong input.");
            }
            res = penaltyDAO.delete(penalty);
        } catch (DAOException e) {
            throw  new ServiceException("Unable to delete penalty from DB.",e);
        }
        return res>0;
    }

    @Override
    public List<Penalty> findAll() throws ServiceException {
        List<Penalty> loans;
        try {
            loans = penaltyDAO.findAll();
        } catch (DAOException e) {
            throw  new ServiceException("Unable to retrieve penalties from DB.",e);
        }
        return loans;
    }
}
