package com.huerta.cards.repository;

import com.huerta.cards.entity.Card;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardsRepository extends JpaRepository<Card, UUID> {

  Optional<Card> findByMobileNumber(String mobileNumber);

  Optional<Card> findByCardNumber(String cardNumber);
}
