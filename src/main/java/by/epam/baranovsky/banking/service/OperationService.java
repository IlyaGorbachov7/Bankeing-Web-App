package by.epam.baranovsky.banking.service;

import by.epam.baranovsky.banking.entity.Operation;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import java.util.List;

public interface OperationService {

    List<Operation> findByCriteria(Criteria<? extends EntityParameters.OperationParam> criteria) throws ServiceException;

    Operation findById(Integer id) throws ServiceException;

    Integer update(Operation operation) throws ServiceException;

    Operation create(Operation operation) throws ServiceException;

    Integer delete(Integer id) throws ServiceException;

    Integer delete(Operation operation) throws ServiceException;

    List<Operation> findAll() throws ServiceException;

}
