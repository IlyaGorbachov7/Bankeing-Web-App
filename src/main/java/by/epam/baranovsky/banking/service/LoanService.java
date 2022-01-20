package by.epam.baranovsky.banking.service;

import by.epam.baranovsky.banking.entity.Loan;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import java.util.List;

public interface LoanService {

    List<Loan> findByCriteria(Criteria<? extends EntityParameters.LoanParams> criteria) throws ServiceException;

    Loan findById(Integer id) throws ServiceException;

    Integer update(Loan loan) throws ServiceException;

    Loan create(Loan loan) throws ServiceException;

    Integer delete(Integer id) throws ServiceException;

    Integer delete(Loan loan) throws ServiceException;

    List<Loan> findAll() throws ServiceException;

}
