package com.huerta.cards.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@Schema(description = "Field error")
public class FieldErrorDTO {

  @Schema(description = "Field name")
  private String field;

  @Schema(description = "Error message")
  private String message;
}
