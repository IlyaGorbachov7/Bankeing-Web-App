package by.epam.baranovsky.banking.entity;

import lombok.Data;

import java.io.Serial;


@Data
public class Operation extends Entity {

    @Serial
    private static final long serialVersionUID = 1L;

    private Double value;
    private Integer typeId;
    private String typeName;
    private Integer accountId;
    private Integer targetAccountId;
    private Integer bankCardId;
    private Integer targetBankCardId;
    private Integer billId;
    private Integer penaltyId;

}
