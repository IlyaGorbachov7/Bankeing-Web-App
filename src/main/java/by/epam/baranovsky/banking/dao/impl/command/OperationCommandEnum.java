package by.epam.baranovsky.banking.dao.impl.command;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.entity.Operation;

import java.util.Objects;

public enum OperationCommandEnum {
    ACC_LOCK(DBMetadata.OPERATION_TYPE_ACC_LOCK,new AccountLockCommand()),
    ACC_SUSP(DBMetadata.OPERATION_TYPE_ACC_SUSP,new AccountSuspendCommand()),
    ACC_UNLOCK(DBMetadata.OPERATION_TYPE_ACC_UNLOCK,new AccountUnlockCommand()),
    CARD_LOCK(DBMetadata.OPERATION_TYPE_CARD_LOCK, new CardLockCommand()),
    CARD_UNLOCK(DBMetadata.OPERATION_TYPE_CARD_UNLOCK, new CardUnlockCommand()),
    TRANSFER_ACC_TO_ACC(DBMetadata.OPERATION_TYPE_TRANSFER_A_A,new TransferCommand(TransferCommand.TransferTypes.ACC_TO_ACC)),
    TRANSFER_ACC_TO_CARD(DBMetadata.OPERATION_TYPE_TRANSFER_A_C,new TransferCommand(TransferCommand.TransferTypes.ACC_TO_CARD)),
    TRANSFER_CARD_TO_ACC(DBMetadata.OPERATION_TYPE_TRANSFER_C_A,new TransferCommand(TransferCommand.TransferTypes.CARD_TO_ACC)),
    TRANSFER_CARD_TO_CARD(DBMetadata.OPERATION_TYPE_TRANSFER_C_C,new TransferCommand(TransferCommand.TransferTypes.CARD_TO_CARD)),
    CARD_EXPIRE(DBMetadata.OPERATION_TYPE_CARD_EXPIRE, new CardExpirationCommand()),
    ACCRUAL(DBMetadata.OPERATION_TYPE_ACCRUAL, new TransferCommand(TransferCommand.TransferTypes.ACCRUAL));


    OperationCommandEnum(Integer id, OperationCommand command) {
        this.id = id;
        this.command=command;
    }

    private Integer id;
    private OperationCommand command;

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
