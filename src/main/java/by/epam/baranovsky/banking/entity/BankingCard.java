package by.epam.baranovsky.banking.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;
import java.util.Date;

/**
 * Java bean that represents a banking card.
 *
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BankingCard extends Entity{

    @Serial
    private static final long serialVersionUID = 1L;

    private String number;
    private String cvc;
    private String pin;

    private Date expirationDate;
    private Date registrationDate;

    private Double balance;
    private Double overdraftMax;
    private Double overdraftInterestRate;

    private Integer userId;
    private Integer accountId;

    private Integer cardTypeId;
    private String cardTypeName;

    private Integer statusId;
    private String statusName;
}
