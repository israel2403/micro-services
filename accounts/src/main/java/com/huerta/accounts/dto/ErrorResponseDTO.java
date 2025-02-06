package com.huerta.accounts.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Builder
public class ErrorResponseDTO {
  private String apiPath;
  private HttpStatus errorCode;
  private String errorMessage;
  private LocalDateTime errorTime;
  private List<FieldErrorDTO> errors;
}
