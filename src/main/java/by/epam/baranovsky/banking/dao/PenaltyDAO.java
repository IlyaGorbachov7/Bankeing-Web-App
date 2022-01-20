package by.epam.baranovsky.banking.dao;

import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.entity.Penalty;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;

import java.util.List;

public interface PenaltyDAO extends AbstractDAO<Penalty> {
    List<Penalty> findByCriteria(Criteria<? extends EntityParameters.PenaltyParams> criteria) throws DAOException;
}
