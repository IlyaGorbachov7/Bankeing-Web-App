package by.epam.baranovsky.banking.dao;

import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.entity.Penalty;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import java.util.List;

/**
 * Interface for Penalty entity DAO.
 * @author Baranovsky E. K.
 * @version 1.0.0
 * @see Penalty
 */
public interface PenaltyDAO extends AbstractDAO<Penalty> {

    /**
     * Retrieves Penalties from data source by criteria filled with parameters.
     * @param criteria Criteria to search by.
     * @return List of Penalties satisfying given criteria.
     * @throws DAOException
     */
    List<Penalty> findByCriteria(Criteria<? extends EntityParameters.PenaltyParams> criteria) throws DAOException;
}
