package by.epam.baranovsky.banking.dao.rowmapper.impl;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.dao.rowmapper.RowMapper;
import by.epam.baranovsky.banking.entity.Penalty;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PenaltyRowMapper implements RowMapper<Penalty> {
    @Override
    public Penalty map(ResultSet resultSet) throws SQLException {
        Penalty penalty = new Penalty();

        penalty.setId(resultSet.getInt(DBMetadata.PENALTIES_ID));

        penalty.setTypeId(resultSet.getInt(DBMetadata.PENALTIES_TYPE_ID));
        penalty.setTypeName(resultSet.getString(DBMetadata.PENALTY_TYPE_NAME));

        penalty.setNotice(resultSet.getString(DBMetadata.PENALTIES_NOTICE));
        penalty.setUserId(resultSet.getInt(DBMetadata.PENALTIES_USER_ID));

        penalty.setStatusId(resultSet.getInt(DBMetadata.PENALTIES_STATUS_ID));
        penalty.setStatusName(resultSet.getString(DBMetadata.PENALTY_STATUS_NAME));

        penalty.setValue(
                resultSet.getDouble(DBMetadata.PENALTIES_VALUE) != 0
                        ? resultSet.getDouble(DBMetadata.PENALTIES_VALUE)
                        : null);
        penalty.setPaymentAccountId(
                resultSet.getInt(DBMetadata.PENALTIES_PAYMENT_ACC_ID) != 0
                        ? resultSet.getInt(DBMetadata.PENALTIES_PAYMENT_ACC_ID)
                        : null );

        return penalty;
    }
}
