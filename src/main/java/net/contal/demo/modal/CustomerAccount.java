package net.contal.demo.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bo Li
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"accountNumber"})})
public class CustomerAccount {
    @Id
    @GeneratedValue
    @JsonIgnore
    private long id;
    @OneToMany(cascade = CascadeType.ALL)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<BankTransaction> transactions;

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;

    private int accountNumber;
    private double accountBalance;

    public void addTransaction(BankTransaction newTransaction) {
        if (transactions == null) {
            transactions = new ArrayList<>();
        }
        newTransaction.setCustomerAccount(this);
        transactions.add(newTransaction);
    }

    public void addAccountBalance (double accountBalance) {
        this.accountBalance += accountBalance;
    }
    public void prePersistOrUpdate() {
        if (this.getTransactions() != null) {
            this.setAccountBalance(this.getTransactions().stream().mapToDouble(BankTransaction :: getTransactionAmount).sum());
        }
    }
}
