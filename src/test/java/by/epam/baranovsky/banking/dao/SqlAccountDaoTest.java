package by.epam.baranovsky.banking.dao;

import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.dao.factory.impl.SqlDAOFactory;
import by.epam.baranovsky.banking.entity.Account;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SqlAccountDaoTest extends SqlDBDaoTest {

    @Override
    @Test
    @Order(0)
    void findByIdTest() throws DAOException {
        AccountDAO dao = SqlDAOFactory.getInstance().getAccountDAO();
        int existingId = 1;
        String expectedNumber = "BY000000000000000000";

        Account account = dao.findEntityById(existingId);

        assertEquals(expectedNumber, account.getAccountNumber());
    }

    @Override
    @Test
    @Order(1)
    void findAllTest() throws DAOException {
        AccountDAO dao = SqlDAOFactory.getInstance().getAccountDAO();
        int expectedSize=3;

        List<Account> accountList = dao.findAll();

        assertEquals(expectedSize, accountList.size());

    }

    @Test
    @Order(2)
    void findByNumberTest() throws DAOException{
        AccountDAO dao = SqlDAOFactory.getInstance().getAccountDAO();
        String number = "BY789009876543345611";
        int expectedId = 2;

        Account account = dao.findByNumber(number);

        assertEquals(expectedId, account.getId());
    }

    @Test
    @Order(3)
    void findByUserIdTest() throws DAOException{
        AccountDAO dao = SqlDAOFactory.getInstance().getAccountDAO();
        int userId = 2;
        int expectedSize = 2;

        List<Account> accounts = dao.findByUserId(userId);

        assertEquals(expectedSize, accounts.size());
    }

    @Test
    @Order(4)
    void findByStatusIdTest() throws DAOException{
        AccountDAO dao = SqlDAOFactory.getInstance().getAccountDAO();
        int statusId = 3;
        int expectedSize = 2;

        List<Account> accounts = dao.findByStatusId(statusId);

        assertEquals(expectedSize, accounts.size());
    }

    @Test
    @Order(5)
    void findUsersTest() throws DAOException{
        AccountDAO dao = SqlDAOFactory.getInstance().getAccountDAO();
        int accountId = 2;
        int expectedSize = 2;

        List<Integer> accounts = dao.findUsers(accountId);

        assertEquals(expectedSize, accounts.size());
    }

    @Override
    @Test
    @Order(6)
    void createTest() throws DAOException {
        AccountDAO dao = SqlDAOFactory.getInstance().getAccountDAO();
        Account toCreate = new Account();
        toCreate.setAccountNumber("PL123456789987654321");
        toCreate.setBalance(120d);
        toCreate.setYearlyInterestRate(2.4);
        toCreate.setStatusId(3);
        toCreate.setStatusName("Unlocked");
        toCreate.addUser(1);
        toCreate.addUser(2);

        int id = dao.create(toCreate);
        toCreate.setId(id);

        Account created = dao.findEntityById(id);
        created.setUsers(dao.findUsers(created.getId()));

        assertEquals(toCreate, created);
    }

    @Override
    @Test
    @Order(7)
    void updateTest() throws DAOException {
        AccountDAO dao = SqlDAOFactory.getInstance().getAccountDAO();
        int idToUpdate = 1;
        int expectedRowsAffected = -1;
        Account toUpdate = dao.findEntityById(idToUpdate);
        toUpdate.setBalance(toUpdate.getBalance()+100d);

        int rowsAffected = dao.update(toUpdate);

        assertEquals(expectedRowsAffected, rowsAffected);
    }

    @Override
    @Test
    @Order(8)
    void deleteTest() throws DAOException {
        AccountDAO dao = SqlDAOFactory.getInstance().getAccountDAO();
        String numberToDelete="PL123456789987654321";
        Account accountToDelete = dao.findByNumber(numberToDelete);
        int expectedRowsAffected = 1;

        int rowsAffected = dao.delete(accountToDelete);

        assertEquals(expectedRowsAffected, rowsAffected);
    }
}
