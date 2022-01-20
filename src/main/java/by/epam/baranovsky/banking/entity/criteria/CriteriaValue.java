package by.epam.baranovsky.banking.entity.criteria;


import java.util.List;

public interface CriteriaValue<T extends Comparable<T>> {

    Class<?> getType();

    List<Object> getAllValues();

}
