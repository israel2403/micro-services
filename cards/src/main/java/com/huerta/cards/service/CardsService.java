package com.huerta.cards.service;

import com.huerta.cards.constans.CardsConstants;
import com.huerta.cards.dto.CardDTO;
import com.huerta.cards.entity.Card;
import com.huerta.cards.exceptions.CardAlreadyExistsException;
import com.huerta.cards.exceptions.CardWithMobileNumberNotFoundException;
import com.huerta.cards.repository.CardsRepository;
import com.huerta.cards.request.CardRequest;
import com.huerta.cards.request.UpdateCardRequest;
import com.huerta.cards.utils.GenerateCardNumber;
import com.huerta.cards.utils.Mapper;
import com.huerta.cards.utils.WrapperRequestAndEntity;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardsService {

  private static final Logger logger = LoggerFactory.getLogger(CardsService.class);

  private final CardsRepository cardsRepository;

  public CardDTO create(final CardRequest cardRequest) {
    logger.info("Creating card for mobile number: {}", cardRequest.getMobileNumber());
    return (CardDTO)
        this.cardsRepository
            .findByMobileNumber(cardRequest.getMobileNumber())
            .map(
                card -> {
                  logger.error(
                      "Card already exists for mobile number: {}", cardRequest.getMobileNumber());
                  throw new CardAlreadyExistsException("Card already exists");
                })
            .orElseGet(
                () -> {
                  final String cardNumber = GenerateCardNumber.generateCardNumber().toString();
                  Card newCard =
                      new Card(
                          cardNumber,
                          cardRequest.getMobileNumber(),
                          CardsConstants.CREDIT_CARD.getDescription(),
                          Long.parseLong(CardsConstants.NEW_CARD_LIMIT.getDescription()),
                          0L,
                          Long.parseLong(CardsConstants.NEW_CARD_LIMIT.getDescription()));
                  cardsRepository.save(newCard);
                  logger.info(
                      "Card created successfully for mobile number: {}",
                      cardRequest.getMobileNumber());
                  return Mapper.createCardDto.apply(newCard);
                });
  }

  /**
   * Fetches the card details for the customer with the given mobile number.
   *
   * <p>If the customer is not found, a {@link CardWithMobileNumberNotFoundException} will be
   * thrown.
   *
   * @param mobileNumber the mobile number of the customer
   * @return the card details
   * @throws CardWithMobileNumberNotFoundException if the customer is not found
   * @throws IllegalArgumentException if the mobile number is invalid
   */
  public CardDTO getByMobileNumber(final String mobileNumber) {
    if (!mobileNumber.matches("^[0-9]{10}$")) {
      logger.error("Invalid mobile number: {}", mobileNumber);
      throw new IllegalArgumentException("Invalid mobile number");
    }
    logger.info("Fetching card for mobile number: {}", mobileNumber);
    return this.cardsRepository
        .findByMobileNumber(mobileNumber)
        .map(card -> Mapper.createCardDto.apply(card))
        .orElseThrow(
            () -> {
              logger.error("Card not found for mobile number: {}", mobileNumber);
              return new CardWithMobileNumberNotFoundException(mobileNumber);
            });
  }

  public CardDTO update(final UpdateCardRequest cardRequest) {
    if (!cardRequest.getMobileNumber().matches("^[0-9]{10}$")) {
      logger.error("Invalid mobile number: {}", cardRequest.getMobileNumber());
      throw new IllegalArgumentException("Invalid mobile number");
    }
    logger.info("Updating card for mobile number: {}", cardRequest.getMobileNumber());
    return this.cardsRepository
        .findByMobileNumber(cardRequest.getMobileNumber())
        .map(card -> Mapper.updateCard.apply(new WrapperRequestAndEntity<>(cardRequest, card)))
        .map(card -> this.cardsRepository.save(card))
        .map(Mapper.createCardDto::apply)
        .orElseThrow(
            () -> {
              logger.error("Card not found for mobile number: {}", cardRequest.getMobileNumber());
              return new CardWithMobileNumberNotFoundException(cardRequest.getMobileNumber());
            });
  }

  public void delete(final String mobileNumber) {
    if (!mobileNumber.matches("^[0-9]{10}$")) {
      logger.error("Invalid mobile number: {}", mobileNumber);
      throw new IllegalArgumentException("Invalid mobile number");
    }
    logger.info("Deleting card for mobile number: {}", mobileNumber);
    this.cardsRepository
        .findByMobileNumber(mobileNumber)
        .ifPresentOrElse(
            card -> {
              this.cardsRepository.delete(card);
              logger.info("Card deleted successfully for mobile number: {}", mobileNumber);
            },
            () -> {
              logger.error("Card not found for mobile number: {}", mobileNumber);
              throw new CardWithMobileNumberNotFoundException(mobileNumber);
            });
  }
}
