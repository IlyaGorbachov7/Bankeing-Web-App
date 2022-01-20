package by.epam.baranovsky.banking.dao;

import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.entity.Operation;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;

import java.util.List;

public interface OperationDAO extends AbstractDAO<Operation> {

    List<Operation> findByCriteria(Criteria<? extends EntityParameters.OperationParam> criteria) throws DAOException;

}
