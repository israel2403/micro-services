package com.huerta.cards.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UpdateCardRequest {

  private String mobileNumber;

  private String cardType;

  private Long totalLimit;

  private Long amountUsed;

  private Long availableAmount;
}
