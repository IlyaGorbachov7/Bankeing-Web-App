package by.epam.baranovsky.banking.dao.impl.command;

import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.entity.Operation;

import java.util.Objects;

public enum OperationCommandEnum {
    ACC_LOCK(1,new AccountLockCommand()),
    ACC_SUSP(2,new AccountSuspendCommand()),
    ACC_UNLOCK(3,new AccountUnlockCommand()),
    CARD_LOCK(4, new CardLockCommand()),
    CARD_UNLOCK(5, new CardUnlockCommand()),
    TRANSFER_ACC_TO_ACC(6,new TransferCommand(TransferCommand.TransferTypes.ACC_TO_ACC)),
    TRANSFER_ACC_TO_CARD(7,new TransferCommand(TransferCommand.TransferTypes.ACC_TO_CARD)),
    TRANSFER_CARD_TO_ACC(8,new TransferCommand(TransferCommand.TransferTypes.CARD_TO_ACC)),
    TRANSFER_CARD_TO_CARD(9,new TransferCommand(TransferCommand.TransferTypes.CARD_TO_CARD)),
    CARD_EXPIRE(11, new CardExpirationCommand()),
    ACCRUAL(13, new TransferCommand(TransferCommand.TransferTypes.ACCRUAL));


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
