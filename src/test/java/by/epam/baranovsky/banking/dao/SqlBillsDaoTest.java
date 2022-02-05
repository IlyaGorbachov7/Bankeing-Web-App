package by.epam.baranovsky.banking.dao;

import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.dao.factory.impl.SqlDAOFactory;
import by.epam.baranovsky.banking.entity.Bill;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.entity.criteria.Range;
import by.epam.baranovsky.banking.entity.criteria.SingularValue;
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
public class SqlBillsDaoTest extends SqlDBDaoTest{

    @Override
    @Test
    @Order(0)
    void findByIdTest() throws DAOException {
        BillDAO dao = SqlDAOFactory.getInstance().getBillDAO();
        int existingId = 1;
        Double expectedValue = 500d;

        Bill found = dao.findEntityById(existingId);

        assertEquals(expectedValue, found.getValue());

    }

    @Override
    @Test
    @Order(1)
    void findAllTest() throws DAOException {
        BillDAO dao = SqlDAOFactory.getInstance().getBillDAO();
        int expectedSize = 3;

        List<Bill> allBills = dao.findAll();

        assertEquals(expectedSize, allBills.size());
    }

    @Test
    @Order(2)
    void findByCriteriaTest() throws DAOException{
        BillDAO dao = SqlDAOFactory.getInstance().getBillDAO();
        int expectedSize = 2;
        Criteria<EntityParameters.BillParam> criteria = new Criteria<>();
        criteria.add(EntityParameters.BillParam.VALUE, new Range<>(250d,600d));
        criteria.add(EntityParameters.BillParam.STATUS_ID, new SingularValue<>(1));

        List<Bill> found = dao.findByCriteria(criteria);

        assertEquals(expectedSize, found.size());
    }

    @Override
    @Test
    @Order(3)
    void createTest() throws DAOException {
        BillDAO dao = SqlDAOFactory.getInstance().getBillDAO();

        Bill toCreate = new Bill();
        toCreate.setIssueDate(DateUtils.truncate(new java.sql.Date(new Date().getTime()), Calendar.DAY_OF_MONTH));
        toCreate.setNotice("Test bill");
        toCreate.setStatusId(2);
        toCreate.setStatusName("closed");
        toCreate.setValue(250d);
        toCreate.setUserId(2);
        toCreate.setBearerId(1);
        toCreate.setPaymentAccountId(1);

        int id = dao.create(toCreate);
        toCreate.setId(id);
        Bill created = dao.findEntityById(id);

        assertEquals(toCreate, created);
    }

    @Override
    @Test
    @Order(4)
    void updateTest() throws DAOException {
        BillDAO dao = SqlDAOFactory.getInstance().getBillDAO();
        int idToUpdate = 3;
        int expectedRowsAffected = 1;

        Bill toUpdate = dao.findEntityById(idToUpdate);
        toUpdate.setStatusId(1);
        int rowsAffected = dao.update(toUpdate);

        assertEquals(expectedRowsAffected, rowsAffected);
    }

    @Override
    @Test
    @Order(5)
    void deleteTest() throws DAOException {
        BillDAO dao = SqlDAOFactory.getInstance().getBillDAO();
        int expectedRowsAffected = 1;
        String noticeToDelete = "Test bill";
        Criteria<EntityParameters.BillParam> criteria = new Criteria<>();
        criteria.add(EntityParameters.BillParam.NOTICE, new SingularValue<>(noticeToDelete));

        Bill bill = dao.findByCriteria(criteria).get(0);
        int rowsAffected = dao.delete(bill);

        assertEquals(expectedRowsAffected, rowsAffected);
    }

}
