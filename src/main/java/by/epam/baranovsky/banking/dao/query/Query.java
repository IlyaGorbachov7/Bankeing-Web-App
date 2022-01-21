package by.epam.baranovsky.banking.dao.query;

import lombok.Data;

@Data
public class Query {

    private String sqlQueryString;
    private Object[] parameters;

    public Query(String query, Object... parameters) {
        this.sqlQueryString = query;
        this.parameters = parameters;
    }
}
