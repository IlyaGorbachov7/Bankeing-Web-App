package by.epam.baranovsky.banking.service;

import by.epam.baranovsky.banking.entity.Bill;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import java.util.List;

public interface BillService {

    List<Bill> findByCriteria(Criteria<? extends EntityParameters.BillParam> criteria) throws ServiceException;

    Bill findById(Integer id) throws ServiceException;

    Integer update(Bill bill) throws ServiceException;

    Bill create(Bill bill) throws ServiceException;

    Integer delete(Integer id) throws ServiceException;

    Integer delete(Bill bill) throws ServiceException;

    List<Bill> findAll() throws ServiceException;

}
