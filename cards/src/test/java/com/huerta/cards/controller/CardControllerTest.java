package com.huerta.cards.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huerta.cards.dto.CardDTO;
import com.huerta.cards.exceptions.CardWithMobileNumberNotFoundException;
import com.huerta.cards.request.CardRequest;
import com.huerta.cards.service.CardsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class CardControllerTest {

  private MockMvc mockMvc;

  @Mock private CardsService cardsService;

  @InjectMocks private CardController cardController;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(cardController).build();
  }

  @Test
  public void testCreateCard() throws Exception {
    CardRequest cardRequest = new CardRequest();
    cardRequest.setMobileNumber("1234567890");

    CardDTO cardDTO = new CardDTO();
    cardDTO.setMobileNumber("1234567890");

    when(cardsService.create(any(CardRequest.class))).thenReturn(cardDTO);

    mockMvc
        .perform(
            post("/api/v1/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(cardRequest)))
        .andExpect(status().isCreated());
  }

  @Test
  public void testGetByMobileNumberWhenCardExists() throws Exception {
    String mobileNumber = "1234567890";

    CardDTO cardDTO = new CardDTO();
    cardDTO.setMobileNumber(mobileNumber);

    when(cardsService.getByMobileNumber(mobileNumber)).thenReturn(cardDTO);

    mockMvc
        .perform(get("/api/v1/cards/{mobileNumber}", mobileNumber))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.mobileNumber").value(mobileNumber));
  }

  @Test
  public void testGetByMobileNumberWhenCardDoesNotExist() throws Exception {
    String mobileNumber = "1234567890";

    when(cardsService.getByMobileNumber(mobileNumber))
        .thenThrow(new CardWithMobileNumberNotFoundException(mobileNumber));

    mockMvc
        .perform(get("/api/v1/cards/{mobileNumber}", mobileNumber))
        .andExpect(status().isNotFound());
  }
}
