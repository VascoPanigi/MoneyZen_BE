package vascopanigi.MoneyZen.payloads.wallet;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewPersonalWalletDTO(
        @NotNull
        @Size(min = 2, max = 15, message = "Wallet name should be between 2 and 15 characters.")
        String name) {
}
