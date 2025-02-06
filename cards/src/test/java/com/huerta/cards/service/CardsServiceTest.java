package com.huerta.cards.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.huerta.cards.constans.CardsConstants;
import com.huerta.cards.dto.CardDTO;
import com.huerta.cards.entity.Card;
import com.huerta.cards.exceptions.CardAlreadyExistsException;
import com.huerta.cards.exceptions.CardWithMobileNumberNotFoundException;
import com.huerta.cards.repository.CardsRepository;
import com.huerta.cards.request.CardRequest;
import com.huerta.cards.request.UpdateCardRequest;
import com.huerta.cards.utils.GenerateCardNumber;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

public class CardsServiceTest {

  private static final String MOBILE_NUMBER = "1234567890";
  private static final String CARD_NUMBER = "1234567890123456";
  private static final String CARD_TYPE = "VISA";
  private static final long TOTAL_LIMIT = 20000L;
  private static final long AMOUNT_USED = 10000L;
  private static final long AVAILABLE_AMOUNT = 10000L;

  @Mock private CardsRepository cardsRepository;

  @InjectMocks private CardsService cardsService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCreateCardWhenCardAlreadyExists() {
    CardRequest cardRequest = createCardRequest();

    when(cardsRepository.findByMobileNumber(cardRequest.getMobileNumber()))
        .thenReturn(Optional.of(new Card()));

    assertThrows(
        CardAlreadyExistsException.class,
        () -> {
          cardsService.create(cardRequest);
        });

    verify(cardsRepository, never()).save(any(Card.class));
  }

  @Test
  public void testCreateCardWhenCardDoesNotExist() {
    CardRequest cardRequest = createCardRequest();

    when(cardsRepository.findByMobileNumber(cardRequest.getMobileNumber()))
        .thenReturn(Optional.empty());

    Card newCard = createNewCard(cardRequest);

    when(cardsRepository.save(any(Card.class))).thenReturn(newCard);

    try (MockedStatic<GenerateCardNumber> mockedStatic = mockStatic(GenerateCardNumber.class)) {
      mockedStatic
          .when(GenerateCardNumber::generateCardNumber)
          .thenReturn(Long.parseLong(CARD_NUMBER));
      CardDTO cardDTO = cardsService.create(cardRequest);

      assertCardDTOEqualsCard(cardDTO, newCard);

      verify(cardsRepository).save(any(Card.class));
    }
  }

  @Test
  public void testGetByMobileNumberWhenCardExists() {
    Card card = createExistingCard();

    when(cardsRepository.findByMobileNumber(MOBILE_NUMBER)).thenReturn(Optional.of(card));

    CardDTO cardDTO = cardsService.getByMobileNumber(MOBILE_NUMBER);

    assertCardDTOEqualsCard(cardDTO, card);

    verify(cardsRepository).findByMobileNumber(MOBILE_NUMBER);
  }

  @Test
  public void testGetByMobileNumberWhenCardDoesNotExist() {
    when(cardsRepository.findByMobileNumber(MOBILE_NUMBER)).thenReturn(Optional.empty());

    assertThrows(
        CardWithMobileNumberNotFoundException.class,
        () -> {
          cardsService.getByMobileNumber(MOBILE_NUMBER);
        });

    verify(cardsRepository).findByMobileNumber(MOBILE_NUMBER);
  }

  @Test
  public void testGetByMobileNumberWithInvalidMobileNumber() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          cardsService.getByMobileNumber("Invalid mobile number");
        });
  }

  @Test
  public void testUpdateCardWhenCardExists() {
    UpdateCardRequest cardRequest = createUpdateCardRequest();

    Card existingCard = createExistingCard();

    when(cardsRepository.findByMobileNumber(cardRequest.getMobileNumber()))
        .thenReturn(Optional.of(existingCard));

    Card updatedCard = createUpdatedCard(cardRequest);

    when(cardsRepository.save(any(Card.class))).thenReturn(updatedCard);

    CardDTO cardDTO = cardsService.update(cardRequest);

    assertCardDTOEqualsCard(cardDTO, updatedCard);

    verify(cardsRepository).save(any(Card.class));
  }

  @Test
  public void testUpdateCardWithInvalidMobileNumber() {
    UpdateCardRequest cardRequest = createUpdateCardRequest();
    cardRequest.setMobileNumber("Invalid mobile number");
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          cardsService.update(cardRequest);
        });
  }

  @Test
  public void testUpdateCardWhenCardDoesNotExist() {
    UpdateCardRequest cardRequest = createUpdateCardRequest();

    when(cardsRepository.findByMobileNumber(cardRequest.getMobileNumber()))
        .thenReturn(Optional.empty());

    assertThrows(
        CardWithMobileNumberNotFoundException.class,
        () -> {
          cardsService.update(cardRequest);
        });

    verify(cardsRepository).findByMobileNumber(cardRequest.getMobileNumber());
  }

  @Test
  public void testDeleteCardWhenCardExists() {
    Card card = createExistingCard();

    when(cardsRepository.findByMobileNumber(MOBILE_NUMBER)).thenReturn(Optional.of(card));

    cardsService.delete(MOBILE_NUMBER);

    verify(cardsRepository).delete(card);
  }

  @Test
  public void testDeleteWithInvalidMobileNumber() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          cardsService.delete("Invalid mobile number");
        });
  }

  @Test
  public void testDeleteCardWhenCardDoesNotExist() {
    when(cardsRepository.findByMobileNumber(MOBILE_NUMBER)).thenReturn(Optional.empty());

    assertThrows(
        CardWithMobileNumberNotFoundException.class,
        () -> {
          cardsService.delete(MOBILE_NUMBER);
        });

    verify(cardsRepository).findByMobileNumber(MOBILE_NUMBER);
  }

  private CardRequest createCardRequest() {
    CardRequest cardRequest = new CardRequest();
    cardRequest.setMobileNumber(MOBILE_NUMBER);
    return cardRequest;
  }

  private Card createNewCard(CardRequest cardRequest) {
    Card newCard = new Card();
    newCard.setMobileNumber(cardRequest.getMobileNumber());
    newCard.setCardNumber(CARD_NUMBER);
    newCard.setCardType(CardsConstants.CREDIT_CARD.getDescription());
    newCard.setTotalLimit(Long.parseLong(CardsConstants.NEW_CARD_LIMIT.getDescription()));
    newCard.setAmountUsed(0L);
    newCard.setAvailableAmount(Long.parseLong(CardsConstants.NEW_CARD_LIMIT.getDescription()));
    return newCard;
  }

  private Card createExistingCard() {
    Card card = new Card();
    card.setMobileNumber(MOBILE_NUMBER);
    card.setCardNumber(CARD_NUMBER);
    card.setCardType(CardsConstants.CREDIT_CARD.getDescription());
    card.setTotalLimit(10000L);
    card.setAmountUsed(5000L);
    card.setAvailableAmount(5000L);
    return card;
  }

  private UpdateCardRequest createUpdateCardRequest() {
    UpdateCardRequest cardRequest = new UpdateCardRequest();
    cardRequest.setMobileNumber(MOBILE_NUMBER);
    cardRequest.setCardType(CARD_TYPE);
    cardRequest.setTotalLimit(TOTAL_LIMIT);
    cardRequest.setAmountUsed(AMOUNT_USED);
    cardRequest.setAvailableAmount(AVAILABLE_AMOUNT);
    return cardRequest;
  }

  private Card createUpdatedCard(UpdateCardRequest cardRequest) {
    Card updatedCard = new Card();
    updatedCard.setMobileNumber(cardRequest.getMobileNumber());
    updatedCard.setCardType(cardRequest.getCardType());
    updatedCard.setTotalLimit(cardRequest.getTotalLimit());
    updatedCard.setAmountUsed(cardRequest.getAmountUsed());
    updatedCard.setAvailableAmount(cardRequest.getAvailableAmount());
    return updatedCard;
  }

  private void assertCardDTOEqualsCard(CardDTO cardDTO, Card card) {
    assertNotNull(cardDTO);
    assertEquals(card.getMobileNumber(), cardDTO.getMobileNumber());
    assertEquals(card.getCardNumber(), cardDTO.getCardNumber());
    assertEquals(card.getCardType(), cardDTO.getCardType());
    assertEquals(card.getTotalLimit(), cardDTO.getTotalLimit());
    assertEquals(card.getAmountUsed(), cardDTO.getAmountUsed());
    assertEquals(card.getAvailableAmount(), cardDTO.getAvailableAmount());
  }
}
