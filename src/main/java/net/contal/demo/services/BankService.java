package net.contal.demo.services;

import net.contal.demo.AccountNumberUtil;
import net.contal.demo.DbUtils;
import net.contal.demo.exceptions.BadRequestException;
import net.contal.demo.exceptions.NotFoundException;
import net.contal.demo.exceptions.TransactionalException;
import net.contal.demo.modal.BankTransaction;
import net.contal.demo.modal.CustomerAccount;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * TODO complete this service class
 * TODO use BankServiceTest class
 * @author Bo Li
 */
@Service
@Transactional
public class BankService {
    final Logger logger = LoggerFactory.getLogger(BankService.class);

    //USE this class to access database , you can call openASession to access database
    private final DbUtils dbUtils;
    @Autowired
    public BankService(DbUtils dbUtils) {
        this.dbUtils = dbUtils;
    }

    /**
     * TODO implement the rest , populate require fields for CustomAccount (Generate Back account by using AccountNumberUtil )
     * Save customAccount to database
     * return AccountNumber
     * @param customerAccount populate this (firstName , lastName ) already provided
     * @return accountNumber
     */
    public int createAnAccount(CustomerAccount customerAccount){
        int accountNumber = AccountNumberUtil.generateAccountNumber();
        customerAccount.setAccountNumber(accountNumber);

        Transaction transaction = null;
        try (Session session = dbUtils.openASession();) {

            if (customerAccount.getAccountBalance() > 0) {
                BankTransaction bankTransaction = new BankTransaction();
                bankTransaction.setTransactionAmount(customerAccount.getAccountBalance());
                customerAccount.addTransaction(bankTransaction);
            }
            session.save(customerAccount);
            // commit transaction
            session.getTransaction().commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(e.getMessage());
            throw new TransactionalException(
                    String.format("Creating an account failed for %s %s",
                            customerAccount.getFirstName(), customerAccount.getLastName()));
        }
        return accountNumber;
    }

    /**
     * TODO implement this functions
     * @param accountNumber target account number
     * @param amount amount to register as transaction
     * @return boolean , if added as transaction
     */
    public boolean addTransactions(int accountNumber , Double amount){

        if (amount == null || amount == 0) {
            return false;
        }
        /**
         *TODO
         * Find and account by using accountNumber (Only write  the query in hql String  )
         * create Transaction for account with provided  amount
         * return true if added , return false if account dont exist , or amount is null
         */

        /** TODO write Query to get account by number un comment section below , catch query   */
        boolean added = false;
        boolean insufficientBalance = false;
        String hql = " FROM CustomerAccount C WHERE C.accountNumber = :accountNumber";
        Transaction transaction = null;
        try (Session session = dbUtils.openASession()) {

            CustomerAccount customerAccount = session.createQuery(hql, CustomerAccount.class)
                    .setParameter("accountNumber",accountNumber)
                    .getSingleResult();

            //check balance
            if (amount < 0 && (customerAccount.getAccountBalance() + amount) < 0) {
                added = false;
                insufficientBalance = true;
            } else {
                BankTransaction bankTransaction = new BankTransaction();
                bankTransaction.setCustomerAccount(customerAccount);
                bankTransaction.setTransactionAmount(amount);
                customerAccount.addTransaction(bankTransaction);
                // to update account balance
                customerAccount.addAccountBalance(amount);
                session.save(customerAccount);
                added = true;
            }

            // commit transaction
            session.getTransaction().commit();
        } catch (NoResultException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            added = false;
            logger.error(String.format("Account was not found for accountNumber %s.", accountNumber));
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            added = false;
            logger.error(e.getMessage());
            throw new TransactionalException(
                    String.format("Adding a transaction failed for account %s", accountNumber));
        }
        if (insufficientBalance) {
            throw new BadRequestException(String.format("Insufficient balance for account %s", accountNumber));
        }
        return added;
    }


    /**
     * TODO implement this functions
     * @param accountNumber target account
     * @return account balance
     */
    public double getBalance(int accountNumber){

        /**
         *TODO
         *  find the account by this account Number
         *  sum total of transactions belong to account
         *  return sum of amount
         *
         */
        Double accountBalance = 0.0d;
        String hql = "SELECT C.accountBalance FROM CustomerAccount C WHERE C.accountNumber = :accountNumber";

        try (Session session = dbUtils.openASession()){
            accountBalance = session.createQuery(hql, Double.class)
                    .setParameter("accountNumber", accountNumber).getSingleResult();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            throw new NotFoundException(String.format("Account was not found for accountNumber %s.", accountNumber));
        }

        return accountBalance;
    }

    /**
     * TODO implement this functions
     * ADVANCE TASK
     * @param accountNumber accountNumber
     * @return HashMap [key: date , value: double]
     */
    public Map<LocalDate,Double> getDateBalance(int accountNumber) {
        /**
         *TODO
         * get all bank Transactions for this account number
         * Create map , Each Entry should hold a Date as a key and value as balance on key date from start of account
         * Example data [01/01/1992 , 2000$] balance 2000$ that date 01/01/1992
         */

        //check if the account number exists
        findByAccountNumber(accountNumber);

        String hql = "SELECT B FROM BankTransaction B inner join B.customerAccount C WHERE C.accountNumber = :accountNumber";
        Map<LocalDate, Double> dateBalance = new HashMap<>();
        try (Session session = dbUtils.openASession()) {
            List<BankTransaction> transactions = session.createQuery(hql, BankTransaction.class)
                    .setParameter("accountNumber", accountNumber).getResultList();

            if (transactions.size() > 0) {
                dateBalance = transactions.stream().collect(
                        Collectors.groupingBy(BankTransaction::getTransactionDate,
                                Collectors.summingDouble(BankTransaction::getTransactionAmount)));
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return dateBalance;
    }

    /**
     * Get last 10 transactions
     *
     * @param accountNumber accountNumber
     * @return HashMap [key: date , value: double]
     */
    public List<BankTransaction> getLastTenTransactions(int accountNumber) {
        List<BankTransaction> bankTransactions = new ArrayList<>();
         //check if the account number exists
        findByAccountNumber(accountNumber);

        String hql = "SELECT B FROM BankTransaction B inner join B.customerAccount C WHERE C.accountNumber = :accountNumber ORDER BY B.id DESC";
        Map<LocalDate, Double> dateBalance = new HashMap<>();
        try (Session session = dbUtils.openASession()) {
            bankTransactions = session.createQuery(hql, BankTransaction.class)
            .setParameter("accountNumber", accountNumber).setMaxResults(10).getResultList();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return bankTransactions;
    }

    /**
     * Retrieve customer account details by account number.
     *
     * @param accountNumber
     * @return CustomerAccount
     */
    public CustomerAccount findByAccountNumber(int accountNumber) {
        CustomerAccount customerAccount = null;
        String hql = " FROM CustomerAccount C WHERE C.accountNumber = :accountNumber";

        try (Session session = dbUtils.openASession()) {
            customerAccount = session.createQuery(hql, CustomerAccount.class)
                    .setParameter("accountNumber", accountNumber).getSingleResult();
        } catch (NoResultException e) {
            // continue
            logger.error(e.getMessage(), e);
            throw new NotFoundException(String.format("Account was not found for accountNumber %s.", accountNumber));
        }

        return customerAccount;
    }
}
