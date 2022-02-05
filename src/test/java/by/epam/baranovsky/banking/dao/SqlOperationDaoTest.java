package by.epam.baranovsky.banking.dao;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.dao.factory.impl.SqlDAOFactory;
import by.epam.baranovsky.banking.entity.Account;
import by.epam.baranovsky.banking.entity.BankingCard;
import by.epam.baranovsky.banking.entity.Operation;
import by.epam.baranovsky.banking.entity.criteria.Criteria;
import by.epam.baranovsky.banking.entity.criteria.EntityParameters;
import by.epam.baranovsky.banking.entity.criteria.SingularValue;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SqlOperationDaoTest extends SqlDBDaoTest{

    @Override
    @Test
    @Order(0)
    void findByIdTest() throws DAOException {
        OperationDAO dao = SqlDAOFactory.getInstance().getOperationDAO();
        int existingId=1;
        int expectedType=6;

        Operation retrieved = dao.findEntityById(existingId);

        assertEquals(expectedType, retrieved.getTypeId());
    }

    @Override
    @Test
    @Order(1)
    void findAllTest() throws DAOException {
        OperationDAO dao = SqlDAOFactory.getInstance().getOperationDAO();
        int expectedSize = 2;

        List<Operation> retrieved = dao.findAll();

        assertEquals(expectedSize, retrieved.size());
    }

    @Test
    @Order(2)
    void findByCriteriaTest() throws DAOException{
        OperationDAO dao = SqlDAOFactory.getInstance().getOperationDAO();
        int expectedSize = 1;
        Criteria<EntityParameters.OperationParam> criteria = new Criteria<>();
        criteria.add(EntityParameters.OperationParam.TYPE_ID, new SingularValue<>(3));
        criteria.add(EntityParameters.OperationParam.ACCOUNT, new SingularValue<>(3));

        List<Operation> retrieved = dao.findByCriteria(criteria);

        assertEquals(expectedSize, retrieved.size());
    }


    @Override
    @Test
    @Order(3)
    void updateTest() throws DAOException {
        OperationDAO dao = SqlDAOFactory.getInstance().getOperationDAO();
        int idToUpdate = 1;
        int expectedRowsAffected = 1;

        Operation toUpdate = dao.findEntityById(idToUpdate);
        toUpdate.setValue(toUpdate.getValue()+1);
        int rowsAffected = dao.update(toUpdate);

        assertEquals(expectedRowsAffected, rowsAffected);
    }


    @Override
    @Test
    void createTest() throws DAOException {
        assertTrue(true);
    }

    @Override
    @Test
    void deleteTest() throws DAOException {
        assertTrue(true);
    }

    @Nested
    @Order(10)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class OperationTransactionTests{
        static int idOperation;
        static int idForDeleteTest;
        static Account acc;
        static Account targetAcc;
        static BankingCard card;
        static BankingCard targetCard;

        @BeforeEach
        void setUpData() throws DAOException {
            AccountDAO accountDAO = SqlDAOFactory.getInstance().getAccountDAO();
            BankCardDAO cardDAO = SqlDAOFactory.getInstance().getBankCardDAO();
            OperationDAO operationDAO = SqlDAOFactory.getInstance().getOperationDAO();

            Account account = new Account();
            account.setUsers(List.of(1));
            account.setStatusId(DBMetadata.ACCOUNT_STATUS_UNLOCKED);
            account.setBalance(1000d);
            account.setYearlyInterestRate(0d);
            account.setAccountNumber("TE909090909090909090");
            account.setId(accountDAO.create(account));
            acc = account;

            Account target = new Account();
            target.setUsers(List.of(2));
            target.setStatusId(DBMetadata.ACCOUNT_STATUS_UNLOCKED);
            target.setBalance(0d);
            target.setYearlyInterestRate(0d);
            target.setAccountNumber("TE890098890098890098");
            target.setId(accountDAO.create(target));
            targetAcc = target;


            BankingCard ownCard = new BankingCard();
            ownCard.setPin("1234");
            ownCard.setNumber("1029384756102938");
            ownCard.setCvc("019");
            ownCard.setUserId(1);
            ownCard.setStatusId(1);
            ownCard.setCardTypeId(1);
            ownCard.setAccountId(acc.getId());
            ownCard.setRegistrationDate(new Date());
            ownCard.setExpirationDate(new Date(ownCard.getRegistrationDate().getTime()+126227808000L));
            ownCard.setId(cardDAO.create(ownCard));
            card = ownCard;

            BankingCard operationTargetCard = new BankingCard();
            operationTargetCard.setPin("4321");
            operationTargetCard.setNumber("1358805981378067");
            operationTargetCard.setCvc("111");
            operationTargetCard.setUserId(2);
            operationTargetCard.setStatusId(1);
            operationTargetCard.setCardTypeId(1);
            operationTargetCard.setAccountId(targetAcc.getId());
            operationTargetCard.setRegistrationDate(new Date());
            operationTargetCard.setExpirationDate(new Date(operationTargetCard.getRegistrationDate().getTime()+126227808000L));
            operationTargetCard.setId(cardDAO.create(operationTargetCard));
            targetCard = operationTargetCard;

            Operation toDelete = new Operation();
            toDelete.setTypeId(3);
            toDelete.setAccountId(3);
            idForDeleteTest = operationDAO.create(toDelete);
        }

        @AfterEach
        void deleteData() throws DAOException {
            AccountDAO accountDAO = SqlDAOFactory.getInstance().getAccountDAO();
            BankCardDAO cardDAO = SqlDAOFactory.getInstance().getBankCardDAO();
            OperationDAO operationDAO = SqlDAOFactory.getInstance().getOperationDAO();

            accountDAO.delete(acc.getId());
            accountDAO.delete(targetAcc.getId());
            cardDAO.delete(card.getId());
            cardDAO.delete(card.getId());
            operationDAO.delete(idOperation);
            operationDAO.delete(idForDeleteTest);
        }

        @Test
        @Order(0)
        void createAccountLockOperationTest() throws DAOException {
            OperationDAO operationDAO = SqlDAOFactory.getInstance().getOperationDAO();
            AccountDAO accountDAO = SqlDAOFactory.getInstance().getAccountDAO();
            int expectedAccStatus = DBMetadata.ACCOUNT_STATUS_BLOCKED;

            Operation toCreate = new Operation();
            toCreate.setTypeId(1);
            toCreate.setAccountId(acc.getId());
            toCreate.setTypeName("Account block");
            idOperation = operationDAO.create(toCreate);
            toCreate.setId(idOperation);

            Operation created = operationDAO.findEntityById(idOperation);
            toCreate.setOperationDate(created.getOperationDate());
            Account modifiedAcc = accountDAO.findEntityById(acc.getId());

            assertEquals(expectedAccStatus, modifiedAcc.getStatusId());
            assertEquals(toCreate, created);
        }

        @Test
        @Order(1)
        void createAccAccTransactionTest() throws DAOException {
            OperationDAO dao = SqlDAOFactory.getInstance().getOperationDAO();
            AccountDAO accountDAO = SqlDAOFactory.getInstance().getAccountDAO();
            double value = 200d;
            double expectedReceiverBalance = targetAcc.getBalance()+value;
            double expectedSenderBalance = acc.getBalance()-value;

            Operation transaction = new Operation();
            transaction.setTypeId(6);
            transaction.setValue(value);
            transaction.setAccountId(acc.getId());
            transaction.setTargetAccountId(targetAcc.getId());
            transaction.setTypeName("Transfer account/account");

            idOperation = dao.create(transaction);
            Operation created = dao.findEntityById(idOperation);

            transaction.setOperationDate(created.getOperationDate());
            transaction.setId(idOperation);

            Account sender = accountDAO.findEntityById(acc.getId());
            Account receiver = accountDAO.findEntityById(targetAcc.getId());

            assertEquals(transaction, created);
            assertEquals(expectedReceiverBalance, receiver.getBalance());
            assertEquals(expectedSenderBalance, sender.getBalance());
        }

        @Test
        @Order(2)
        void createAccCardTransactionTest() throws DAOException {
            OperationDAO dao = SqlDAOFactory.getInstance().getOperationDAO();
            AccountDAO accountDAO = SqlDAOFactory.getInstance().getAccountDAO();

            double value = 300d;
            double expectedSenderBalance = acc.getBalance()-value;
            double expectedReceiverBalance = targetAcc.getBalance()+value;

            Operation transaction = new Operation();
            transaction.setTypeId(7);
            transaction.setValue(value);
            transaction.setAccountId(acc.getId());
            transaction.setTargetBankCardId(targetCard.getId());
            transaction.setTypeName("Transfer account/card");

            idOperation = dao.create(transaction);
            Operation created = dao.findEntityById(idOperation);

            transaction.setOperationDate(created.getOperationDate());
            transaction.setId(idOperation);

            Account sender = accountDAO.findEntityById(acc.getId());
            Account receiver = accountDAO.findEntityById(targetAcc.getId());

            assertEquals(transaction, created);
            assertEquals(expectedReceiverBalance, receiver.getBalance());
            assertEquals(expectedSenderBalance, sender.getBalance());

        }

        @Test
        @Order(3)
        void createCardAccTransactionTest() throws DAOException {
            OperationDAO dao = SqlDAOFactory.getInstance().getOperationDAO();
            AccountDAO accountDAO = SqlDAOFactory.getInstance().getAccountDAO();

            double value = 500d;
            double expectedSenderBalance = acc.getBalance()-value;
            double expectedReceiverBalance = targetAcc.getBalance()+value;

            Operation transaction = new Operation();
            transaction.setTypeId(8);
            transaction.setValue(value);
            transaction.setBankCardId(card.getId());
            transaction.setTargetAccountId(targetAcc.getId());
            transaction.setTypeName("Transfer card/account");

            idOperation = dao.create(transaction);
            Operation created = dao.findEntityById(idOperation);

            transaction.setOperationDate(created.getOperationDate());
            transaction.setId(idOperation);

            Account sender = accountDAO.findEntityById(acc.getId());
            Account receiver = accountDAO.findEntityById(targetAcc.getId());

            assertEquals(transaction, created);
            assertEquals(expectedReceiverBalance, receiver.getBalance());
            assertEquals(expectedSenderBalance, sender.getBalance());
        }

        @Test
        @Order(4)
        void createCardCardTransactionTest() throws DAOException {
            OperationDAO dao = SqlDAOFactory.getInstance().getOperationDAO();
            AccountDAO accountDAO = SqlDAOFactory.getInstance().getAccountDAO();

            double value = 700d;
            double expectedSenderBalance = acc.getBalance()-value;
            double expectedReceiverBalance = targetAcc.getBalance()+value;

            Operation transaction = new Operation();
            transaction.setTypeId(9);
            transaction.setValue(value);
            transaction.setBankCardId(card.getId());
            transaction.setTargetBankCardId(targetCard.getId());
            transaction.setTypeName("Transfer card/card");

            idOperation = dao.create(transaction);
            Operation created = dao.findEntityById(idOperation);

            transaction.setOperationDate(created.getOperationDate());
            transaction.setId(idOperation);

            Account sender = accountDAO.findEntityById(acc.getId());
            Account receiver = accountDAO.findEntityById(targetAcc.getId());

            assertEquals(transaction, created);
            assertEquals(expectedReceiverBalance, receiver.getBalance());
            assertEquals(expectedSenderBalance, sender.getBalance());
        }

        @Test
        @Order(5)
        void createCardLockTest() throws DAOException {
            OperationDAO dao = SqlDAOFactory.getInstance().getOperationDAO();
            BankCardDAO cardDAO = SqlDAOFactory.getInstance().getBankCardDAO();
            int expectedStatus = DBMetadata.CARD_STATUS_LOCKED;

            Operation lock = new Operation();
            lock.setTypeId(4);
            lock.setTypeName("Card lock");
            lock.setBankCardId(card.getId());
            idOperation = dao.create(lock);

            Operation created = dao.findEntityById(idOperation);

            lock.setOperationDate(created.getOperationDate());
            lock.setId(idOperation);

            BankingCard modifiedCard = cardDAO.findEntityById(card.getId());

            assertEquals(expectedStatus, modifiedCard.getStatusId());
            assertEquals(lock, created);
        }

        @Test
        @Order(6)
        void deleteOperationTest() throws DAOException {
            OperationDAO dao = SqlDAOFactory.getInstance().getOperationDAO();
            int expectedRowsAffected = 1;

            int rowsAffected = dao.delete(idForDeleteTest);

            assertEquals(expectedRowsAffected, rowsAffected);
        }

    }
}
