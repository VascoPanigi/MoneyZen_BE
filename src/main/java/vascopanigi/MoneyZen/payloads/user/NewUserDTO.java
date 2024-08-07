package vascopanigi.MoneyZen.payloads.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record NewUserDTO(
        @NotEmpty(message = "Username field is required")
                         @Size(min = 4, max = 20, message = "The username must be between 4 and 20 characters")
                         String username,
                         @NotEmpty(message = "Email field is required")
                         @Email
                         String email,
                         @NotEmpty(message = "Username field is required")
                         @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
                         String password,
                         @NotEmpty(message = "name field is required")
                         @Size(min = 3, max = 20, message = "name must be between 2 and 20 characters")
                         String name,
                         @NotEmpty(message = "surname field is required")
                         @Size(min = 3, max = 20, message = "surname field is required")
                         String surname
                         )
{
}
