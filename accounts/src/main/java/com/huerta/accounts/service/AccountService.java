package com.huerta.accounts.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.huerta.accounts.config.messages.DataMessagesConfig;
import com.huerta.accounts.config.messages.ErrorMessagesConfig;
import com.huerta.accounts.dto.AccountDTO;
import com.huerta.accounts.dto.AccountWithCustomerDTO;
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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

  private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

  private final AccountRepository accountRepository;

  private final CustomerRepository customerRepository;

  private final DataMessagesConfig dataMessagesConfig;

  private final ErrorMessagesConfig errorMessagesConfig;

  /**
   * Create a new account with the given customer details.
   *
   * <p>If a customer with the same mobile number already exists, a {@link
   * CustomerAlreadyExistsException} will be thrown.
   *
   * @param customerRequest the customer details
   * @return the created account
   * @throws CustomerAlreadyExistsException if a customer with the same mobile number already exists
   */
  public AccountDTO createAccount(final CustomerRequest customerRequest) {
    logger.info(
        "Creating account for customer with mobile number: {}", customerRequest.getMobileNumber());
    Customer customer = new Customer(customerRequest);
    if (customerRepository.findByMobileNumber(customerRequest.getMobileNumber()).isPresent()) {
      logger.error(
          "Customer with mobile number {} already exists", customerRequest.getMobileNumber());
      throw new CustomerAlreadyExistsException(errorMessagesConfig.getCUSTUMER_ALREADY_EXISTS());
    }
    customerRepository.save(customer);
    final Account account = createAccount(customer);
    logger.info("Account ---: {}", account.toString());
    accountRepository.save(account);
    logger.info(
        "Account created successfully for customer with mobile number: {}",
        customerRequest.getMobileNumber());
    customer.setAccount(account);
    customerRepository.save(customer);
    return new AccountDTO(
        "", "", account.getAccountNumber(), account.getAccountType(), account.getBranchAddress());
  }

  /**
   * Creates a new account from the given customer.
   *
   * <p>The account number is a randomly generated integer between 0 and 9999999999. The account
   * type is "SAVINGS". The branch address is "ADDRESS".
   *
   * @param customer the customer to create the account for
   * @return the created account
   */
  private Account createAccount(final Customer customer) {
    logger.info(
        "Generating account number for customer with mobile number: {}",
        customer.getMobileNumber());
    Account account = new Account(customer);
    account.setAccountNumber(GenerateAccountNumber.generateAccountNumber());
    account.setAccountType(dataMessagesConfig.getSavings());
    account.setBranchAddress(dataMessagesConfig.getAddress());
    logger.info("Account ***: {}", account.toString());
    logger.info("Account number generated: {}", account.getAccountNumber());
    return account;
  }

  /**
   * Fetches the account details for the customer with the given mobile number.
   *
   * <p>If the customer is not found, a {@link ResourceNotFoundException} will be thrown.
   *
   * @param mobileNumber the mobile number of the customer
   * @return the account details
   * @throws ResourceNotFoundException if the customer is not found
   */
  public AccountWithCustomerDTO getAccountDetails(String mobileNumber) {
    logger.info("Fetching account details for customer with mobile number: {}", mobileNumber);
    return customerRepository
        .findByMobileNumber(mobileNumber)
        .or(
            () -> {
              logger.error("Customer with mobile number {} not found", mobileNumber);
              throw new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber);
            })
        .map(
            customer -> {
              AccountWithCustomerDTO accountWithCustomerDTO =
                  Mapper.fromCustomerToAccountWithCustomerDTO.apply(customer);
              logger.info(
                  "Account details fetched successfully for customer with mobile number: {}",
                  mobileNumber);
              return accountWithCustomerDTO;
            })
        .get();
  }

  public AccountWithCustomerDTO updateAccount(final AccountRequest accountRequest) {
    logger.info(
        "Received request to update account for customer with mobile number: {}",
        accountRequest.getMobileNumber());
    return accountRepository
        .findByAccountNumber(Integer.parseInt(accountRequest.getAccountNumber()))
        .or(
            () -> {
              logger.error(
                  "Account with account number {} not found", accountRequest.getAccountNumber());
              throw new ResourceNotFoundException(
                  "Account", "accountNumber", accountRequest.getAccountNumber());
            })
        .map(
            account -> {
              Optional<Customer> customerOptional =
                  customerRepository.findByMobileNumber(accountRequest.getMobileNumber());
              if (customerOptional.isEmpty()) {
                logger.error(
                    "Customer with mobile number {} not found", accountRequest.getMobileNumber());
                throw new ResourceNotFoundException(
                    "Customer", "mobileNumber", accountRequest.getMobileNumber());
              }
              updateAccountDetails(account, accountRequest);
              updateCustomerDetails(account.getCustomer(), accountRequest);
              logger.info(
                  "Account updated successfully for customer with mobile number: {}",
                  accountRequest.getMobileNumber());
              AccountWithCustomerDTO accountWithCustomerDTO =
                  Mapper.fromAccountToAccountWithCustomerDTO.apply(account);
              logger.info(
                  "Account details fetched successfully for customer with mobile number: {}",
                  accountRequest.getMobileNumber());
              return accountWithCustomerDTO;
            })
        .get();
  }

  private void updateAccountDetails(Account account, AccountRequest accountRequest) {
    account.setAccountType(accountRequest.getAccountType());
    account.setBranchAddress(accountRequest.getBranchAddress());
    accountRepository.save(account);
  }

  private void updateCustomerDetails(Customer customer, AccountRequest accountRequest) {
    customer.setName(accountRequest.getName());
    customer.setMobileNumber(accountRequest.getMobileNumber());
    customerRepository.save(customer);
  }

  public Void deleteAccountAndCustomer(String mobileNumber) {
    logger.info(
        "Received request to delete account and customer with mobile number: {}", mobileNumber);
    Customer customer =
        customerRepository
            .findByMobileNumber(mobileNumber)
            .orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
    if (customer.getAccount() != null) {
      accountRepository.delete(customer.getAccount());
    } else {
      logger.error("Account with mobile number {} not found", mobileNumber);
      throw new ResourceNotFoundException("Account", "mobileNumber", mobileNumber);
    }
    customerRepository.delete(customer);
    logger.info(
        "Account and customer deleted successfully for customer with mobile number: {}",
        mobileNumber);
    return null;
  }
}
