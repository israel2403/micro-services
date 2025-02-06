package com.huerta.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public abstract class ResponseDTO {
  private String statusCode;
  private String statusMessage;
}
