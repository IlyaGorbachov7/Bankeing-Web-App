package by.epam.baranovsky.banking.entity.criteria;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementation of CriteriaValue.
 * Represents a singular value.
 * @param <T> Type of object that will be kept as value.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
@Data
public class SingularValue<T extends Comparable<T>> implements CriteriaValue<T>{

    private T value;

    public SingularValue(T value) {
        this.value = value;
    }

    @Override
    public List<Object> getAllValues() {
        List<Object> list = new ArrayList<>();
        list.add(value);
        return list;
    }

    /**
     * Generates SQL conditions by pattern (column_name=?).
     * Swaps java.util.Date for java.sql.Date so JDBC will work correctly.
     * @param name instance of EntityEnum, from which
     *              column name will be extracted.
     * @return An SQL condition with singular value.
     */
    @Override
    public String generateSqlCondition(EntityEnum name) {
        StringBuilder builder =new StringBuilder(" (");

        if(value instanceof Date){
            Date valueDate = (Date) value;
            value = (T) new java.sql.Date(valueDate.getTime());
        }

        builder.append(name.getColumn())
                .append("=?");
        return builder.append(") ").toString();
    }

}
