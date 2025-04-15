package com.huerta.cards.controller;

import com.huerta.cards.constans.ErrorConstants;
import com.huerta.cards.dto.CardDTO;
import com.huerta.cards.error.ErrorResponseDTO;
import com.huerta.cards.request.CardRequest;
import com.huerta.cards.service.CardsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
    name = "CRUD REST API for cards",
    description = "Operations for cards(create, read, update, delete)")
@RestController
@RequestMapping(
    path = "api/v1/cards",
    produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
public class CardController {

  private static final Logger logger = LoggerFactory.getLogger(CardController.class);

  private final CardsService cardsService;

  /**
   * Create a new card with the given request data.
   *
   * @param cardRequest request data for the new card
   * @return a ResponseEntity containing the created card
   */
  @Operation(
      summary = "Create a new card",
      description = "Create a new card with the given request data",
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Request data for creating a new card",
              required = true,
              content = @Content(schema = @Schema(implementation = CardRequest.class))))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Card created successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CardDTO.class))),
        @ApiResponse(
            responseCode = "409",
            description = "Card already exists",
            content =
                @Content(
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = {
                      @io.swagger.v3.oas.annotations.media.ExampleObject(
                          name = "Card already exists",
                          value = ErrorConstants.CARD_ALREADY_EXISTS_ERROR)
                    })),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data",
            content =
                @Content(
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = {
                      @io.swagger.v3.oas.annotations.media.ExampleObject(
                          name = "Invalid request data",
                          value = ErrorConstants.INVALID_CARD_PHONE_NUMBER_REQUEST)
                    }))
      })
  @PostMapping
  public ResponseEntity<CardDTO> create(@Valid @RequestBody final CardRequest cardRequest) {
    logger.info(
        "Received request to create card for mobile number: {}", cardRequest.getMobileNumber());
    CardDTO cardsDTO = this.cardsService.create(cardRequest);
    logger.info("Card created successfully for mobile number: {}", cardRequest.getMobileNumber());
    return ResponseEntity.status(201).body(cardsDTO);
  }

  /**
   * Get the card details for the customer with the given mobile number.
   *
   * @param mobileNumber the mobile number of the customer
   * @return a ResponseEntity containing the card details
   */
  @Operation(
      summary = "Get card details",
      description = "Get the card details for the customer with the given mobile number",
      parameters =
          @Parameter(
              name = "mobileNumber",
              description = "Mobile number of the customer",
              required = true,
              schema = @Schema(pattern = "^[0-9]{10}$"),
              example = "1234567890"))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Card details found successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CardDTO.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Card not found",
            content =
                @Content(
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = {
                      @io.swagger.v3.oas.annotations.media.ExampleObject(
                          name = "Card not found",
                          value = ErrorConstants.CARD_NOT_FOUND_ERROR)
                    })),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid mobile number",
            content =
                @Content(
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = {
                      @io.swagger.v3.oas.annotations.media.ExampleObject(
                          name = "Invalid mobile number",
                          value = ErrorConstants.INVALID_CARD_PHONE_NUMBER)
                    }))
      })
  @GetMapping("{mobileNumber}")
  public ResponseEntity<CardDTO> getByMobileNumber(@Valid @PathVariable final String mobileNumber) {
    logger.info("Received request to get card for mobile number: {}", mobileNumber);
    CardDTO cardsDTO = this.cardsService.getByMobileNumber(mobileNumber);
    logger.info("Card found successfully for mobile number: {}", mobileNumber);
    return ResponseEntity.ok(cardsDTO);
  }
}
