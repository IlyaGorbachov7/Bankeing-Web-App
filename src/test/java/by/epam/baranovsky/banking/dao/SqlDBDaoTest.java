package by.epam.baranovsky.banking.dao;

import by.epam.baranovsky.banking.dao.connectionpool.ConnectionPool;
import by.epam.baranovsky.banking.dao.connectionpool.ConnectionPoolException;
import by.epam.baranovsky.banking.dao.exception.DAOException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public abstract class SqlDBDaoTest {

    @BeforeAll
    static void initPool() throws ConnectionPoolException {
        ConnectionPool.getInstance();
    }

    @AfterAll
    static void disposePool() throws ConnectionPoolException{
        ConnectionPool.getInstance().dispose();
    }

    abstract void findByIdTest() throws DAOException;

    abstract void findAllTest() throws DAOException;

    abstract void createTest() throws DAOException;

    abstract void updateTest() throws DAOException;

    abstract void deleteTest() throws DAOException;

}
