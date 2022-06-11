package net.contal.demo.services;

import net.contal.demo.exceptions.BadRequestException;
import net.contal.demo.exceptions.AccountNumberGenException;
import net.contal.demo.exceptions.NotFoundException;
import net.contal.demo.modal.BankTransaction;
import net.contal.demo.modal.CustomerAccount;
import net.contal.demo.repositories.BankTransactionRepository;
import net.contal.demo.repositories.CustomerAccountRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NewBankServiceTest {
    @InjectMocks
    private NewBankService bankService;

    @Mock
    private CustomerAccountRepository accountRepository;
    @Mock
    private BankTransactionRepository bandRepository;

    private final ArgumentCaptor<CustomerAccount> customerAccountArgumentCaptor = ArgumentCaptor.forClass(CustomerAccount.class);

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private String firstName = "test_firstname";
    private String lastName = "test_lastname";

    private CustomerAccount customerAccount;

    @BeforeEach
    public void setup() {
        bankService = new NewBankService(accountRepository, bandRepository);

        customerAccount = new CustomerAccount();
        customerAccount.setFirstName(firstName);
        customerAccount.setLastName(lastName);
    }

    @Test
    public void createAnAccount() {
        bankService.createAnAccount(customerAccount);
        SoftAssertions.assertSoftly(as -> {
            verify(accountRepository, times(1)).save(customerAccountArgumentCaptor.capture());
            as.assertThat(customerAccountArgumentCaptor.getValue().getFirstName()).isEqualTo(firstName);
            as.assertThat(customerAccountArgumentCaptor.getValue().getLastName()).isEqualTo(lastName);
            as.assertThat(customerAccountArgumentCaptor.getValue().getAccountNumber()).isGreaterThan(0);
            as.assertThat(customerAccountArgumentCaptor.getValue().getAccountBalance()).isEqualTo(0);
            as.assertThat(customerAccountArgumentCaptor.getValue().getTransactions()).isNull();
        });
    }

    @Test
    public void createAnAccount_AccountNumberGenerationException() {
        when(accountRepository.findByAccountNumber(anyInt())).thenReturn(Optional.of(customerAccount));
        SoftAssertions.assertSoftly(as -> {
            as.assertThatThrownBy(() -> bankService.createAnAccount(customerAccount))
                    .isInstanceOf(AccountNumberGenException.class)
                    .hasMessage("Generating Account Number failed! Please wait and try again.");
            verify(accountRepository, times(5)).findByAccountNumber(anyInt());
            verify(accountRepository, never()).save(any());

        });
    }

    @Test
    public void addTransactions() {
        customerAccount.setAccountNumber(1);
        customerAccount.setAccountBalance(1200.0d);

        when(accountRepository.findByAccountNumber(1)).thenReturn(Optional.of(customerAccount));

        boolean result = bankService.addTransactions(1, 500.0d);
        SoftAssertions.assertSoftly(as -> {
            as.assertThat(result).isTrue();
            verify(accountRepository, times(1)).save(customerAccountArgumentCaptor.capture());
            as.assertThat(customerAccountArgumentCaptor.getValue().getFirstName()).isEqualTo(firstName);
            as.assertThat(customerAccountArgumentCaptor.getValue().getLastName()).isEqualTo(lastName);
            as.assertThat(customerAccountArgumentCaptor.getValue().getAccountNumber()).isEqualTo(1);
            as.assertThat(customerAccountArgumentCaptor.getValue().getTransactions().size()).isEqualTo(1);
            as.assertThat(customerAccountArgumentCaptor.getValue().getTransactions().get(0).getTransactionAmount()).isEqualTo(500.0d);
        });
    }

    @Test
    public void addTransactions_insufficientBalance() {
        customerAccount.setAccountNumber(1);
        customerAccount.setAccountBalance(400.0d);

        when(accountRepository.findByAccountNumber(1)).thenReturn(Optional.of(customerAccount));

        SoftAssertions.assertSoftly(as -> {
            as.assertThatThrownBy(() -> bankService.addTransactions(1, -500.0d))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("Insufficient balance for account 1");

            verify(accountRepository, never()).save(any());
        });
    }

    @Test
    public void addTransactions_amountNull() {
        boolean result = bankService.addTransactions(1, null);
        SoftAssertions.assertSoftly(as -> {
            verify(accountRepository, never()).save(any());
            as.assertThat(result).isFalse();
        });
    }

    @Test
    public void addTransactions_amountZero() {
        boolean result = bankService.addTransactions(1, 0.0d);
        SoftAssertions.assertSoftly(as -> {
            verify(accountRepository, never()).save(any());
            as.assertThat(result).isFalse();
        });
    }

    @Test
    public void getBalance() {
        customerAccount.setAccountNumber(1);
        customerAccount.setAccountBalance(1200.0d);

        when(accountRepository.findByAccountNumber(1)).thenReturn(Optional.of(customerAccount));

        double balance = bankService.getBalance(1);
        SoftAssertions.assertSoftly(as -> {
            as.assertThat(balance).isEqualTo(1200.0d);
        });
    }

    @Test
    public void getBalance_accountNotFound() {
        SoftAssertions.assertSoftly(as -> {
            as.assertThatThrownBy(() -> bankService.getBalance(1))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("Account was not found for accountNumber 1.");

            verify(accountRepository, never()).save(any());
        });
    }

    @Test
    public void getDateBalance() {
        String date1 = "16/02/2022";
        LocalDate localDate1 = LocalDate.parse(date1, formatter);

        String date2 = "18/02/2022";
        LocalDate localDate2 = LocalDate.parse(date2, formatter);

        customerAccount.setAccountNumber(1);
        customerAccount.setAccountBalance(1200.0d);
        BankTransaction bt1 = new BankTransaction();
        bt1.setTransactionAmount(1000.0d);
        bt1.setTransactionDate(localDate1);

        BankTransaction bt2_1 = new BankTransaction();
        bt2_1.setTransactionAmount(-500.0d);
        bt2_1.setTransactionDate(localDate2);

        BankTransaction bt2_2 = new BankTransaction();
        bt2_2.setTransactionAmount(300.0d);
        bt2_2.setTransactionDate(localDate2);

        customerAccount.addTransaction(bt1);
        customerAccount.addTransaction(bt2_1);
        customerAccount.addTransaction(bt2_2);

        List<LocalDate> localDateList = new ArrayList<LocalDate>();
        localDateList.add(localDate1);
        localDateList.add(localDate2);

        when(accountRepository.findByAccountNumber(1)).thenReturn(Optional.of(customerAccount));

        Map<LocalDate, Double> dateBalance = bankService.getDateBalance(1);
        SoftAssertions.assertSoftly(as -> {
            as.assertThat(dateBalance).isNotEmpty();
            as.assertThat(dateBalance.size()).isEqualTo(2);
            as.assertThat(dateBalance.keySet()).containsExactlyInAnyOrderElementsOf(localDateList);
            as.assertThat(dateBalance.get(localDate1)).isEqualTo(1000.0d);
            as.assertThat(dateBalance.get(localDate2)).isEqualTo(-200.0d);
        });
    }

    @Test
    public void getDateBalance_accountNotFound() {
        SoftAssertions.assertSoftly(as -> {
            as.assertThatThrownBy(() -> bankService.getDateBalance(1))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("Account was not found for accountNumber 1.");

            verify(accountRepository, never()).save(any());
        });
    }

}
