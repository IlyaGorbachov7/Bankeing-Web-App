package by.epam.baranovsky.banking.entity.criteria;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class SingularValue<T extends Comparable<T>> implements CriteriaValue<T>{

    private T value;

    public SingularValue(T value) {
        this.value = value;
    }

    @Override
    public Class<?> getType() {
        return value.getClass();
    }

    @Override
    public List<Object> getAllValues() {
        List<Object> list = new ArrayList<>();
        list.add(value);
        return list;
    }
}
