package vascopanigi.MoneyZen.payloads.user;

import jakarta.validation.constraints.NotEmpty;

public record UserLoginDTO(
        @NotEmpty
        String email,
        @NotEmpty
        String password) {
}
