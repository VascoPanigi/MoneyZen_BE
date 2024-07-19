package vascopanigi.MoneyZen.payloads.wallet;

import java.util.UUID;

public record PersonalWalletDTO(UUID id, String name, double balance) implements WalletDTO{
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
