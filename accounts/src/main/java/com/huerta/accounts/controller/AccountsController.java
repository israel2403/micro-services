package com.huerta.accounts.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.huerta.accounts.config.messages.SuccessMessagesConfig;
import com.huerta.accounts.dto.AccountDTO;
import com.huerta.accounts.dto.AccountWithCustomerDTO;
import com.huerta.accounts.dto.ErrorResponseDTO;
import com.huerta.accounts.exception.CustomerAlreadyExistsException;
import com.huerta.accounts.exception.ResourceNotFoundException;
import com.huerta.accounts.request.AccountRequest;
import com.huerta.accounts.request.CustomerRequest;
import com.huerta.accounts.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;

@Tag(
    name = "CRUD REST APIs for accounts",
    description = "Operations related to accounts (create, read, update, delete)")
@RestController
@RequestMapping(
    path = "/api/v1/accounts",
    produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
public class AccountsController {

  private static final Logger logger = LoggerFactory.getLogger(AccountsController.class);

  private final SuccessMessagesConfig successMessagesConfig;

  private final AccountService accountService;

  /**
   * Creates a new account with the given customer details.
   *
   * <p>If a customer with the same mobile number already exists, a {@link
   * CustomerAlreadyExistsException} will be thrown.
   *
   * @param customerRequest the customer details
   * @return the created account
   * @throws CustomerAlreadyExistsException if a customer with the same mobile number already exists
   */
  @Operation(
      summary = "Create a new account",
      description = "Create a new account with the given customer details",
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Customer details for creating a new account",
              required = true,
              content = @Content(schema = @Schema(implementation = CustomerRequest.class))))
  @ApiResponse(
      responseCode = "201",
      description = "Account created successfully",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = AccountDTO.class)))
  @ApiResponse(responseCode = "400", description = "Bad request")
  @ApiResponse(
      responseCode = "500",
      description = "Internal server error",
      content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
  @PostMapping
  public ResponseEntity<AccountDTO> createAccount(
      @Valid @RequestBody final CustomerRequest customerRequest) {
    logger.info(
        "Received request to create account for customer with mobile number: {}",
        customerRequest.getMobileNumber());
    AccountDTO accountDTO = accountService.createAccount(customerRequest);
    accountDTO.setStatusCode(HttpStatus.CREATED.toString());
    accountDTO.setStatusMessage(successMessagesConfig.getMESSAGE_201());
    logger.info(
        "Account created successfully for customer with mobile number: {}",
        customerRequest.getMobileNumber());
    logger.info("Response: {}", accountDTO);
    return ResponseEntity.status(201).body(accountDTO);
  }

  /**
   * Gets the account details of the customer with the given mobile number.
   *
   * <p>The response will be a JSON object containing the account details.
   *
   * @param mobileNumber the mobile number of the customer
   * @return the account with customer details
   * @throws ResourceNotFoundException if the customer with the given mobile number is not found
   */
  @GetMapping("/details")
  public ResponseEntity<AccountWithCustomerDTO> getAccountDetails(
      @RequestParam @Pattern(regexp = "^$|[0-9]{10}", message = "Mobile number must be 10 digits")
          final String mobileNumber) {
    logger.info(
        "Received request to get account details for customer with mobile number: {}",
        mobileNumber);
    AccountWithCustomerDTO accountDTO = accountService.getAccountDetails(mobileNumber);
    accountDTO.setStatusCode(HttpStatus.OK.toString());
    accountDTO.setStatusMessage(successMessagesConfig.getMESSAGE_200());
    logger.info("Response: {}", accountDTO);
    return ResponseEntity.status(200).body(accountDTO);
  }

  @PutMapping
  public ResponseEntity<AccountWithCustomerDTO> updateAccount(
      @Valid @RequestBody final AccountRequest accountRequest) {
    logger.info(
        "Received request to update account for customer with mobile number: {}",
        accountRequest.getMobileNumber());
    AccountWithCustomerDTO accountDTO = accountService.updateAccount(accountRequest);
    accountDTO.setStatusCode(HttpStatus.OK.toString());
    accountDTO.setStatusMessage(successMessagesConfig.getMESSAGE_200());
    logger.info(
        "Account updated successfully for customer with mobile number: {}",
        accountRequest.getMobileNumber());
    logger.info("Response: {}", accountDTO);
    return ResponseEntity.status(200).body(accountDTO);
  }

  @DeleteMapping
  public ResponseEntity<Void> removeAccountAndCustomer(
      @RequestParam @Pattern(regexp = "^$|[0-9]{10}", message = "Mobile number must be 10 digits")
          final String mobileNumber) {
    logger.info(
        "Received request to delete account and customer with mobile number: {}", mobileNumber);
    accountService.deleteAccountAndCustomer(mobileNumber);
    return ResponseEntity.status(200).build();
  }
}
