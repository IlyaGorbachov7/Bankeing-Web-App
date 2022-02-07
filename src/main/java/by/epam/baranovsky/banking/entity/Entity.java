package by.epam.baranovsky.banking.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * An abstract class that represents an entity with database ID.
 *
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
@Data
public abstract class Entity implements Serializable {

    protected Integer id = null;

    protected Entity() {}

}
