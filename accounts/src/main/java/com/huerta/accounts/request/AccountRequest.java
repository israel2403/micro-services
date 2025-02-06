package com.huerta.accounts.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountRequest {

  @NotEmpty(message = "Name is required")
  private String name;

  @NotEmpty(message = "Email is required")
  @Email(message = "Email must be a valid email address")
  private String email;

  @NotEmpty(message = "Mobile number is required")
  @Pattern(regexp = "^$|[0-9]{10}", message = "Mobile number must be 10 digits")
  private String mobileNumber;

  @NotNull(message = "Account number is required")
  @Pattern(regexp = "^$|[0-9]{10}", message = "Account number must be 10 digits")
  private String accountNumber;

  @NotEmpty(message = "Account type is required")
  private String accountType;

  @NotEmpty(message = "Branch name is required")
  private String branchAddress;
}
