package by.epam.baranovsky.banking.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.mindrot.jbcrypt.BCrypt;

import java.io.Serial;
import java.util.Date;

/**
 * Java bean that represents user of an application.
 *
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class User extends Entity {

    @Serial
    private static final long serialVersionUID = 1L;

    private String password;
    private String email;

    private Integer roleId;
    private String roleName;

    private String firstName;
    private String lastName;
    private String patronymic;

    private String passportSeries;
    private String passportNumber;

    private Date birthDate;
    private Date dateCreated;
    private Date lastLogin;

}