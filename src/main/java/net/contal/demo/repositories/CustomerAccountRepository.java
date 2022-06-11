package net.contal.demo.repositories;

import net.contal.demo.modal.CustomerAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Bo Li
 */
public interface CustomerAccountRepository extends JpaRepository<CustomerAccount, Integer>{
    Optional<CustomerAccount> findByAccountNumber(Integer accountNumber);
}
