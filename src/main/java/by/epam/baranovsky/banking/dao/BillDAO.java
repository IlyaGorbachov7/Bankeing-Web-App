package by.epam.baranovsky.banking.dao;

import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.entity.Bill;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;

import java.util.List;

public interface BillDAO extends AbstractDAO<Bill> {

    List<Bill> findByCriteria(Criteria<? extends EntityParameters.BillParam> criteria) throws DAOException;

}
