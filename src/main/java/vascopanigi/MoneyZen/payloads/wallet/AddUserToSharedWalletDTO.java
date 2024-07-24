package vascopanigi.MoneyZen.payloads.wallet;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddUserToSharedWalletDTO (
        @NotNull
        UUID walletId
) {
}
