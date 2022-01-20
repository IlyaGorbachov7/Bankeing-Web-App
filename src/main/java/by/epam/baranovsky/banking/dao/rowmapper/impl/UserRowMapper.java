package by.epam.baranovsky.banking.dao.rowmapper.impl;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.dao.rowmapper.RowMapper;
import by.epam.baranovsky.banking.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User map(ResultSet resultSet) throws SQLException {
        User user = new User();

        user.setId(resultSet.getInt(DBMetadata.USERS_ID));
        user.setEmail(resultSet.getString(DBMetadata.USERS_EMAIL));
        user.setPassword(resultSet.getString(DBMetadata.USERS_PASSWORD));

        user.setRoleId(resultSet.getInt(DBMetadata.USERS_ROLE_ID));
        user.setRoleName(resultSet.getString(DBMetadata.USER_ROLES_DESC));

        user.setFirstName(resultSet.getString(DBMetadata.USERS_NAME));
        user.setLastName(resultSet.getString(DBMetadata.USERS_SURNAME));
        user.setPatronymic(resultSet.getString(DBMetadata.USERS_PATRONYMIC));

        user.setPassportSeries(resultSet.getString(DBMetadata.USERS_PASS_SERIES));
        user.setPassportNumber(resultSet.getString(DBMetadata.USERS_PASS_NUMBER));

        user.setBirthDate(resultSet.getDate(DBMetadata.USERS_BIRTHDATE));
        user.setLastLogin(resultSet.getDate(DBMetadata.USERS_LAST_LOGIN));
        user.setDateCreated(resultSet.getDate(DBMetadata.USERS_DATE_CREATED));

        return user;
    }

}
