package by.epam.baranovsky.banking.dao.rowmapper;

import by.epam.baranovsky.banking.entity.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<T> {

    T map(ResultSet resultSet) throws SQLException;

}
