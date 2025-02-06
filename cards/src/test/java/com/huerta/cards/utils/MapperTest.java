package com.huerta.cards.utils;

import static org.junit.jupiter.api.Assertions.*;

import com.huerta.cards.dto.CardDTO;
import com.huerta.cards.entity.Card;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class MapperTest {

  @Test
  public void testCreateCardDto() {
    Card card = new Card();
    card.setId(UUID.randomUUID());
    card.setMobileNumber("1234567890");
    card.setCardNumber("1234567890123456");
    card.setCardType("VISA");
    card.setTotalLimit(10000L);
    card.setAmountUsed(5000L);
    card.setAvailableAmount(5000L);
    card.setCreatedBy("user");
    card.setUpdatedBy("user");

    CardDTO cardDTO = Mapper.createCardDto.apply(card);

    assertNotNull(cardDTO);
    assertEquals(card.getCardNumber(), cardDTO.getCardNumber());
  }
}
