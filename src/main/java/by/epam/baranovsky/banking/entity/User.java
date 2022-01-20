package by.epam.baranovsky.banking.entity;

import lombok.Data;
import org.mindrot.jbcrypt.BCrypt;

import java.io.Serial;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Data
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

    private String bcrypt(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    public String getBcryptPassword(){
        return bcrypt(password);
    }
}