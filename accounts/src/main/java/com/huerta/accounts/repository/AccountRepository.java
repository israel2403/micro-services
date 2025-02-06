package com.huerta.accounts.repository;

import com.huerta.accounts.entities.Account;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, UUID> {

  Optional<Account> findByAccountNumber(Integer accountNumber);
}
