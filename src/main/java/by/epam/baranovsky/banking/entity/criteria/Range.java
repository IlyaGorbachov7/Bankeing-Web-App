package by.epam.baranovsky.banking.entity.criteria;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Range<T extends Comparable<T>> implements CriteriaValue<T> {

    private T maxBorder;
    private T minBorder;

    public Range(T minBorder, T maxBorder) {

        if(maxBorder.compareTo(minBorder) < 0){
            T temp = minBorder;
            minBorder = maxBorder;
            maxBorder = temp;
        }

        this.maxBorder = maxBorder;
        this.minBorder = minBorder;
    }

    @Override
    public Class<?> getType() {
        return minBorder.getClass();
    }

    @Override
    public List<Object> getAllValues() {
        List<Object> list = new ArrayList<>();
        list.add(minBorder);
        list.add(maxBorder);

        return list;
    }

}
