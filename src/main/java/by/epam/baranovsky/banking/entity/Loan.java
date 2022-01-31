package by.epam.baranovsky.banking.entity;

import lombok.Data;

import java.io.Serial;
import java.util.Date;

@Data
public class Loan extends Entity {

    @Serial
    private static final long serialVersionUID = 1L;

    private Double singlePaymentValue;
    private Double startingValue;
    private Double totalPaymentValue;
    private Double yearlyInterestRate;
    private Date issueDate;
    private Date dueDate;
    private Integer userId;
    private Integer statusId;
    private String statusName;
    private Integer cardId;
    private Integer accountId;


}
