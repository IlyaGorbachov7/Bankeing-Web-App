package by.epam.baranovsky.banking.service;

import by.epam.baranovsky.banking.entity.Penalty;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import java.util.List;

public interface PenaltyService {

    List<Penalty> findByCriteria(Criteria<? extends EntityParameters.PenaltyParams> criteria) throws ServiceException;

    Penalty findById(Integer id) throws ServiceException;

    Integer update(Penalty penalty) throws ServiceException;

    Penalty create(Penalty penalty) throws ServiceException;

    Integer delete(Integer id) throws ServiceException;

    Integer delete(Penalty penalty) throws ServiceException;

    List<Penalty> findAll() throws ServiceException;

}
