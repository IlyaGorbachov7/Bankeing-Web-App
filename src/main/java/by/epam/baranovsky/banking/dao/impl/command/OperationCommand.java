package by.epam.baranovsky.banking.dao.impl.command;

import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.entity.Operation;

import java.util.Objects;

public interface OperationCommand {

    int create(Operation operation) throws DAOException;

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
