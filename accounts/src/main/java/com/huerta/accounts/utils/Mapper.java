package com.huerta.accounts.utils;

import com.huerta.accounts.dto.AccountDTOWithoutResponse;
import com.huerta.accounts.dto.AccountWithCustomerDTO;
import com.huerta.accounts.dto.CustomerDTOWithoutResponse;
import com.huerta.accounts.entities.Account;
import com.huerta.accounts.entities.Customer;
import java.util.function.Function;

public interface Mapper {
  public static AccountDTOWithoutResponse fromAccountToAccountDTOWithoutResponse(Account account) {
    return AccountDTOWithoutResponse.builder()
        .accountNumber(account.getAccountNumber())
        .accountType(account.getAccountType())
        .branchAddress(account.getBranchAddress())
        .build();
  }

  public static CustomerDTOWithoutResponse fromCustomerToCustomerDTOWithoutResponse(
      Customer customer) {
    return CustomerDTOWithoutResponse.builder()
        .name(customer.getName())
        .email(customer.getEmail())
        .mobileNumber(customer.getMobileNumber())
        .build();
  }

  Function<Customer, AccountWithCustomerDTO> fromCustomerToAccountWithCustomerDTO =
      customer -> {
        AccountDTOWithoutResponse accountDTOWithoutResponse =
            fromAccountToAccountDTOWithoutResponse(customer.getAccount());
        CustomerDTOWithoutResponse customerDTOWithoutResponse =
            fromCustomerToCustomerDTOWithoutResponse(customer);
        return AccountWithCustomerDTO.builder()
            .account(accountDTOWithoutResponse)
            .customer(customerDTOWithoutResponse)
            .build();
      };

  Function<Account, AccountWithCustomerDTO> fromAccountToAccountWithCustomerDTO =
      account -> {
        AccountDTOWithoutResponse accountDTOWithoutResponse =
            fromAccountToAccountDTOWithoutResponse(account);
        CustomerDTOWithoutResponse customerDTOWithoutResponse =
            fromCustomerToCustomerDTOWithoutResponse(account.getCustomer());
        return AccountWithCustomerDTO.builder()
            .account(accountDTOWithoutResponse)
            .customer(customerDTOWithoutResponse)
            .build();
      };
}
