package by.epam.baranovsky.banking.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;

/**
 * Java bean that represents a penalty (of many kinds) that is assigned to a user.
 *
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Penalty extends Entity {

    @Serial
    private static final long serialVersionUID = 1L;

    private Double value;
    private Integer paymentAccountId;

    private Integer typeId;
    private String typeName;

    private String notice;
    private Integer userId;

    private Integer statusId;
    private String statusName;

}
