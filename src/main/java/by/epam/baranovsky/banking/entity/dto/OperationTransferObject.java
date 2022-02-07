package by.epam.baranovsky.banking.entity.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Data transfer object for Operation class.
 *
 * @see by.epam.baranovsky.banking.entity.Operation
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
@Data
public class OperationTransferObject implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Date date;
    private String accountNumber;
    private String targetAccountNumber;
    private String cardNumber;
    private String targetCardNumber;
    private Double value;
    private Double commission;
    private Integer bill;
    private Integer penalty;
    private Integer typeId;
    private Boolean isAccrual;

    public OperationTransferObject(){}
}