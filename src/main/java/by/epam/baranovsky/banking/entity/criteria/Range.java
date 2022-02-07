package by.epam.baranovsky.banking.entity.criteria;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementation of CriteriaValue.
 * Represents a range between two values.
 * @param <T> Type of object that will be kept as value.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
@Data
public class Range<T extends Comparable<T>> implements CriteriaValue<T> {

    /**
     * Upper range.
     */
    private T maxBorder;
    /**
     * Lower range.
     */
    private T minBorder;


    /**
     * Constructor for Range. If lower border is higher than upper,
     * swap sthem.
     * @param minBorder Lower range.
     * @param maxBorder Upper range.
     */
    public Range(T minBorder, T maxBorder) {

        if(maxBorder.compareTo(minBorder) < 0){
            T temp = minBorder;
            minBorder = maxBorder;
            maxBorder = temp;
        }

        this.maxBorder = maxBorder;
        this.minBorder = minBorder;
    }

    /**
     * Returns list of values of this Range, first lower border then upper border.
     * @return list of values of this range.
     */
    @Override
    public List<Object> getAllValues() {
        List<Object> list = new ArrayList<>();
        list.add(minBorder);
        list.add(maxBorder);

        return list;
    }

    /**
     * Generates SQL conditions by pattern (column_name>=? AND column_name<=?).
     * @param name instance of EntityEnum, from which
     *              column name will be extracted.
     * @return An SQL condition with a range of values.
     */
    @Override
    public String generateSqlCondition(EntityEnum name) {
        StringBuilder builder =new StringBuilder(" (");

        builder.append(name.getColumn())
                .append(">=?");
        builder.append(" AND ");
        builder.append(name.getColumn())
                .append("<=?");

        return builder.append(") ").toString();
    }
}
