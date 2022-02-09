package by.epam.baranovsky.banking.dao;

import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.entity.Bill;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import java.util.List;

/**
 * Interface for Bill entity DAO.
 * @author Baranovsky E. K.
 * @version 1.0.0
 * @see Bill
 */
public interface BillDAO extends AbstractDAO<Bill> {

    /**
     * Retrieves Bills from data source by criteria filled with parameters.
     * @param criteria Criteria to search by.
     * @return List of Bills satisfying given criteria.
     * @throws DAOException
     */
    List<Bill> findByCriteria(Criteria<? extends EntityParameters.BillParam> criteria) throws DAOException;

}
