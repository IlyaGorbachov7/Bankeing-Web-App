package by.epam.baranovsky.banking.entity;

import lombok.Data;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class Account extends Entity{

    @Serial
    private static final long serialVersionUID = 1L;

    private List<Integer> users = new ArrayList<>();

    private String accountNumber;
    private Double balance;
    private Double yearlyInterestRate;

    private Integer statusId;
    private String statusName;

    public void addUser(Integer userId){
        if(!users.contains(userId)){
            users.add(userId);
        }
    }

    public void removeUser(Integer userId){
        users.remove(userId);
    }

}
