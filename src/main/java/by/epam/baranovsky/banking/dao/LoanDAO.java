package by.epam.baranovsky.banking.dao;

import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.entity.Loan;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import java.util.List;

/**
 * Interface for Loan entity DAO.
 * @author Baranovsky E. K.
 * @version 1.0.0
 * @see Loan
 */
public interface LoanDAO extends AbstractDAO<Loan> {

    /**
     * Retrieves Loans from data source by criteria filled with parameters.
     * @param criteria Criteria to search by.
     * @return List of Loans satisfying given criteria.
     * @throws DAOException
     */
    List<Loan> findByCriteria(Criteria<? extends EntityParameters.LoanParams> criteria) throws DAOException;

}
