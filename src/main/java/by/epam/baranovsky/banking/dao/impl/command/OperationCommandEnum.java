package by.epam.baranovsky.banking.dao.impl.command;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.entity.Operation;

import java.util.Objects;

/**
 * Enumeration that stores instances of OperationCommand.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public enum OperationCommandEnum {
    ACC_LOCK(DBMetadata.OPERATION_TYPE_ACC_LOCK,new AccountLockCommand()),
    ACC_SUSP(DBMetadata.OPERATION_TYPE_ACC_SUSP,new AccountSuspendCommand()),
    ACC_UNLOCK(DBMetadata.OPERATION_TYPE_ACC_UNLOCK,new AccountUnlockCommand()),
    CARD_LOCK(DBMetadata.OPERATION_TYPE_CARD_LOCK, new CardLockCommand()),
    CARD_UNLOCK(DBMetadata.OPERATION_TYPE_CARD_UNLOCK, new CardUnlockCommand()),
    TRANSFER_ACC_TO_ACC(DBMetadata.OPERATION_TYPE_TRANSFER_A_A,new TransferAccAccCommand()),
    TRANSFER_ACC_TO_CARD(DBMetadata.OPERATION_TYPE_TRANSFER_A_C,new TransferAccCardCommand()),
    TRANSFER_CARD_TO_ACC(DBMetadata.OPERATION_TYPE_TRANSFER_C_A,new TransferCardAccCommand()),
    TRANSFER_CARD_TO_CARD(DBMetadata.OPERATION_TYPE_TRANSFER_C_C,new TransferCardCardCommand()),
    CARD_EXPIRE(DBMetadata.OPERATION_TYPE_CARD_EXPIRE, new CardExpirationCommand()),
    ACCRUAL(DBMetadata.OPERATION_TYPE_ACCRUAL, new TransferAccrualCommand());

    /** Type ID of an operation*/
    private final Integer id;
    /** Command associated with this type ID*/
    private final OperationCommand command;


    OperationCommandEnum(Integer id, OperationCommand command) {
        this.id = id;
        this.command=command;
    }

    /**
     * Retrieves operation creation command
     * from this enumeration by typeId of a passed operation.
     * @param operation Operation to find command for.
     * @return Instance of OperationCommand matching operation's type.
     * If no command is found or operation is {@code null},
     * returns anonymous OperationCommand that throws DAOException.
     */
    public static OperationCommand getCommand(Operation operation) {
        OperationCommand wrongCommand = o -> {
            throw new DAOException("No such operation permitted!");
        };
        if(operation == null){
            return wrongCommand;
        }
        for(OperationCommandEnum commandEnum : OperationCommandEnum.values()){
            if(Objects.equals(commandEnum.id, operation.getTypeId())){
                return commandEnum.command;
            }
        }

        return wrongCommand;
    }

}
