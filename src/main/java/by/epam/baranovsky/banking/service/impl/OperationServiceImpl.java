package by.epam.baranovsky.banking.service.impl;

import by.epam.baranovsky.banking.dao.OperationDAO;
import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.dao.factory.impl.SqlDAOFactory;
import by.epam.baranovsky.banking.entity.Operation;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.service.OperationService;
import by.epam.baranovsky.banking.service.exception.ServiceException;
import by.epam.baranovsky.banking.service.exception.ValidationException;
import by.epam.baranovsky.banking.service.validator.OperationValidator;

import java.util.ArrayList;
import java.util.List;

public class OperationServiceImpl implements OperationService {

    private final OperationValidator validator = new OperationValidator();
    private final OperationDAO operationDAO = SqlDAOFactory.getInstance().getOperationDAO();

    @Override
    public List<Operation> findByCriteria(Criteria<? extends EntityParameters.OperationParam> criteria) throws ServiceException {
        List<Operation> operations = new ArrayList<>();
        try {
            operations = operationDAO.findByCriteria(criteria);
        } catch (DAOException e) {
            throw new ServiceException("Unable to retrieve operations from DB",e);
        }
        return operations;
    }

    @Override
    public Operation findById(Integer id) throws ServiceException {
        Operation operation;
        try{
            operation = operationDAO.findEntityById(id);
        } catch (DAOException e) {
            throw new ServiceException("Unable to retrieve operation from DB",e);
        }
        return operation;
    }

    @Override
    public Integer update(Operation operation) throws ServiceException {
        Integer result;
        try{
            if(!validator.validate(operation)){
                throw new ValidationException("Invalid input!");
            }
            if(operation.getId() == null || operationDAO.findEntityById(operation.getId()) == null){
                throw new ValidationException("No operation with such ID");
            }
            result = operationDAO.update(operation);
        } catch (DAOException e) {
            throw  new ServiceException("Unable to update operation in DB.",e);
        }
        return result;
    }

    @Override
    public Operation create(Operation operation) throws ServiceException {
        Operation result;
        try{
            if(!validator.validate(operation)){
                throw new ValidationException();
            }
            result = operationDAO.findEntityById(operationDAO.create(operation));
        } catch (DAOException e) {
            throw  new ServiceException("Unable to create operation in DB.",e);
        }
        return result;
    }

    @Override
    public Integer delete(Integer id) throws ServiceException {
        try {
            return delete(operationDAO.findEntityById(id));
        } catch (DAOException e) {
            throw  new ServiceException("Unable to delete operation from DB.",e);
        }
    }

    @Override
    public Integer delete(Operation operation) throws ServiceException {
        Integer res;
        try {
            if(!validator.validate(operation)){
                throw new ValidationException("Wrong input.");
            }
            res = operationDAO.delete(operation);
        } catch (DAOException e) {
            throw  new ServiceException("Unable to delete operation from DB.",e);
        }
        return res;
    }

    @Override
    public List<Operation> findAll() throws ServiceException {
        List<Operation> operations;
        try {
            operations = operationDAO.findAll();
        } catch (DAOException e) {
            throw  new ServiceException("Unable to retrieve penalties from DB.",e);
        }
        return operations;
    }
}
