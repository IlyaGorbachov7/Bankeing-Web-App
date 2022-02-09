package by.epam.baranovsky.banking.dao.impl.command;

import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.entity.Operation;

import java.util.Objects;

/**
 * Interface for commands that are responsible for creation of operations in SQL DB.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public interface OperationCommand {

    /**
     * Saves operation to DB and makes necessary changes in other DB.
     * @param operation Operation to save to DB.
     * @return ID of inserted operation.
     * @throws DAOException
     */
    int create(Operation operation) throws DAOException;


    /**
     * Checks if all passed objects are not null.
     * @param objects Objects to check
     * @throws DAOException if any of passed objects is null.
     */
    static void testNonNull(Object... objects) throws DAOException{
        try {
            for(Object obj : objects){
                Objects.requireNonNull(obj);
            }
        } catch (NullPointerException e){
            throw new DAOException("Operation data not full");
        }
    }

}
