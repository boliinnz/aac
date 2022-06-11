package net.contal.demo.services;

import net.contal.demo.AccountNumberUtil;
import net.contal.demo.exceptions.AccountNumberGenException;
import net.contal.demo.exceptions.BadRequestException;
import net.contal.demo.exceptions.NotFoundException;
import net.contal.demo.modal.BankTransaction;
import net.contal.demo.modal.CustomerAccount;
import net.contal.demo.repositories.BankTransactionRepository;
import net.contal.demo.repositories.CustomerAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO complete this service class
 * TODO use BankServiceTest class
 * @author Bo Li
 */
@Service
@Transactional
public class NewBankService {
    private static final int MAX_GENERATED_COUNT = 5;

    final Logger logger = LoggerFactory.getLogger(NewBankService.class);

    private final CustomerAccountRepository accountRepository;

    private final BankTransactionRepository bankRepository;

    public NewBankService(CustomerAccountRepository accountRepository, BankTransactionRepository bankRepository) {
        this.accountRepository = accountRepository;
        this.bankRepository = bankRepository;
    }

    /**
     * Save customAccount to database
     * return AccountNumber
     * @param customerAccount populate this (firstName , lastName) already provided
     * @return accountNumber
     */
    public int createAnAccount(CustomerAccount customerAccount){
        int accountNumber = this.getAccountNumber();
        customerAccount.setAccountNumber(accountNumber);
        if (customerAccount.getAccountBalance() > 0) {
            BankTransaction bankTransaction = new BankTransaction();
            bankTransaction.setTransactionAmount(customerAccount.getAccountBalance());
            customerAccount.addTransaction(bankTransaction);
        }
        accountRepository.save(customerAccount);
        return accountNumber;
    }

    /**
     * TODO implement this functions
     * @param accountNumber target account number
     * @param amount amount to register as transaction
     * @return boolean , if added as transaction
     */
    public boolean addTransactions(int accountNumber , Double amount){
        /**
         * Find and account by using accountNumber (Only write  the query in hql String  )
         * create Transaction for account with provided  amount
         * return true if added , return false if account dont exist , or amount is null
         */
        if (amount == null || amount == 0) {
            return false;
        }
        Optional<CustomerAccount> customerAccountOpt = accountRepository.findByAccountNumber(accountNumber);
        if (customerAccountOpt.isPresent()) {
            CustomerAccount customerAccount = customerAccountOpt.get();

            //check balance
            if (amount < 0 && (customerAccount.getAccountBalance() + amount) < 0) {
                throw new BadRequestException(String.format("Insufficient balance for account %s", accountNumber));
            }
            BankTransaction bankTransaction = new BankTransaction();
            bankTransaction.setTransactionAmount(amount);
            bankTransaction.setCustomerAccount(customerAccount);
            customerAccount.addTransaction(bankTransaction);
            // to update account balance
            customerAccount.addAccountBalance(amount);
            accountRepository.save(customerAccount);

            return true;
        }
        return false;
    }

    /**
     * Get Balance
     *
     * @param accountNumber target account
     * @return account balance
     */
    public double getBalance(int accountNumber){
        /**
         *
         *  find the account by this account Number
         *  sum total of transactions belong to account
         *  return sum of amount
         *
         */
        Optional<CustomerAccount> customerAccountOpt = accountRepository.findByAccountNumber(accountNumber);
        if (customerAccountOpt.isPresent()) {
            return customerAccountOpt.get().getAccountBalance();
        } else {
            throw new NotFoundException(String.format("Account was not found for accountNumber %s.", accountNumber));
        }
    }


    /**
     * Get Date Balance by account number.
     *
     * ADVANCE TASK
     * @param accountNumber accountNumber
     * @return HashMap [key: date , value: double]
     */
    public Map<LocalDate,Double> getDateBalance(int accountNumber){
        /**
         * get all bank Transactions for this account number
         * Create map , Each Entry should hold a Date as a key and value as balance on key date from start of account
         * Example data [01/01/1992 , 2000$] balance 2000$ that date 01/01/1992
         */
        Optional<CustomerAccount> customerAccountOpt = accountRepository.findByAccountNumber(accountNumber);
        if (customerAccountOpt.isPresent()) {
            List<BankTransaction> transactions = customerAccountOpt.get().getTransactions();
            if (transactions != null) {
                Map<LocalDate,Double> dateBalance = transactions.stream().collect(
                        Collectors.groupingBy(BankTransaction::getTransactionDate,
                                Collectors.summingDouble(BankTransaction::getTransactionAmount)));

                return dateBalance;
            }
            return new HashMap<>();
        } else {
            throw new NotFoundException(String.format("Account was not found for accountNumber %s.", accountNumber));
        }
    }

    /**
     * Retrieve customer account details by account number.
     *
     * @param accountNumber
     * @return CustomerAccount
     */
    public CustomerAccount findByAccountNumber(int accountNumber) {

        Optional<CustomerAccount> customerAccountOpt = accountRepository.findByAccountNumber(accountNumber);
        if (customerAccountOpt.isPresent()) {
            return customerAccountOpt.get();
        } else {
            throw new NotFoundException(String.format("Account was not found for accountNumber %s.", accountNumber));
        }
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

        return bankRepository.findTop10ByCustomerAccount_AccountNumberOrderByIdDesc(accountNumber);

    }

    /**
     * return a random account number and validate if it is used in database
     * @return integer account number
     */
    int getAccountNumber() {
        int accountNumber = AccountNumberUtil.generateAccountNumber();
        Optional<CustomerAccount> accountOptional = accountRepository.findByAccountNumber(accountNumber);
        int cnt = 1;
        while (accountOptional.isPresent() && cnt < MAX_GENERATED_COUNT) {
            cnt ++;
            accountNumber = AccountNumberUtil.generateAccountNumber();
            accountOptional = accountRepository.findByAccountNumber(accountNumber);
        }
        if (accountOptional.isPresent()) {
            throw new AccountNumberGenException("Generating Account Number failed! Please wait and try again.");
        }
        return accountNumber;
    }

}
