package vascopanigi.MoneyZen.payloads.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record ModifyUserDTO(@NotEmpty(message = "Username field is required")
                            @Size(min = 4, max = 20, message = "The username must be between 4 and 20 characters")
                            String username,
                            @NotEmpty(message = "Email field is required")
                            @Email
                            String email,
                            @NotEmpty(message = "name field is required")
                            @Size(min = 3, max = 20, message = "name must be between 2 and 20 characters")
                            String name,
                            @NotEmpty(message = "surname field is required")
                            @Size(min = 3, max = 20, message = "surname field is required")
                            String surname) {
}
