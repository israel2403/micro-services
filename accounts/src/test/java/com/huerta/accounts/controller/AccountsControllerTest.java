package com.huerta.accounts.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.huerta.accounts.config.messages.SuccessMessagesConfig;
import com.huerta.accounts.dto.AccountDTO;
import com.huerta.accounts.dto.AccountDTOWithoutResponse;
import com.huerta.accounts.dto.AccountWithCustomerDTO;
import com.huerta.accounts.dto.CustomerDTOWithoutResponse;
import com.huerta.accounts.exception.ResourceNotFoundException;
import com.huerta.accounts.request.AccountRequest;
import com.huerta.accounts.request.CustomerRequest;
import com.huerta.accounts.service.AccountService;

@ExtendWith(MockitoExtension.class)
public class AccountsControllerTest {

  @Mock private SuccessMessagesConfig successMessagesConfig;

  @Mock private AccountService accountService;

  @InjectMocks private AccountsController accountsController;

  private CustomerRequest customerRequest;
  private AccountDTO accountDTO;
  private AccountWithCustomerDTO accountWithCustomerDTO;

  @BeforeEach
  public void setUp() {
    customerRequest = new CustomerRequest();
    customerRequest.setMobileNumber("1234567890");

    accountDTO = new AccountDTO("", "", 1234567890, "SAVINGS", "ADDRESS");

    AccountDTOWithoutResponse accountDTOWithoutResponse = new AccountDTOWithoutResponse();
    accountDTOWithoutResponse.setAccountNumber(1234567890);
    accountDTOWithoutResponse.setAccountType("SAVINGS");
    accountDTOWithoutResponse.setBranchAddress("ADDRESS");

    CustomerDTOWithoutResponse customerDTOWithoutResponse = new CustomerDTOWithoutResponse();
    customerDTOWithoutResponse.setName("John Doe");
    customerDTOWithoutResponse.setEmail("john.doe@example.com");
    customerDTOWithoutResponse.setMobileNumber("1234567890");

    accountWithCustomerDTO =
        AccountWithCustomerDTO.builder()
            .account(accountDTOWithoutResponse)
            .customer(customerDTOWithoutResponse)
            .build();
  }

  @Test
  public void testCreateAccount() {
    when(accountService.createAccount(any(CustomerRequest.class))).thenReturn(accountDTO);
    when(successMessagesConfig.getMESSAGE_201()).thenReturn("Account created successfully");

    ResponseEntity<AccountDTO> response = accountsController.createAccount(customerRequest);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Account created successfully", response.getBody().getStatusMessage());
    assertEquals("201 CREATED", response.getBody().getStatusCode());
    assertEquals(accountDTO.getAccountNumber(), response.getBody().getAccountNumber());
    assertEquals(accountDTO.getAccountType(), response.getBody().getAccountType());
    assertEquals(accountDTO.getBranchAddress(), response.getBody().getBranchAddress());
  }

  @Test
  public void testGetAccountDetails() {
    when(accountService.getAccountDetails(anyString())).thenReturn(accountWithCustomerDTO);
    when(successMessagesConfig.getMESSAGE_200()).thenReturn("Account details fetched successfully");

    ResponseEntity<AccountWithCustomerDTO> response =
        accountsController.getAccountDetails("1234567890");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Account details fetched successfully", response.getBody().getStatusMessage());
    assertEquals("200 OK", response.getBody().getStatusCode());
    assertEquals(
        accountWithCustomerDTO.getAccount().getAccountNumber(),
        response.getBody().getAccount().getAccountNumber());
    assertEquals(
        accountWithCustomerDTO.getAccount().getAccountType(),
        response.getBody().getAccount().getAccountType());
    assertEquals(
        accountWithCustomerDTO.getAccount().getBranchAddress(),
        response.getBody().getAccount().getBranchAddress());
    assertEquals(
        accountWithCustomerDTO.getCustomer().getName(), response.getBody().getCustomer().getName());
    assertEquals(
        accountWithCustomerDTO.getCustomer().getEmail(),
        response.getBody().getCustomer().getEmail());
    assertEquals(
        accountWithCustomerDTO.getCustomer().getMobileNumber(),
        response.getBody().getCustomer().getMobileNumber());
  }

  @Test
  public void testUpdateAccount_Success() {
    AccountRequest accountRequest = new AccountRequest();
    accountRequest.setAccountNumber("1234567890");
    accountRequest.setAccountType("CURRENT");
    accountRequest.setBranchAddress("NEW_ADDRESS");
    accountRequest.setMobileNumber("0987654321");

    when(accountService.updateAccount(any(AccountRequest.class)))
        .thenReturn(accountWithCustomerDTO);
    when(successMessagesConfig.getMESSAGE_200()).thenReturn("Account updated successfully");

    ResponseEntity<AccountWithCustomerDTO> response =
        accountsController.updateAccount(accountRequest);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Account updated successfully", response.getBody().getStatusMessage());
    assertEquals("200 OK", response.getBody().getStatusCode());
    assertEquals(
        accountWithCustomerDTO.getAccount().getAccountNumber(),
        response.getBody().getAccount().getAccountNumber());
    assertEquals(
        accountWithCustomerDTO.getAccount().getAccountType(),
        response.getBody().getAccount().getAccountType());
    assertEquals(
        accountWithCustomerDTO.getAccount().getBranchAddress(),
        response.getBody().getAccount().getBranchAddress());
    assertEquals(
        accountWithCustomerDTO.getCustomer().getName(), response.getBody().getCustomer().getName());
    assertEquals(
        accountWithCustomerDTO.getCustomer().getEmail(),
        response.getBody().getCustomer().getEmail());
    assertEquals(
        accountWithCustomerDTO.getCustomer().getMobileNumber(),
        response.getBody().getCustomer().getMobileNumber());
  }

  @Test
  public void testDeleteAccountAndCustomer_Success() {
    doNothing().when(accountService).deleteAccountAndCustomer(anyString());

    ResponseEntity<Void> response = accountsController.removeAccountAndCustomer("1234567890");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNull(response.getBody());
    verify(accountService, times(1)).deleteAccountAndCustomer("1234567890");
  }

  @Test
  public void testDeleteAccountAndCustomer_CustomerNotFound() {
    doThrow(new ResourceNotFoundException("Customer", "mobileNumber", "1234567890"))
        .when(accountService)
        .deleteAccountAndCustomer(anyString());

    ResourceNotFoundException exception =
        assertThrows(
            ResourceNotFoundException.class,
            () -> {
              accountsController.removeAccountAndCustomer("1234567890");
            });

    assertEquals(
        "Customer not found with the given input data mobileNumber : '1234567890'",
        exception.getMessage());
    verify(accountService, times(1)).deleteAccountAndCustomer("1234567890");
  }

  @Test
  public void testDeleteAccountAndCustomer_AccountNotFound() {
    doThrow(new ResourceNotFoundException("Account", "mobileNumber", "1234567890"))
        .when(accountService)
        .deleteAccountAndCustomer(anyString());

    ResourceNotFoundException exception =
        assertThrows(
            ResourceNotFoundException.class,
            () -> {
              accountsController.removeAccountAndCustomer("1234567890");
            });

    assertEquals(
        "Account not found with the given input data mobileNumber : '1234567890'",
        exception.getMessage());
    verify(accountService, times(1)).deleteAccountAndCustomer("1234567890");
  }
}
