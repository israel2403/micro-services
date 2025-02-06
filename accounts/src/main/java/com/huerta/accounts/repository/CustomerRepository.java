package com.huerta.accounts.repository;

import com.huerta.accounts.entities.Customer;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

  Optional<Customer> findByMobileNumber(String mobileNumber);
}
