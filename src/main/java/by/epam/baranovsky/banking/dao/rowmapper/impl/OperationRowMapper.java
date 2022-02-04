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

        operation.setTypeId(resultSet.getInt(DBMetadata.OPERATION_TYPES_ID));
        operation.setTypeName(resultSet.getString(DBMetadata.OPERATION_TYPES_NAME));
        operation.setOperationDate(resultSet.getTimestamp(DBMetadata.OPERATIONS_DATE));

        operation.setValue(
                resultSet.getDouble(DBMetadata.OPERATIONS_VALUE) !=0
                        ? resultSet.getDouble(DBMetadata.OPERATIONS_VALUE)
                        : null);

        operation.setAccountId(
                resultSet.getInt(DBMetadata.OPERATIONS_ACC_ID) !=0
                        ?  resultSet.getInt(DBMetadata.OPERATIONS_ACC_ID)
                        : null);

        operation.setTargetAccountId(
                resultSet.getInt(DBMetadata.OPERATIONS_TARGET_ACC_ID) !=0
                        ?  resultSet.getInt(DBMetadata.OPERATIONS_TARGET_ACC_ID)
                        : null);

        operation.setBankCardId(
                resultSet.getInt(DBMetadata.OPERATIONS_CARD_ID) !=0
                        ?  resultSet.getInt(DBMetadata.OPERATIONS_CARD_ID)
                        : null);

        operation.setTargetBankCardId(
                resultSet.getInt(DBMetadata.OPERATIONS_TARGET_CARD_ID) !=0
                        ?  resultSet.getInt(DBMetadata.OPERATIONS_TARGET_CARD_ID)
                        : null);

        operation.setBillId(
                resultSet.getInt(DBMetadata.OPERATIONS_BILL_ID) !=0
                        ?  resultSet.getInt(DBMetadata.OPERATIONS_BILL_ID)
                        : null);

        operation.setPenaltyId(
                resultSet.getInt(DBMetadata.OPERATIONS_PENALTY_ID) !=0
                        ?  resultSet.getInt(DBMetadata.OPERATIONS_PENALTY_ID)
                        : null);

        operation.setCommission(
                resultSet.getDouble(DBMetadata.OPERATIONS_COMMISSION) != 0
                        ? resultSet.getDouble(DBMetadata.OPERATIONS_COMMISSION)
                        : null);

        return operation;
    }
}
