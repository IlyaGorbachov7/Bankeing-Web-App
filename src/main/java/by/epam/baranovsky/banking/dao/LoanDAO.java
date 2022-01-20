package by.epam.baranovsky.banking.dao;

import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.entity.Loan;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;

import java.util.List;

public interface LoanDAO extends AbstractDAO<Loan> {

    List<Loan> findByCriteria(Criteria<? extends EntityParameters.LoanParams> criteria) throws DAOException;

}
