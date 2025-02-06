package com.huerta.accounts.exception;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.huerta.accounts.dto.ErrorResponseDTO;
import com.huerta.accounts.dto.FieldErrorDTO;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

public class GlobalExceptionHandlerTest {

  private GlobalExceptionHandler globalExceptionHandler;
  private WebRequest webRequest;

  @BeforeEach
  public void setUp() {
    globalExceptionHandler = new GlobalExceptionHandler();
    webRequest = mock(WebRequest.class);
  }

  @Test
  public void testHandleCustomerAlreadyExistsException() {
    CustomerAlreadyExistsException exception =
        new CustomerAlreadyExistsException("Customer already exists");
    when(webRequest.getDescription(false)).thenReturn("/api/customers");

    ResponseEntity<ErrorResponseDTO> response =
        globalExceptionHandler.handleCustomerAlreadyExistsException(exception, webRequest);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("/api/customers", response.getBody().getApiPath());
    assertEquals(HttpStatus.CONFLICT, response.getBody().getErrorCode());
    assertEquals("Customer already exists", response.getBody().getErrorMessage());
    assertEquals(
        LocalDateTime.now().getDayOfYear(), response.getBody().getErrorTime().getDayOfYear());
  }

  @Test
  public void testHandleResourceNotFoundException() {
    ResourceNotFoundException exception = new ResourceNotFoundException("Resource", "ID", 123);
    when(webRequest.getDescription(false)).thenReturn("/api/resources");

    ResponseEntity<ErrorResponseDTO> response =
        globalExceptionHandler.handleResourceNotFoundException(exception, webRequest);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("/api/resources", response.getBody().getApiPath());
    assertEquals(HttpStatus.NOT_FOUND, response.getBody().getErrorCode());
    assertEquals(
        "Resource not found with the given input data ID : '123'",
        response.getBody().getErrorMessage());
    assertEquals(
        LocalDateTime.now().getDayOfYear(), response.getBody().getErrorTime().getDayOfYear());
  }

  @Test
  public void testHandleException() {
    Exception exception = new Exception("Internal server error");
    when(webRequest.getDescription(false)).thenReturn("/api/test");

    ResponseEntity<ErrorResponseDTO> response =
        globalExceptionHandler.handleGlobalException(exception, webRequest);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals("/api/test", response.getBody().getApiPath());
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getBody().getErrorCode());
    assertEquals("Internal server error", response.getBody().getErrorMessage());
    assertEquals(
        LocalDateTime.now().getDayOfYear(), response.getBody().getErrorTime().getDayOfYear());
  }

  @Test
  public void testHandleMethodArgumentNotValid() {
    MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
    BindingResult bindingResult = mock(BindingResult.class);
    FieldError fieldError = new FieldError("accountRequest", "name", "Name is required");

    when(exception.getBindingResult()).thenReturn(bindingResult);
    when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(fieldError));
    when(webRequest.getDescription(false)).thenReturn("/api/accounts");

    ResponseEntity<Object> response =
        globalExceptionHandler.handleMethodArgumentNotValid(
            exception, new HttpHeaders(), HttpStatusCode.valueOf(400), webRequest);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    ErrorResponseDTO responseBody = (ErrorResponseDTO) response.getBody();
    assertNotNull(responseBody);
    assertEquals("/api/accounts", responseBody.getApiPath());
    assertEquals(HttpStatus.BAD_REQUEST, responseBody.getErrorCode());
    assertEquals("Validation failed", responseBody.getErrorMessage());
    assertEquals(LocalDateTime.now().getDayOfYear(), responseBody.getErrorTime().getDayOfYear());

    FieldErrorDTO fieldErrorDTO = responseBody.getErrors().get(0);
    assertEquals("name", fieldErrorDTO.getField());
    assertEquals("Name is required", fieldErrorDTO.getMessage());
  }
}
