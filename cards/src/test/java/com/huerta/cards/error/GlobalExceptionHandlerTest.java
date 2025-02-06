package com.huerta.cards.error;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.huerta.cards.exceptions.CardAlreadyExistsException;
import com.huerta.cards.exceptions.CardWithMobileNumberNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    when(webRequest.getDescription(false)).thenReturn("/api/test");
  }

  @Test
  public void testHandleGlobalException() {
    Exception exception = new Exception("Internal Server Error");

    ResponseEntity<ErrorResponseDTO> response =
        globalExceptionHandler.handleGlobalException(exception, webRequest);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals("/api/test", response.getBody().getApiPath());
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getBody().getErrorCode());
    assertEquals("Internal Server Error", response.getBody().getErrorMessage());
    assertEquals(
        LocalDateTime.now().getDayOfYear(), response.getBody().getErrorTime().getDayOfYear());
  }

  @Test
  public void testHandleCardAlreadyExistsException() {
    CardAlreadyExistsException exception = new CardAlreadyExistsException("Card already exists");

    ResponseEntity<ErrorResponseDTO> response =
        globalExceptionHandler.handleCardAlreadyExistsException(exception, webRequest);

    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    assertEquals("/api/test", response.getBody().getApiPath());
    assertEquals(HttpStatus.CONFLICT, response.getBody().getErrorCode());
    assertEquals("Card already exists", response.getBody().getErrorMessage());
    assertEquals(
        LocalDateTime.now().getDayOfYear(), response.getBody().getErrorTime().getDayOfYear());
  }

  @Test
  public void testHandleCardWithMobileNumberNotFoundException() {
    CardWithMobileNumberNotFoundException exception =
        new CardWithMobileNumberNotFoundException("Card not found");

    ResponseEntity<ErrorResponseDTO> response =
        globalExceptionHandler.handleCardWithMobileNumberNotFoundException(exception, webRequest);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("/api/test", response.getBody().getApiPath());
    assertEquals(HttpStatus.NOT_FOUND, response.getBody().getErrorCode());
    assertEquals(
        "Card with mobile number Card not found not found", response.getBody().getErrorMessage());
    assertEquals(
        LocalDateTime.now().getDayOfYear(), response.getBody().getErrorTime().getDayOfYear());
  }

  @Test
  public void testHandleMethodArgumentNotValid() {
    BindingResult bindingResult = mock(BindingResult.class);
    FieldError fieldError =
        new FieldError("objectName", "mobileNumber", "Mobile number must be 10 digits");
    when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

    MethodArgumentNotValidException exception =
        new MethodArgumentNotValidException(null, bindingResult);

    ResponseEntity<Object> response =
        globalExceptionHandler.handleMethodArgumentNotValid(
            exception, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    ErrorResponseDTO errorResponse = (ErrorResponseDTO) response.getBody();
    assertNotNull(errorResponse);
    assertEquals("/api/test", errorResponse.getApiPath());
    assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getErrorCode());
    assertEquals("Validation failed", errorResponse.getErrorMessage());
    assertEquals(LocalDateTime.now().getDayOfYear(), errorResponse.getErrorTime().getDayOfYear());
    assertEquals(1, errorResponse.getErrors().size());
    assertEquals("mobileNumber", errorResponse.getErrors().get(0).getField());
    assertEquals("Mobile number must be 10 digits", errorResponse.getErrors().get(0).getMessage());
  }

  /* @Test
  public void testHandleIllegalArgumentException() {
    IllegalArgumentException exception = new IllegalArgumentException("Invalid mobile number");

    ResponseEntity<ErrorResponseDTO> response =
        globalExceptionHandler.handleIllegalArgumentException(exception, webRequest);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("/api/test", response.getBody().getApiPath());
    assertEquals(HttpStatus.BAD_REQUEST, response.getBody().getErrorCode());
    assertEquals("Invalid mobile number", response.getBody().getErrorMessage());
    assertEquals(
        LocalDateTime.now().getDayOfYear(), response.getBody().getErrorTime().getDayOfYear());
  } */
}
