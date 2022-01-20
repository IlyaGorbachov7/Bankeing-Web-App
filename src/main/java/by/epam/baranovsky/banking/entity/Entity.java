package by.epam.baranovsky.banking.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class Entity implements Serializable, Cloneable {

    protected Integer id = null;

    public Entity() {}

    public Entity(Integer id) {
        this.id = id;
    }
}
