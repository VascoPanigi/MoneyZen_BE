package vascopanigi.MoneyZen.payloads.user;

import java.util.UUID;

public record UserDTO(UUID id, String name, String surname, String username, String email, String avatarUR) {
}
