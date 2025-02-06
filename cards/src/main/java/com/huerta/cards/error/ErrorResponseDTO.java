package com.huerta.cards.error;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Builder
@Schema(description = "Error response object")
public class ErrorResponseDTO {

  @Schema(description = "API path that caused the error")
  private String apiPath;

  @Schema(description = "Error code")
  private HttpStatus errorCode;

  @Schema(description = "Error message")
  private String errorMessage;

  @Schema(description = "Error time")
  private LocalDateTime errorTime;

  @Schema(description = "List of field errors")
  private List<FieldErrorDTO> errors;
}
