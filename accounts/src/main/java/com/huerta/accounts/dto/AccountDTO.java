package com.huerta.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "AccountDTO", description = "Details of an account")
public class AccountDTO extends ResponseDTO {

  private int accountNumber;
  private String accountType;
  private String branchAddress;

  public AccountDTO(
      final String statusCode,
      final String statusMessage,
      final int accountNumber,
      final String accountType,
      final String branchAddress) {
    super(statusCode, statusMessage);
    this.accountNumber = accountNumber;
    this.accountType = accountType;
    this.branchAddress = branchAddress;
  }
}
