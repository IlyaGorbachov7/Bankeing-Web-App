package by.epam.baranovsky.banking.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;
import java.util.Date;

/**
 * Java bean that represents a banking operation within a system.
 *
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
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
    private Date operationDate;

    private Double commission;

}
