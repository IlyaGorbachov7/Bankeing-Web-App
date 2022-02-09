package by.epam.baranovsky.banking.dao;

import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.entity.Operation;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import java.util.List;

/**
 * Interface for Operation entity DAO.
 * @author Baranovsky E. K.
 * @version 1.0.0
 * @see Operation
 */
public interface OperationDAO extends AbstractDAO<Operation> {

    /**
     * Retrieves Operations from data source by criteria filled with parameters.
     * @param criteria Criteria to search by.
     * @return List of Operations satisfying given criteria.
     * @throws DAOException
     */
    List<Operation> findByCriteria(Criteria<? extends EntityParameters.OperationParam> criteria) throws DAOException;

}
