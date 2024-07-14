package vascopanigi.MoneyZen.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(UUID id) {
        super("Record with id: " + id + ", not found!");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
