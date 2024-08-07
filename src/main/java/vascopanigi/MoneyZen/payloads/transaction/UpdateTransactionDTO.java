package vascopanigi.MoneyZen.payloads.transaction;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record UpdateTransactionDTO(@NotNull
                                   @Size(min = 2, max=25, message = "Transaction name should be between 2 and 15 characters")
                                   String name,
                                   @NotNull
                                   double amount,
                                   @NotNull
                                   String transactionRecurrence,
                                   String description,
                                   @NotNull
                                   String categoryName,
                                   @NotNull
                                   LocalDateTime date) {
}
