package by.epam.baranovsky.banking.dao;

import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.entity.BankingCard;
import by.epam.baranovsky.banking.service.exception.ServiceException;

import java.util.Date;
import java.util.List;

/**
 * Interface for BankingCard entity DAO.
 * @author Baranovsky E. K.
 * @version 1.0.0
 * @see BankingCard
 */
public interface BankCardDAO extends AbstractDAO<BankingCard> {

    /**
     * Retrieves BankingCards from data source by their number.
     * @param number Number of bank card
     * @return List of BankingCards with this number.
     * @throws DAOException
     */
    List<BankingCard> findByNumber(String number) throws DAOException;

    /**
     * Retrieves BankingCard from data source by its number and cvc code.
     * @param number Number of bank card
     * @param cvc Cvc code of bank card
     * @return Instance of BankingCard or {@code null} if not found.
     * @throws DAOException
     */
    BankingCard findByNumberAndCvc(String number, String cvc) throws DAOException;

    /**
     * Retrieves BankingCards from data source by their type id.
     * @param typeId BankingCard's type ID
     * @return List of BankingCards with this type ID.
     * @throws DAOException
     */
    List<BankingCard> findByType(Integer typeId) throws DAOException;

    /**
     * Retrieves BankingCards from data source by their user id.
     * @param userId BankingCard's user ID
     * @return List of BankingCards with this user.
     * @throws DAOException
     */
    List<BankingCard> findByUser(Integer userId) throws DAOException;

    /**
     * Retrieves BankingCards from data source by account they are tied to.
     * @param accountId BankingCard's account ID
     * @return List of BankingCards tied to account with this ID.
     * @throws DAOException
     */
    List<BankingCard> findByAccount(Integer accountId) throws DAOException;

}
