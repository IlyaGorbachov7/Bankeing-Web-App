package by.epam.baranovsky.banking.service;

import by.epam.baranovsky.banking.entity.BankingCard;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import java.util.List;

public interface BankCardService {

    BankingCard findById(Integer id) throws ServiceException;

    BankingCard findByNumberAndCvc(String number, String cvc) throws ServiceException;

    List<BankingCard> findByNumber(String number) throws ServiceException;

    List<BankingCard> findByType(Integer typeId) throws ServiceException;

    List<BankingCard> findByUser(Integer userId) throws ServiceException;

    List<BankingCard> findByAccount(Integer accountId) throws ServiceException;

    Integer update(BankingCard card) throws ServiceException;

    BankingCard create(BankingCard card) throws ServiceException;

    Integer delete(Integer id) throws ServiceException;

    Integer delete(BankingCard card) throws ServiceException;

    List<BankingCard> findAll() throws ServiceException;

}
