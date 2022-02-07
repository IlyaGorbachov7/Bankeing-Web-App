package by.epam.baranovsky.banking.service;

import by.epam.baranovsky.banking.entity.BankingCard;
import by.epam.baranovsky.banking.service.exception.ServiceException;
import by.epam.baranovsky.banking.service.exception.ValidationException;

import java.util.List;
/**
 * Interface for bank cards service.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public interface BankCardService {

    /**
     * Retrieves BankingCard from data source by its id.
     * @param id id of BankingCard
     * @return Instance of BankingCard or {@code null} if not found.
     * @throws ServiceException if DAOException occurs.
     */
    BankingCard findById(Integer id) throws ServiceException;

    /**
     * Retrieves BankingCard from data source by its number and cvc code.
     * @param number Number of bank card
     * @param cvc Cvc code of bank card
     * @return Instance of BankingCard or {@code null} if not found.
     * @throws ServiceException if DAOException occurs.
     */
    BankingCard findByNumberAndCvc(String number, String cvc) throws ServiceException;

    /**
     * Retrieves BankingCards from data source by their number.
     * @param number Number of bank card
     * @return List of BankingCards with this number.
     * @throws ServiceException if DAOException occurs.
     */
    List<BankingCard> findByNumber(String number) throws ServiceException;

    /**
     * Retrieves BankingCards from data source by their type id.
     * @param typeId BankingCard's type ID
     * @return List of BankingCards with this type ID.
     * @throws ServiceException if DAOException occurs.
     */
    List<BankingCard> findByType(Integer typeId) throws ServiceException;

    /**
     * Retrieves BankingCards from data source by their user id.
     * @param userId BankingCard's user ID
     * @return List of BankingCards with this user.
     * @throws ServiceException if DAOException occurs.
     */
    List<BankingCard> findByUser(Integer userId) throws ServiceException;

    /**
     * Retrieves BankingCards from data source by account they are tied to.
     * @param accountId BankingCard's account ID
     * @return List of BankingCards tied to account with this ID.
     * @throws ServiceException if DAOException occurs.
     */
    List<BankingCard> findByAccount(Integer accountId) throws ServiceException;

    /**
     * Updates BankingCard in data source.
     * @param card BankingCard to update
     * @return {@code true} if update is successful, {@code false} otherwise
     * @throws ServiceException if DAOException occurs.
     * @throws ValidationException if passed object did not pass validation.
     */
    boolean update(BankingCard card) throws ServiceException;

    /**
     * Saves BankingCard to data source.
     * @param card BankingCard to save
     * @return Created BankingCard if creation is successful, {@code null} otherwise
     * @throws ServiceException if DAOException occurs.
     * @throws ValidationException if passed object did not pass validation.
     */
    BankingCard create(BankingCard card) throws ServiceException;

    /**
     * Deletes BankingCard from data source.
     * @param id id of BankingCard to delete
     * @return {@code true} if removal is successful, {@code false} otherwise
     * @throws ServiceException if DAOException occurs.
     * @throws ValidationException if object with such id did not pass validation.
     */
    boolean delete(Integer id) throws ServiceException;

    /**
     * Deletes BankingCard from data source.
     * @param card BankingCard to delete
     * @return {@code true} if removal is successful, {@code false} otherwise
     * @throws ServiceException if DAOException occurs.
     * @throws ValidationException if passed object did not pass validation.
     */
    boolean delete(BankingCard card) throws ServiceException;

    /**
     * Retrieves all BankingCards from data source.
     * @return List of all BankingCards stored in data source.
     * @throws ServiceException if DAOException occurs.
     */
    List<BankingCard> findAll() throws ServiceException;

}
