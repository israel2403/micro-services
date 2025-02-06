package com.huerta.cards.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(name = "Card request", description = "Schema to hold card information")
public class CardRequest {

  @Schema(description = "Mobile number", example = "1234567890")
  @NotEmpty(message = "Mobile number is required")
  @Pattern(regexp = "^$|[0-9]{10}", message = "Mobile number must be 10 digits")
  private String mobileNumber;
}
