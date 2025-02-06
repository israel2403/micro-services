package com.huerta.cards.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Card {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", columnDefinition = "UUID")
  private UUID id;

  @Column(name = "mobile_number", nullable = false, length = 15)
  private String mobileNumber;

  @Column(name = "card_number", nullable = false, length = 100)
  private String cardNumber;

  @Column(name = "card_type", nullable = false, length = 100)
  private String cardType;

  @Column(name = "total_limit", nullable = false)
  private Long totalLimit;

  @Column(name = "amount_used", nullable = false)
  private Long amountUsed;

  @Column(name = "available_amount", nullable = false)
  private Long availableAmount;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "created_by", nullable = false, length = 20)
  private String createdBy;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "updated_by", length = 20)
  private String updatedBy;

  public Card(
      final String cardNumber,
      final String mobileNumber,
      final String cardType,
      final Long totalLimit,
      final Long amountUsed,
      final Long availableAmount) {
    this.cardNumber = cardNumber;
    this.mobileNumber = mobileNumber;
    this.cardType = cardType;
    this.totalLimit = totalLimit;
    this.amountUsed = amountUsed;
    this.availableAmount = availableAmount;
    this.createdAt = LocalDateTime.now();
    this.createdBy = "system";
    this.updatedAt = null;
    this.updatedBy = null;
  }
}
