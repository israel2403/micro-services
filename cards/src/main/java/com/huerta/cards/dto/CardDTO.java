package com.huerta.cards.dto;

import com.huerta.cards.entity.Card;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(name = "CardDTO", description = "Data Transfer Object (DTO) for a card")
public class CardDTO {

  @Schema(description = "Card number", example = "1234567890123456")
  private String cardNumber;

  @Schema(description = "Card type", example = "Credit Card")
  private String cardType;

  @Schema(description = "Mobile number", example = "1234567890")
  private String mobileNumber;

  @Schema(description = "Total limit", example = "20000")
  private Long totalLimit;

  @Schema(description = "Amount used", example = "1000")
  private Long amountUsed;

  @Schema(description = "Available amount", example = "19000")
  private Long availableAmount;

  public CardDTO(Card card) {
    this.cardNumber = card.getCardNumber();
    this.cardType = card.getCardType();
    this.mobileNumber = card.getMobileNumber();
    this.totalLimit = card.getTotalLimit();
    this.amountUsed = card.getAmountUsed();
    this.availableAmount = card.getAvailableAmount();
  }
}
