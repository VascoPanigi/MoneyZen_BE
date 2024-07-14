package vascopanigi.MoneyZen.payloads;

import java.time.LocalDateTime;

public record ErrorsDTO(String errorMessage, LocalDateTime errorTime) {
}
