package by.epam.baranovsky.banking.entity.criteria;

import java.util.List;

/**
 * An interface that represents a certain type of parameter,
 * such as single value, range, likeness, etc.
 * @param <T> Type of object that will be kept as value.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public interface CriteriaValue<T extends Comparable<T>> {

    /**
     * Returns all values of this.
     * @return List of values.
     */
    List<Object> getAllValues();

    /**
     * Generates single sql-style condition.
     * @param name instance of EntityEnum, from which
     *              column name will be extracted.
     * @return SQL condition.
     */
    String generateSqlCondition(EntityEnum name);

}
