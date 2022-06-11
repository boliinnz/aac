package net.contal.demo.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author Bo Li
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table
public class BankTransaction {
    @Id
    @GeneratedValue
    @JsonIgnore
    private long id;

    @ManyToOne
    @JsonIgnore
    private CustomerAccount customerAccount;

    private double transactionAmount;
    private LocalDate transactionDate;

    @PrePersist
    @PreUpdate
    public void prePersist() {
        this.setTransactionDate(LocalDate.now());
    }
}
