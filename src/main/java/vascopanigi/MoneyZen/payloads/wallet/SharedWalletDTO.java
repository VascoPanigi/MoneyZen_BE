package vascopanigi.MoneyZen.payloads.wallet;

import vascopanigi.MoneyZen.payloads.user.UserDTO;

import java.util.Set;
import java.util.UUID;

public record SharedWalletDTO(UUID id, String name, double balance, Set<UserDTO> users) implements WalletDTO{
    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public double getBalance() {
        return this.balance;
    }
}
