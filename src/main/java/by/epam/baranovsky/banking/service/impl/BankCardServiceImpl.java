package by.epam.baranovsky.banking.service.impl;

import by.epam.baranovsky.banking.dao.BankCardDAO;
import by.epam.baranovsky.banking.dao.exception.DAOException;
import by.epam.baranovsky.banking.dao.factory.impl.SqlDAOFactory;
import by.epam.baranovsky.banking.entity.BankingCard;
import by.epam.baranovsky.banking.service.BankCardService;
import by.epam.baranovsky.banking.service.exception.ServiceException;
import by.epam.baranovsky.banking.service.exception.ValidationException;
import by.epam.baranovsky.banking.service.validator.BankCardValidator;

import java.util.Date;
import java.util.List;

public class BankCardServiceImpl implements BankCardService {

    private final BankCardValidator validator = new BankCardValidator();
    private final BankCardDAO cardDAO = SqlDAOFactory.getInstance().getBankCardDAO();
    private static volatile BankCardServiceImpl instance = null;

    private BankCardServiceImpl() {}

    public static BankCardServiceImpl getInstance() {
        if (instance == null) {
            synchronized (BankCardServiceImpl.class) {
                if (instance == null) {
                    instance = new BankCardServiceImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public BankingCard findById(Integer id) throws ServiceException {
        BankingCard card;
        try {
            card = cardDAO.findEntityById(id);
        } catch (DAOException e) {
            throw  new ServiceException(e);
        }
        return card;
    }

    @Override
    public BankingCard findByNumberAndCvc(String number, String cvc) throws ServiceException {

        BankingCard card;
        try {
            card = cardDAO.findByNumberAndCvc(number, cvc);
        } catch (DAOException e) {
            throw  new ServiceException(e);
        }
        return card;
    }

    @Override
    public List<BankingCard>  findByNumber(String number) throws ServiceException{
        List<BankingCard> cards;
        try {
            cards = cardDAO.findByNumber(number);
        } catch (DAOException e) {
            throw  new ServiceException(e);
        }
        return cards;
    }

    @Override
    public List<BankingCard> findByType(Integer typeId) throws ServiceException {
        List<BankingCard> cards;
        try {
            cards = cardDAO.findByType(typeId);
        } catch (DAOException e) {
            throw  new ServiceException(e);
        }
        return cards;
    }

    @Override
    public List<BankingCard> findByUser(Integer userId) throws ServiceException {
        List<BankingCard> cards;
        try {
            cards = cardDAO.findByUser(userId);
        } catch (DAOException e) {
            throw  new ServiceException(e);
        }
        return cards;
    }

    @Override
    public List<BankingCard> findByAccount(Integer accountId) throws ServiceException {
        List<BankingCard> cards;
        try {
            cards = cardDAO.findByAccount(accountId);
        } catch (DAOException e) {
            throw  new ServiceException(e);
        }
        return cards;
    }

    @Override
    public Integer update(BankingCard card) throws ServiceException {
        Integer result;
        try{
            if(!validator.validate(card)){
                throw new ValidationException();
            }
            if(card.getId() == null || cardDAO.findEntityById(card.getId()) == null){
                throw new ValidationException("No card with such ID");
            }
            result = cardDAO.update(card);
        } catch (DAOException e) {
            throw  new ServiceException(e);
        }
        return result;
    }

    @Override
    public BankingCard create(BankingCard card) throws ServiceException {
        BankingCard result;
        try{
            if(!validator.validate(card)){
                throw new ValidationException();
            }
            result = cardDAO.findEntityById(cardDAO.create(card));
        } catch (DAOException e) {
            throw  new ServiceException(e);
        }
        return result;
    }

    @Override
    public Integer delete(Integer id) throws ServiceException {
        try {
            return delete(cardDAO.findEntityById(id));
        } catch (DAOException e) {
            throw  new ServiceException(e);
        }
    }

    @Override
    public Integer delete(BankingCard card) throws ServiceException {
        Integer res;
        try {
            if(!validator.validate(card)){
                throw new ValidationException();
            }
            res = cardDAO.delete(card);
        } catch (DAOException e) {
            throw  new ServiceException(e);
        }
        return res;
    }

    @Override
    public List<BankingCard> findAll() throws ServiceException {
        List<BankingCard> cards;
        try {
            cards = cardDAO.findAll();
        } catch (DAOException e) {
            throw  new ServiceException(e);
        }
        return cards;
    }
}
