package by.epam.baranovsky.banking.entity;

import lombok.Data;

import java.io.Serial;
import java.util.Date;

@Data
public class Bill extends Entity {

    @Serial
    private static final long serialVersionUID = 1L;

    private Double value;
    private Date issueDate;
    private Date dueDate;

    private Integer userId;
    private Integer paymentAccountId;

    private Integer statusId;
    private String statusName;

    private Integer penaltyId;
    private Integer loanId;

}
