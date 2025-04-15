package com.huerta.cards.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "Update card request", description = "Schema to hold card information")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UpdateCardRequest {

  private String mobileNumber;

  private String cardNumber;

  private String cardType;

  private Long totalLimit;

  private Long amountUsed;

  private Long availableAmount;
}
