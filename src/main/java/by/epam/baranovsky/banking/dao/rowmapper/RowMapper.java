package by.epam.baranovsky.banking.dao.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interface for row mapper,
 * which is used to build entities based on data from ResultSet.
 *
 * @param <T> Type of entity to build
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public interface RowMapper<T> {

    /**
     * Builds an entity based on data from ResultSet.
     * <p>If a column is SQL NULL,
     * entity field associated with it are to be set null if possible</p>
     * @param resultSet JDBC ResultSet of a query.
     * @return Instance of entity
     * @throws SQLException
     */
    T map(ResultSet resultSet) throws SQLException;

}
