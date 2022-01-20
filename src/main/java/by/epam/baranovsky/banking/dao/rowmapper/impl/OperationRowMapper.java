package by.epam.baranovsky.banking.dao.rowmapper.impl;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.dao.rowmapper.RowMapper;
import by.epam.baranovsky.banking.entity.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OperationRowMapper implements RowMapper<Operation> {
    @Override
    public Operation map(ResultSet resultSet) throws SQLException {
        Operation operation = new Operation();

        operation.setId(resultSet.getInt(DBMetadata.OPERATIONS_ID));
        operation.setValue(resultSet.getDouble(DBMetadata.OPERATIONS_VALUE));

        operation.setTypeId(resultSet.getInt(DBMetadata.OPERATION_TYPES_ID));
        operation.setTypeName(resultSet.getString(DBMetadata.OPERATION_TYPES_NAME));

        operation.setAccountId(resultSet.getInt(DBMetadata.OPERATIONS_ACC_ID));
        operation.setTargetAccountId(resultSet.getInt(DBMetadata.OPERATIONS_TARGET_ACC_ID));

        operation.setBankCardId(resultSet.getInt(DBMetadata.OPERATIONS_CARD_ID));
        operation.setTargetBankCardId(resultSet.getInt(DBMetadata.OPERATIONS_TARGET_CARD_ID));
        operation.setBillId(resultSet.getInt(DBMetadata.OPERATIONS_BILL_ID));
        operation.setPenaltyId(resultSet.getInt(DBMetadata.OPERATIONS_PENALTY_ID));

        return operation;
    }
}
