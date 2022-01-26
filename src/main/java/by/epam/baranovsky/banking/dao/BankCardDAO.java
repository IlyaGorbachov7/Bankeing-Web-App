package by.epam.baranovsky.banking.dao;

import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.entity.BankingCard;

import java.util.List;

public interface BankCardDAO extends AbstractDAO<BankingCard> {

    BankingCard findByNumberAndCvc(String number, String cvc) throws DAOException;

    List<BankingCard> findByType(Integer typeId) throws DAOException;

    List<BankingCard> findByUser(Integer userId) throws DAOException;

    List<BankingCard> findByAccount(Integer accountId) throws DAOException;

}
