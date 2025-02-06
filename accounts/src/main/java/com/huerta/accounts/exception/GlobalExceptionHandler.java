package com.huerta.accounts.exception;

import com.huerta.accounts.dto.ErrorResponseDTO;
import com.huerta.accounts.dto.FieldErrorDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDTO> handleGlobalException(
      final Exception ex, WebRequest webRequest) {
    final ErrorResponseDTO errorResponseDTO =
        ErrorResponseDTO.builder()
            .apiPath(webRequest.getDescription(false))
            .errorCode(HttpStatus.INTERNAL_SERVER_ERROR)
            .errorMessage(ex.getMessage())
            .errorTime(LocalDateTime.now())
            .build();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDTO);
  }

  @ExceptionHandler(CustomerAlreadyExistsException.class)
  public ResponseEntity<ErrorResponseDTO> handleCustomerAlreadyExistsException(
      final CustomerAlreadyExistsException ex, WebRequest webRequest) {
    final ErrorResponseDTO errorResponseDTO =
        ErrorResponseDTO.builder()
            .apiPath(webRequest.getDescription(false))
            .errorCode(HttpStatus.CONFLICT)
            .errorMessage(ex.getMessage())
            .errorTime(LocalDateTime.now())
            .build();
    return ResponseEntity.badRequest().body(errorResponseDTO);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(
      final ResourceNotFoundException ex, WebRequest webRequest) {
    final ErrorResponseDTO errorResponseDTO =
        ErrorResponseDTO.builder()
            .apiPath(webRequest.getDescription(false))
            .errorCode(HttpStatus.NOT_FOUND)
            .errorMessage(ex.getMessage())
            .errorTime(LocalDateTime.now())
            .build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDTO);
  }

  @Override
  @Nullable
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      @NonNull MethodArgumentNotValidException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {
    List<FieldErrorDTO> fieldErrors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(
                error ->
                    FieldErrorDTO.builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .build())
            .collect(Collectors.toList());

    final ErrorResponseDTO errorResponseDTO =
        ErrorResponseDTO.builder()
            .apiPath(request.getDescription(false))
            .errorCode(HttpStatus.BAD_REQUEST)
            .errorMessage("Validation failed")
            .errorTime(LocalDateTime.now())
            .errors(fieldErrors)
            .build();

    return ResponseEntity.badRequest().body(errorResponseDTO);
  }
}
