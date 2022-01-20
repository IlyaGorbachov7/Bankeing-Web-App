package by.epam.baranovsky.banking.dao.query;

import lombok.Data;

@Data
public class Query {

    private String query;
    private Object[] parameters;

    public Query(String query, Object... parameters) {
        this.query = query;
        this.parameters = parameters;
    }
}
