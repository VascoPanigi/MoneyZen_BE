package vascopanigi.MoneyZen.payloads.wallet;

import java.util.UUID;

public interface WalletDTO {
    UUID getId();
    String getName();
    double getBalance();
}
