package by.epam.baranovsky.banking.entity.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Data transfer object for Bill class.
 *
 * @see by.epam.baranovsky.banking.entity.Bill
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
@Data
public class BillTransferObject implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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
