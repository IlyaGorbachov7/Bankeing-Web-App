package by.epam.baranovsky.banking.dao;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.dao.factory.impl.SqlDAOFactory;
import by.epam.baranovsky.banking.entity.User;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.entity.criteria.SingularValue;
import org.apache.commons.lang.time.DateUtils;
import org.junit.jupiter.api.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SqlUserDaoTest extends SqlDBDaoTest{

    @Test
    @Order(0)
    void findByIdTest() throws DAOException {
        UserDAO dao = SqlDAOFactory.getInstance().getUserDAO();

        int existingId = 1;
        String expectedEmail="default@user.com";
        User user = dao.findEntityById(existingId);

        Assertions.assertEquals(expectedEmail, user.getEmail());
    }

    @Test
    @Order(1)
    void findAllTest() throws DAOException {
        UserDAO dao = SqlDAOFactory.getInstance().getUserDAO();
        int expectedSize = 2;

        List<User> allUsers = dao.findAll();

        Assertions.assertEquals(expectedSize, allUsers.size());
    }

    @Test
    @Order(2)
    void findByAndCriteriaTest() throws DAOException {
        UserDAO dao = SqlDAOFactory.getInstance().getUserDAO();
        int expectedSize = 1;

        Criteria<EntityParameters.UserParams> criteria = new Criteria<>(Criteria.SQL_AND);
        criteria.add(
                EntityParameters.UserParams.ROLE_ID,
                new SingularValue<>(DBMetadata.USER_ROLE_REGULAR));
        criteria.add(
                EntityParameters.UserParams.NAME,
                new SingularValue<>("Иван"));
        List<User> found = dao.findByCriteria(criteria);

        Assertions.assertEquals(expectedSize, found.size());

    }

    @Test
    @Order(3)
    void findByOrCriteriaTest() throws DAOException {
        UserDAO dao = SqlDAOFactory.getInstance().getUserDAO();
        int expectedSize = 2;

        Criteria<EntityParameters.UserParams> criteria = new Criteria<>(Criteria.SQL_OR);
        criteria.add(
                EntityParameters.UserParams.ROLE_ID,
                new SingularValue<>(DBMetadata.USER_ROLE_REGULAR));
        criteria.add(
                EntityParameters.UserParams.PATRONYMIC,
                new SingularValue<>("Иванович"));
        List<User> found = dao.findByCriteria(criteria);

        Assertions.assertEquals(expectedSize, found.size());
    }

    @Test
    @Order(4)
    void findByEmailTest() throws DAOException{
        UserDAO dao = SqlDAOFactory.getInstance().getUserDAO();
        String expectedEmail = "dead@insi.de";

        User user = dao.findByEmail(expectedEmail);

        Assertions.assertEquals(expectedEmail, user.getEmail());
    }

    @Test
    @Order(5)
    void createTest() throws DAOException {
        UserDAO dao = SqlDAOFactory.getInstance().getUserDAO();

        Date lastLogin = new java.sql.Date(new Date().getTime());
        lastLogin = DateUtils.truncate(lastLogin, Calendar.DAY_OF_MONTH);
        Date created = new java.sql.Date(new Date().getTime());
        created = DateUtils.truncate(created, Calendar.DAY_OF_MONTH);
        Date birthdate = new java.sql.Date(232502400000L);
        birthdate = DateUtils.truncate(birthdate, Calendar.DAY_OF_MONTH);

        User expected = new User();
        expected.setEmail("inserted@user.us");
        expected.setPassword("inserted");
        expected.setBirthDate(birthdate);
        expected.setLastName("Vstavlentsev");
        expected.setFirstName("Vasiliy");
        expected.setPatronymic(null);
        expected.setPassportSeries("IN");
        expected.setPassportNumber("5553535");
        expected.setRoleId(DBMetadata.USER_ROLE_REGULAR);
        expected.setRoleName("User");
        expected.setLastLogin(lastLogin);
        expected.setDateCreated(created);

        int id = dao.create(expected);
        expected.setId(id);
        User createdUser = dao.findEntityById(id);

        Assertions.assertEquals(expected, createdUser);
    }

    @Test
    @Order(6)
    void updateTest() throws DAOException {
        UserDAO dao = SqlDAOFactory.getInstance().getUserDAO();
        int existingId = 1;
        int expectedRowsAffected = 1;

        User userToUpdate = dao.findEntityById(existingId);
        userToUpdate.setPatronymic("Evstafjevich");

        int rowsAffected = dao.update(userToUpdate);

        Assertions.assertEquals(expectedRowsAffected, rowsAffected);
    }

    @Test
    @Order(7)
    void deleteTest() throws DAOException{
        UserDAO dao = SqlDAOFactory.getInstance().getUserDAO();
        String userEmail = "inserted@user.us";
        int expectedRowsAffected = 1;

        User toDelete = dao.findByEmail(userEmail);
        int rowsAffected = dao.delete(toDelete);

        Assertions.assertEquals(expectedRowsAffected, rowsAffected);
    }

    @Test
    void insertUserWithoutDataTest(){
        UserDAO dao = SqlDAOFactory.getInstance().getUserDAO();

        Assertions.assertThrows(NullPointerException.class, () -> {
            dao.create(new User());
        });
    }

    @Test
    void insertUserWithNotEnoughDataTest(){
        UserDAO dao = SqlDAOFactory.getInstance().getUserDAO();
        User user = new User();
        user.setBirthDate(new Date());

        Assertions.assertThrows(DAOException.class, () -> {
            dao.create(user);
        });
    }

}
