package by.epam.baranovsky.banking.dao;


import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.dao.factory.impl.SqlDAOFactory;
import by.epam.baranovsky.banking.entity.BankingCard;
import org.apache.commons.lang.time.DateUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SqlBankCardDaoTest extends SqlDBDaoTest {

    @Override
    @Test
    @Order(0)
    void findByIdTest() throws DAOException {
        BankCardDAO dao = SqlDAOFactory.getInstance().getBankCardDAO();
        int existingId = 1;
        String expectedNumber = "0000111122223333";

        BankingCard retrieved = dao.findEntityById(existingId);

        assertEquals(expectedNumber, retrieved.getNumber());
    }

    @Override
    @Test
    @Order(1)
    void findAllTest() throws DAOException {
        BankCardDAO dao = SqlDAOFactory.getInstance().getBankCardDAO();
        int expectedSize = 5;

        List<BankingCard> retrieved = dao.findAll();

        assertEquals(expectedSize, retrieved.size());
    }

    @Test
    @Order(2)
    void findByNumberTest() throws DAOException {
        BankCardDAO dao = SqlDAOFactory.getInstance().getBankCardDAO();
        String existingNumber = "4444555566667777";
        int expectedSize = 2;

        List<BankingCard> retrieved = dao.findByNumber(existingNumber);

        assertEquals(expectedSize, retrieved.size());
    }

    @Test
    @Order(3)
    void findByNumberAndCvcTest() throws DAOException {
        BankCardDAO dao = SqlDAOFactory.getInstance().getBankCardDAO();
        String[] existingNumCvc = {"4444555566667777", "666"};
        int expectedId = 2;

        BankingCard retrieved = dao.findByNumberAndCvc(existingNumCvc[0], existingNumCvc[1]);

        assertEquals(expectedId, retrieved.getId());
    }

    @Test
    @Order(4)
    void findByTypeTest() throws DAOException {
        BankCardDAO dao = SqlDAOFactory.getInstance().getBankCardDAO();
        int existingTypeId=1;
        int expectedSize = 3;

        List<BankingCard> retrieved = dao.findByType(existingTypeId);

        assertEquals(expectedSize, retrieved.size());
    }

    @Test
    @Order(5)
    void findByUserTest() throws DAOException {
        BankCardDAO dao = SqlDAOFactory.getInstance().getBankCardDAO();
        int existingUserId=2;
        int expectedSize = 3;

        List<BankingCard> retrieved = dao.findByUser(existingUserId);

        assertEquals(expectedSize, retrieved.size());
    }

    @Test
    @Order(6)
    void findByAccountTest() throws DAOException {
        BankCardDAO dao = SqlDAOFactory.getInstance().getBankCardDAO();
        int existingAccountId=1;
        int expectedSize = 2;

        List<BankingCard> retrieved = dao.findByAccount(existingAccountId);

        assertEquals(expectedSize, retrieved.size());
    }

    @Override
    @Test
    @Order(7)
    void createTest() throws DAOException {
        BankCardDAO dao = SqlDAOFactory.getInstance().getBankCardDAO();

        Calendar expiration = Calendar.getInstance();
        expiration.setTime(new Date());
        expiration.add(Calendar.YEAR, 4);

        BankingCard toCreate = new BankingCard();
        toCreate.setNumber("4321123456788765");
        toCreate.setCvc("333");
        toCreate.setPin("8080");
        toCreate.setCardTypeId(1);
        toCreate.setCardTypeName("Debit card");
        toCreate.setStatusId(1);
        toCreate.setStatusName("unlocked");
        toCreate.setRegistrationDate(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH));
        toCreate.setExpirationDate(DateUtils.truncate(expiration.getTime(), Calendar.DAY_OF_MONTH));
        toCreate.setUserId(1);
        toCreate.setAccountId(3);

        int id = dao.create(toCreate);
        toCreate.setId(id);
        BankingCard created = dao.findEntityById(id);

        assertEquals(toCreate, created);
    }

    @Override
    @Test
    @Order(8)
    void updateTest() throws DAOException {
        BankCardDAO dao = SqlDAOFactory.getInstance().getBankCardDAO();
        int idToUpdate=3;
        int expectedRowsAffected = 1;

        BankingCard toUpdate = dao.findEntityById(idToUpdate);
        toUpdate.setPin("5678");
        int rowsAffected = dao.update(toUpdate);

        assertEquals(expectedRowsAffected, rowsAffected);

    }

    @Override
    @Test
    @Order(9)
    void deleteTest() throws DAOException {
        BankCardDAO dao = SqlDAOFactory.getInstance().getBankCardDAO();
        String[] numCvcToDelete = {"4321123456788765","333"};
        BankingCard toDelete = dao.findByNumberAndCvc(numCvcToDelete[0], numCvcToDelete[1]);
        int expectedRowsAffected = 1;

        int rowsAffected = dao.delete(toDelete);

        assertEquals(expectedRowsAffected,rowsAffected);
    }
}
