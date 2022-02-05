package by.epam.baranovsky.banking.dao;

import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.dao.factory.impl.SqlDAOFactory;
import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.entity.Loan;
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
public class SqlLoansDaoTest extends SqlDBDaoTest{

    @Override
    @Test
    @Order(0)
    void findByIdTest() throws DAOException {
        LoanDAO dao = SqlDAOFactory.getInstance().getLoanDAO();
        int existingId=1;
        double expectedStartingValue = 500d;

        Loan retrieved = dao.findEntityById(existingId);

        assertEquals(expectedStartingValue, retrieved.getStartingValue());
    }


    @Override
    @Test
    @Order(1)
    void findAllTest() throws DAOException {
        LoanDAO dao = SqlDAOFactory.getInstance().getLoanDAO();
        int expectedSize = 3;

        List<Loan> retrieved = dao.findAll();

        assertEquals(expectedSize, retrieved.size());
    }

    @Test
    @Order(2)
    void findByCriteriaTest() throws DAOException{
        LoanDAO dao = SqlDAOFactory.getInstance().getLoanDAO();
        int expectedSize=2;
        Criteria<EntityParameters.LoanParams> criteria = new Criteria<>();
        criteria.add(EntityParameters.LoanParams.YEARLY_INTEREST, new Range<>(20,40));
        criteria.add(EntityParameters.LoanParams.STATUS, new SingularValue<>(2));

        List<Loan> retrieved = dao.findByCriteria(criteria);

        assertEquals(expectedSize, retrieved.size());
    }

    @Override
    @Test
    @Order(3)
    void createTest() throws DAOException {
        LoanDAO loanDao = SqlDAOFactory.getInstance().getLoanDAO();
        AccountDAO accountDao = SqlDAOFactory.getInstance().getAccountDAO();

        Calendar expiration = Calendar.getInstance();
        expiration.setTime(new Date());
        expiration.add(Calendar.YEAR, 1);

        double loanStartingValue = 500;
        double[] expectedBalances = {
                accountDao.findEntityById(1).getBalance()-loanStartingValue,
                accountDao.findEntityById(3).getBalance()+loanStartingValue};

        Loan toCreate = new Loan();
        toCreate.setStartingValue(loanStartingValue);
        toCreate.setUserId(2);
        toCreate.setAccountId(3);
        toCreate.setYearlyInterestRate(20d);
        toCreate.setIssueDate(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH));
        toCreate.setDueDate(DateUtils.truncate(expiration.getTime(), Calendar.DAY_OF_MONTH));
        toCreate.setTotalPaymentValue(loanStartingValue*1.2);
        toCreate.setSinglePaymentValue((loanStartingValue*1.2)/12);
        toCreate.setStatusId(2);
        toCreate.setStatusName("Pending");

        int id = loanDao.create(toCreate);
        toCreate.setId(id);
        Loan created = loanDao.findEntityById(id);
        Account bankAcc = accountDao.findEntityById(1);
        Account receiverAcc = accountDao.findEntityById(3);

        assertEquals(toCreate, created);
        assertEquals(expectedBalances[0], bankAcc.getBalance());
        assertEquals(expectedBalances[1], receiverAcc.getBalance());
    }

    @Override
    @Test
    @Order(4)
    void updateTest() throws DAOException {
        LoanDAO dao = SqlDAOFactory.getInstance().getLoanDAO();
        int expectedRowsAffected = 1;
        int existingId=2;

        Loan toUpdate = dao.findEntityById(existingId);
        toUpdate.setStatusId(1);
        int rowsAffected = dao.update(toUpdate);

        assertEquals(expectedRowsAffected, rowsAffected);
    }

    @Override
    @Test
    @Order(5)
    void deleteTest() throws DAOException {
        LoanDAO dao = SqlDAOFactory.getInstance().getLoanDAO();
        int expectedRowsAffected = 1;
        Criteria<EntityParameters.LoanParams> toDelete = new Criteria<>();
        toDelete.add(EntityParameters.LoanParams.USER, new SingularValue<>(2));
        toDelete.add(EntityParameters.LoanParams.YEARLY_INTEREST, new SingularValue<>(20d));
        toDelete.add(
                EntityParameters.LoanParams.ISSUE_DATE,
                new SingularValue<>(
                        DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH)));

        Loan loanToDelete = dao.findByCriteria(toDelete).get(0);

        int rowsAffected = dao.delete(loanToDelete);

        assertEquals(expectedRowsAffected, rowsAffected);

    }
}
