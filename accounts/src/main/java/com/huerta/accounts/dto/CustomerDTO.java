package com.huerta.accounts.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO extends ResponseDTO {
  private String name;
  private String email;
  private String mobileNumber;

  public CustomerDTO(final String name, final String email, final String mobileNumber) {
    super("", "");
    this.name = name;
    this.email = email;
    this.mobileNumber = mobileNumber;
  }
}
