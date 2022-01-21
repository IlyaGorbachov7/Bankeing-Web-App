package by.epam.baranovsky.banking.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class Entity implements Serializable {

    protected Integer id = null;

    protected Entity() {}

    protected Entity(Integer id) {
        this.id = id;
    }
}
