package com.huerta.accounts.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "CustomerRequest", description = "Schema to hold Customer information")
public class CustomerRequest {

  @Schema(description = "Name of the customer", example = "John Doe")
  @NotEmpty(message = "Name is required")
  @Size(min = 5, max = 30, message = "Name must be between 5 and 30 characters")
  private String name;

  @Schema(description = "Email of the customer", example = "John@example.com")
  @NotEmpty(message = "Email is required")
  @Email(message = "Email must be a valid email address")
  private String email;

  @Schema(description = "Mobile number of the customer", example = "1234567890")
  @NotEmpty(message = "Mobile number is required")
  @Pattern(regexp = "^$|[0-9]{10}", message = "Mobile number must be 10 digits")
  private String mobileNumber;
}
