package vascopanigi.MoneyZen.payloads.transaction;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vascopanigi.MoneyZen.entities.Label;

import java.time.LocalDateTime;
import java.util.Set;

public record NewTransactionDTO (
        @NotNull
        @Size(min = 2, max=15, message = "Transaction name should be between 2 and 15 characters")
        String name,
        @NotNull
        double amount,
        @NotEmpty
        String transactionType,
        @NotEmpty
        String transactionRecurrence,
        String description,
        LocalDateTime date,
        Set<Label> labels
){
}
