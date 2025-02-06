package com.huerta.accounts.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccountWithCustomerDTO extends ResponseDTO {
  private CustomerDTOWithoutResponse customer;
  private AccountDTOWithoutResponse account;

  public AccountWithCustomerDTO(
      final CustomerDTOWithoutResponse customer, final AccountDTOWithoutResponse account) {
    super("", "");
    this.customer = customer;
    this.account = account;
  }

  public AccountWithCustomerDTO() {
    super("", "");
  }
}
