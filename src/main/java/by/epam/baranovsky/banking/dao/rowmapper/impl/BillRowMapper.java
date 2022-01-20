package by.epam.baranovsky.banking.dao.rowmapper.impl;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.dao.rowmapper.RowMapper;
import by.epam.baranovsky.banking.entity.Bill;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BillRowMapper implements RowMapper<Bill> {


    @Override
    public Bill map(ResultSet resultSet) throws SQLException {
        Bill bill = new Bill();

        bill.setId(resultSet.getInt(DBMetadata.BILLS_ID));
        bill.setValue(resultSet.getDouble(DBMetadata.BILLS_VALUE));

        bill.setIssueDate(resultSet.getDate(DBMetadata.BILLS_ISSUE_DATE));
        bill.setDueDate(resultSet.getDate(DBMetadata.BILLS_DUE_DATE));

        bill.setUserId(resultSet.getInt(DBMetadata.BILLS_USER_ID));
        bill.setPaymentAccountId(resultSet.getInt(DBMetadata.BILLS_PAYMENT_ACC_ID));

        bill.setStatusId(resultSet.getInt(DBMetadata.BILL_STATUS_ID));
        bill.setStatusName(resultSet.getString(DBMetadata.BILL_STATUS_NAME));

        bill.setPenaltyId(resultSet.getInt(DBMetadata.BILLS_PENALTY_ID));
        bill.setLoanId(resultSet.getInt(DBMetadata.BILLS_LOAN_ID));

        return bill;
    }
}
