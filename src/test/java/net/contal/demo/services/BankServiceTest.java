package net.contal.demo.services;

import net.contal.demo.exceptions.BadRequestException;
import net.contal.demo.exceptions.NotFoundException;
import net.contal.demo.modal.BankTransaction;
import net.contal.demo.modal.CustomerAccount;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BankServiceTest {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private String firstName = "test_firstname";
    private String lastName = "test_lastname";

    private CustomerAccount customerAccount;

    @Autowired
    private BankService bankService;

    private static int accountNumber;

    @Test
    @Order(1)
    public void createAnAccount() {
        customerAccount = new CustomerAccount();
        customerAccount.setFirstName(firstName);
        customerAccount.setLastName(lastName);
        accountNumber = bankService.createAnAccount(customerAccount);
        CustomerAccount customerAccountOpt = bankService.findByAccountNumber(accountNumber);
        SoftAssertions.assertSoftly(as -> {
            as.assertThat(customerAccount.getAccountNumber()).isEqualTo(accountNumber);
            as.assertThat(customerAccount.getFirstName()).isEqualTo(firstName);
            as.assertThat(customerAccount.getLastName()).isEqualTo(lastName);
            as.assertThat(customerAccount.getAccountBalance()).isEqualTo(0);
            as.assertThat(customerAccount.getTransactions()).isNull();
        });
    }

    @Test
    @Order(2)
    public void addTransactions() {
        boolean result = bankService.addTransactions(accountNumber, 500.0d);

        assertTrue(result);


    }

    @Test
    @Order(3)
    public void addTransactions_insufficientBalance() {
        SoftAssertions.assertSoftly(as -> {

            as.assertThatThrownBy(() -> bankService.addTransactions(accountNumber, -600.0d))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("Insufficient balance for account " + accountNumber);
        });
    }

    @Test
    @Order(4)
    public void addTransactions_amountNull() {
        boolean result = bankService.addTransactions(accountNumber, null);
        assertFalse(result);

    }

    @Test
    @Order(5)
    public void addTransactions_amountZero() {
        boolean result = bankService.addTransactions(accountNumber, 0.0d);
        assertFalse(result);

    }

    @Test
    @Order(6)
    public void getBalance() {
        double balance = bankService.getBalance(accountNumber);
        SoftAssertions.assertSoftly(as -> {
            as.assertThat(balance).isEqualTo(500.0d);
        });
    }

    @Test
    public void getBalance_accountNotFound() {

        SoftAssertions.assertSoftly(as -> {
            as.assertThatThrownBy(() -> bankService.getBalance(1))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("Account was not found for accountNumber 1.");
        });
    }

    @Test
    @Order(8)
    public void getDateBalance() {
        boolean result = bankService.addTransactions(accountNumber, 150.0d);

        Map<LocalDate,Double> dateBalance = bankService.getDateBalance(accountNumber);
        SoftAssertions.assertSoftly(as -> {
            as.assertThat(dateBalance).isNotEmpty();
            as.assertThat(dateBalance.size()).isEqualTo(1);
            as.assertThat(dateBalance.keySet()).contains(LocalDate.now());
            as.assertThat(dateBalance.get(LocalDate.now())).isEqualTo(650.0d);
        });
    }

    @Test
    public void getDateBalance_accountNotFound() {
        SoftAssertions.assertSoftly(as -> {
            as.assertThatThrownBy(() -> bankService.getDateBalance(1))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("Account was not found for accountNumber 1.");
        });
    }

    @Test
    @Order(9)
    public void getLastTenTransactions() {
        bankService.addTransactions(accountNumber, 100.0d);
        bankService.addTransactions(accountNumber, 200.0d);
        bankService.addTransactions(accountNumber, 300.0d);
        bankService.addTransactions(accountNumber, 400.0d);
        bankService.addTransactions(accountNumber, 500.0d);
        bankService.addTransactions(accountNumber, 600.0d);
        bankService.addTransactions(accountNumber, 700.0d);
        bankService.addTransactions(accountNumber, 800.0d);
        bankService.addTransactions(accountNumber, 900.0d);
        bankService.addTransactions(accountNumber, 1000.0d);
        bankService.addTransactions(accountNumber, 1100.0d);
        bankService.addTransactions(accountNumber, 1200.0d);
        List<BankTransaction> bankTransactions = bankService.getLastTenTransactions(accountNumber);

        SoftAssertions.assertSoftly(as -> {
            as.assertThat(bankTransactions).isNotEmpty();
            as.assertThat(bankTransactions.size()).isEqualTo(10);
            as.assertThat(bankTransactions.get(0).getTransactionAmount()).isEqualTo(1200.0d);
            as.assertThat(bankTransactions.get(1).getTransactionAmount()).isEqualTo(1100.0d);
            as.assertThat(bankTransactions.get(2).getTransactionAmount()).isEqualTo(1000.0d);
            as.assertThat(bankTransactions.get(3).getTransactionAmount()).isEqualTo(900.0d);
            as.assertThat(bankTransactions.get(4).getTransactionAmount()).isEqualTo(800.0d);
            as.assertThat(bankTransactions.get(5).getTransactionAmount()).isEqualTo(700.0d);
            as.assertThat(bankTransactions.get(6).getTransactionAmount()).isEqualTo(600.0d);
            as.assertThat(bankTransactions.get(7).getTransactionAmount()).isEqualTo(500.0d);
            as.assertThat(bankTransactions.get(8).getTransactionAmount()).isEqualTo(400.0d);
            as.assertThat(bankTransactions.get(9).getTransactionAmount()).isEqualTo(300.0d);
        });
    }
}
