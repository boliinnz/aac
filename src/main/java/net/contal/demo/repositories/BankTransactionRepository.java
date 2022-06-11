package net.contal.demo.repositories;

import net.contal.demo.modal.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Bo Li
 */
public interface BankTransactionRepository extends JpaRepository<BankTransaction, Integer>{
    List<BankTransaction> findTop10ByCustomerAccount_AccountNumberOrderByIdDesc(int accountNumber);
}
