package com.huerta.cards.utils;

import com.huerta.cards.dto.CardDTO;
import com.huerta.cards.entity.Card;
import com.huerta.cards.request.UpdateCardRequest;
import java.util.function.Function;

public interface Mapper {

  Function<Card, CardDTO> createCardDto = CardDTO::new;

  Function<WrapperRequestAndEntity<UpdateCardRequest, Card>, Card> updateCard =
      wrapper -> {
        Card card = wrapper.getEntity();
        UpdateCardRequest request = wrapper.getRequest();
        card.setMobileNumber(request.getMobileNumber());
        card.setCardType(request.getCardType());
        card.setTotalLimit(request.getTotalLimit());
        card.setAmountUsed(request.getAmountUsed());
        card.setAvailableAmount(request.getAvailableAmount());
        return card;
      };
}
