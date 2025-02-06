package com.huerta.accounts.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.huerta.accounts.config.messages.DataMessagesConfig;
import com.huerta.accounts.config.messages.ErrorMessagesConfig;
import com.huerta.accounts.dto.AccountDTO;
import com.huerta.accounts.dto.AccountDTOWithoutResponse;
import com.huerta.accounts.dto.AccountWithCustomerDTO;
import com.huerta.accounts.dto.CustomerDTOWithoutResponse;
import com.huerta.accounts.entities.Account;
import com.huerta.accounts.entities.Customer;
import com.huerta.accounts.exception.CustomerAlreadyExistsException;
import com.huerta.accounts.exception.ResourceNotFoundException;
import com.huerta.accounts.repository.AccountRepository;
import com.huerta.accounts.repository.CustomerRepository;
import com.huerta.accounts.request.AccountRequest;
import com.huerta.accounts.request.CustomerRequest;
import com.huerta.accounts.utils.GenerateAccountNumber;
import com.huerta.accounts.utils.Mapper;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
/**
 * We use MockitoExtension to inject mocks annotated with @Mock and @InjectMocks, and to initialize
 * the mocks before each test.
 *
 * <p>The @MockitoSettings annotation is used to specify the strictness of Mockito. In this case, we
 * use Strictness.LENIENT, which means that Mockito will not perform any additional checks (such as
 * checking that a mock is not null before calling a method on it).
 */
@MockitoSettings(strictness = Strictness.LENIENT)
public class AccountServiceTest {

  @Mock private AccountRepository accountRepository;

  @Mock private CustomerRepository customerRepository;

  @Mock private DataMessagesConfig dataMessagesConfig;

  @Mock private ErrorMessagesConfig errorMessagesConfig;

  @InjectMocks private AccountService accountService;

  private CustomerRequest customerRequest;
  private Customer customer;
  private Account account;

  @BeforeEach
  public void setUp() {
    customerRequest = new CustomerRequest();
    customerRequest.setMobileNumber("1234567890");

    customer = new Customer(customerRequest);
    account = new Account(customer);
    account.setAccountNumber(1234567890); // Use a consistent value for testing
    account.setAccountType("SAVINGS");
    account.setBranchAddress("ADDRESS");

    // Set the account in the customer
    customer.setAccount(account);

    when(dataMessagesConfig.getSavings()).thenReturn("SAVINGS");
    when(dataMessagesConfig.getAddress()).thenReturn("ADDRESS");
    when(errorMessagesConfig.getCUSTUMER_ALREADY_EXISTS()).thenReturn("Customer already exists");
  }

  @Test
  public void testCreateAccount_Success() {
    when(customerRepository.findByMobileNumber(customerRequest.getMobileNumber()))
        .thenReturn(Optional.empty());
    when(accountRepository.save(any(Account.class))).thenReturn(account);

    try (MockedStatic<GenerateAccountNumber> mockedStatic =
        mockStatic(GenerateAccountNumber.class)) {
      mockedStatic.when(GenerateAccountNumber::generateAccountNumber).thenReturn(1234567890);

      AccountDTO accountDTO = accountService.createAccount(customerRequest);

      assertEquals(account.getAccountNumber(), accountDTO.getAccountNumber());
      assertEquals(account.getAccountType(), accountDTO.getAccountType());
      assertEquals(account.getBranchAddress(), accountDTO.getBranchAddress());

      verify(customerRepository, times(2)).save(any(Customer.class));
      verify(accountRepository, times(1)).save(any(Account.class));
    }
  }

  @Test
  public void testCreateAccount_CustomerAlreadyExists() {
    when(customerRepository.findByMobileNumber(customerRequest.getMobileNumber()))
        .thenReturn(Optional.of(customer));

    CustomerAlreadyExistsException exception =
        assertThrows(
            CustomerAlreadyExistsException.class,
            () -> {
              accountService.createAccount(customerRequest);
            });

    assertEquals("Customer already exists", exception.getMessage());

    verify(customerRepository, never()).save(any(Customer.class));
    verify(accountRepository, never()).save(any(Account.class));
  }

  @Test
  public void testGetAccountDetails_Success() {
    when(customerRepository.findByMobileNumber(anyString())).thenReturn(Optional.of(customer));

    AccountWithCustomerDTO result = accountService.getAccountDetails("2711527572");

    AccountDTOWithoutResponse expectedAccountDTO =
        Mapper.fromAccountToAccountDTOWithoutResponse(account);
    CustomerDTOWithoutResponse expectedCustomerDTO =
        Mapper.fromCustomerToCustomerDTOWithoutResponse(customer);

    assertEquals(expectedAccountDTO, result.getAccount());
    assertEquals(expectedCustomerDTO, result.getCustomer());
  }

  @Test
  public void testGetAccountDetails_CustomerNotFound() {
    when(customerRepository.findByMobileNumber(anyString())).thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class,
        () -> {
          accountService.getAccountDetails("2711527572");
        });
  }

  @Test
  public void testUpdateAccount_Success() {
    AccountRequest accountRequest = new AccountRequest();
    accountRequest.setAccountNumber("1234567890");
    accountRequest.setAccountType("CURRENT");
    accountRequest.setBranchAddress("NEW_ADDRESS");
    accountRequest.setMobileNumber("0987654321");

    when(accountRepository.findByAccountNumber(Integer.parseInt(accountRequest.getAccountNumber())))
        .thenReturn(Optional.of(account));
    when(accountRepository.save(any(Account.class))).thenReturn(account);
    when(customerRepository.save(any(Customer.class))).thenReturn(customer);
    when(customerRepository.findByMobileNumber(accountRequest.getMobileNumber()))
        .thenReturn(Optional.of(customer));

    AccountWithCustomerDTO result = accountService.updateAccount(accountRequest);

    assertEquals(accountRequest.getAccountType(), result.getAccount().getAccountType());
    assertEquals(accountRequest.getBranchAddress(), result.getAccount().getBranchAddress());
    assertEquals(accountRequest.getMobileNumber(), result.getCustomer().getMobileNumber());

    verify(accountRepository, times(1)).save(any(Account.class));
    verify(customerRepository, times(1)).save(any(Customer.class));
  }

  @Test
  public void testUpdateAccount_AccountNotFound() {
    AccountRequest accountRequest = new AccountRequest();
    accountRequest.setAccountNumber("1234567890");

    when(accountRepository.findByAccountNumber(Integer.parseInt(accountRequest.getAccountNumber())))
        .thenReturn(Optional.empty());

    ResourceNotFoundException exception =
        assertThrows(
            ResourceNotFoundException.class,
            () -> {
              accountService.updateAccount(accountRequest);
            });

    assertEquals(
        "Account not found with the given input data accountNumber : '1234567890'",
        exception.getMessage());

    verify(accountRepository, never()).save(any(Account.class));
    verify(customerRepository, never()).save(any(Customer.class));
  }

  @Test
  public void testUpdateAccount_CustomerNotFound() {
    AccountRequest accountRequest = new AccountRequest();
    accountRequest.setAccountNumber("1234567890");
    accountRequest.setMobileNumber("0987654321");

    when(accountRepository.findByAccountNumber(Integer.parseInt(accountRequest.getAccountNumber())))
        .thenReturn(Optional.of(account));
    when(customerRepository.findByMobileNumber(accountRequest.getMobileNumber()))
        .thenReturn(Optional.empty());

    ResourceNotFoundException exception =
        assertThrows(
            ResourceNotFoundException.class,
            () -> {
              accountService.updateAccount(accountRequest);
            });

    assertEquals(
        "Customer not found with the given input data mobileNumber : '0987654321'",
        exception.getMessage());

    verify(accountRepository, never()).save(any(Account.class));
    verify(customerRepository, never()).save(any(Customer.class));
  }

  @Test
  public void testDeleteAccountAndCustomer_Success() {
    when(customerRepository.findByMobileNumber(customerRequest.getMobileNumber()))
        .thenReturn(Optional.of(customer));

    accountService.deleteAccountAndCustomer(customerRequest.getMobileNumber());

    verify(accountRepository, times(1)).delete(any(Account.class));
    verify(customerRepository, times(1)).delete(any(Customer.class));
  }

  @Test
  public void testDeleteAccountAndCustomer_CustomerNotFound() {
    when(customerRepository.findByMobileNumber(customerRequest.getMobileNumber()))
        .thenReturn(Optional.empty());

    ResourceNotFoundException exception =
        assertThrows(
            ResourceNotFoundException.class,
            () -> {
              accountService.deleteAccountAndCustomer(customerRequest.getMobileNumber());
            });

    assertEquals(
        "Customer not found with the given input data mobileNumber : '1234567890'",
        exception.getMessage());

    verify(accountRepository, never()).delete(any(Account.class));
    verify(customerRepository, never()).delete(any(Customer.class));
  }

  @Test
  public void testDeleteAccountAndCustomer_AccountNotFound() {
    customer.setAccount(null);
    when(customerRepository.findByMobileNumber(customerRequest.getMobileNumber()))
        .thenReturn(Optional.of(customer));

    ResourceNotFoundException exception =
        assertThrows(
            ResourceNotFoundException.class,
            () -> {
              accountService.deleteAccountAndCustomer(customerRequest.getMobileNumber());
            });

    assertEquals(
        "Account not found with the given input data mobileNumber : '1234567890'",
        exception.getMessage());

    verify(accountRepository, never()).delete(any(Account.class));
    verify(customerRepository, never()).delete(any(Customer.class));
  }
}
