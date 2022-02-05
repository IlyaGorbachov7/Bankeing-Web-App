package by.epam.baranovsky.banking.dao;

import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.dao.factory.impl.SqlDAOFactory;
import by.epam.baranovsky.banking.entity.Penalty;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.entity.criteria.SingularValue;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SqlPenaltiesDaoTest extends SqlDBDaoTest{

    @Override
    @Test
    @Order(0)
    void findByIdTest() throws DAOException {
        PenaltyDAO dao = SqlDAOFactory.getInstance().getPenaltyDAO();
        int existingId=1;
        int expectedType = 4;

        Penalty found = dao.findEntityById(existingId);

        assertEquals(expectedType, found.getTypeId());
    }

    @Test
    @Order(1)
    void findByCriteriaTest() throws DAOException{
        PenaltyDAO dao = SqlDAOFactory.getInstance().getPenaltyDAO();
        int expectedSize = 2;
        Criteria<EntityParameters.PenaltyParams> criteria = new Criteria<>();
        criteria.add(EntityParameters.PenaltyParams.TYPE_ID, new SingularValue<>(1));
        criteria.add(EntityParameters.PenaltyParams.TYPE_ID, new SingularValue<>(4));

        List<Penalty> found = dao.findByCriteria(criteria);

        assertEquals(expectedSize, found.size());
    }

    @Override
    @Test
    @Order(2)
    void findAllTest() throws DAOException {
        PenaltyDAO dao = SqlDAOFactory.getInstance().getPenaltyDAO();
        int expectedSize = 3;

        List<Penalty> found = dao.findAll();

        assertEquals(expectedSize, found.size());
    }

    @Override
    @Test
    @Order(3)
    void createTest() throws DAOException {
        PenaltyDAO dao = SqlDAOFactory.getInstance().getPenaltyDAO();
        Penalty toCreate = new Penalty();
        toCreate.setValue(200d);
        toCreate.setTypeId(4);
        toCreate.setStatusId(1);
        toCreate.setPaymentAccountId(1);
        toCreate.setUserId(2);
        toCreate.setNotice("test penalty");
        toCreate.setTypeName("Fee");
        toCreate.setStatusName("pending");

        int id = dao.create(toCreate);
        toCreate.setId(id);
        Penalty created = dao.findEntityById(id);

        assertEquals(toCreate, created);

    }

    @Override
    @Test
    @Order(4)
    void updateTest() throws DAOException {
        PenaltyDAO dao = SqlDAOFactory.getInstance().getPenaltyDAO();
        int expectedRowsAffected = 1;
        int updatedId = 1;

        Penalty toUpdate = dao.findEntityById(updatedId);
        toUpdate.setValue(666d);
        int rowsAffected = dao.update(toUpdate);

        assertEquals(expectedRowsAffected, rowsAffected);
    }

    @Override
    @Test
    @Order(5)
    void deleteTest() throws DAOException {
        PenaltyDAO dao = SqlDAOFactory.getInstance().getPenaltyDAO();
        String deletedNotice = "test penalty";
        int expectedRowsAffected = 1;
        Criteria<EntityParameters.PenaltyParams> criteria = new Criteria<>();
        criteria.add(EntityParameters.PenaltyParams.NOTICE, new SingularValue<>(deletedNotice));

        Penalty toDelete = dao.findByCriteria(criteria).get(0);

        int rowsAffected = dao.delete(toDelete);

        assertEquals(expectedRowsAffected, rowsAffected);
    }
}
