package by.epam.baranovsky.banking.dao.query;

import lombok.Data;

/**
 * A class that is used to conveniently transfer prepared SQL statement with parameters.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
@Data
public class Query {

    /**
     * SQL statement (should be prepared statement).
     */
    private String sqlQueryString;
    /**
     * Parameters that are to be set in prepared statement in order.
     */
    private Object[] parameters;

    public Query(String query, Object... parameters) {
        this.sqlQueryString = query;
        this.parameters = parameters;
    }
}
