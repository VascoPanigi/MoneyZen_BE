package vascopanigi.MoneyZen.payloads.label;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewLabelDTO(
        @NotNull
                @Size(max = 15, message = "The size of your label must be 15 characters max.")
        String name) {
}
