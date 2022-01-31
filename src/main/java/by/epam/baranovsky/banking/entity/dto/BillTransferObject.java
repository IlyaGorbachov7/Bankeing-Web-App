package by.epam.baranovsky.banking.entity.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BillTransferObject{

    private Integer id;
    private Double value;
    private Date issueDate;
    private Date dueDate;

    private Integer userId;
    private String userFullName;

    private Integer bearerId;
    private String bearerFullName;

    private Integer statusId;

    private Integer penaltyId;
    private Integer penaltyTypeId;
    private Double penaltyValue;

    private Integer loanId;

    private String notice;

}
