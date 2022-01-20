package by.epam.baranovsky.banking.dao.rowmapper.impl;

import by.epam.baranovsky.banking.constant.DBMetadata;
import by.epam.baranovsky.banking.dao.rowmapper.RowMapper;
import by.epam.baranovsky.banking.entity.BankingCard;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CardRowMapper implements RowMapper<BankingCard> {
    @Override
    public BankingCard map(ResultSet resultSet) throws SQLException {
        BankingCard card = new BankingCard();

        card.setId(resultSet.getInt(DBMetadata.BANK_CARDS_ID));
        card.setBalance(resultSet.getDouble(DBMetadata.BANK_CARDS_BALANCE));

        card.setCvc(resultSet.getInt(DBMetadata.BANK_CARDS_CVC));
        card.setNumber(resultSet.getString(DBMetadata.BANK_CARDS_NUMBER));
        card.setPin(resultSet.getInt(DBMetadata.BANK_CARDS_PIN));

        card.setRegistrationDate(resultSet.getDate(DBMetadata.BANK_CARDS_REGISTRATION_DATE));
        card.setExpirationDate(resultSet.getDate(DBMetadata.BANK_CARDS_EXPIRATION_DATE));

        card.setOverdraftMax(resultSet.getDouble(DBMetadata.BANK_CARDS_OVERDRAFT_MAXIMUM));
        card.setOverdraftInterestRate(resultSet.getDouble(DBMetadata.BANK_CARDS_OVERDRAFT_INTEREST));

        card.setUserId(resultSet.getInt(DBMetadata.BANK_CARDS_USER_ID));
        card.setAccountId(resultSet.getInt(DBMetadata.BANK_CARDS_ACCOUNT_ID));

        card.setStatusId(resultSet.getInt(DBMetadata.BANK_CARDS_STATUS_ID));
        card.setStatusName(resultSet.getString(DBMetadata.CARD_STATUS_NAME));

        card.setCardTypeId(resultSet.getInt(DBMetadata.BANK_CARDS_TYPE_ID));
        card.setCardTypeName(resultSet.getString(DBMetadata.CARD_TYPE_NAME));

        return card;
    }
}
