package vascopanigi.MoneyZen.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "labels")
public class Label {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    private String name;

    private String avatarURL;

    public Label(String name, String avatarURL) {
        this.name = name;
        this.avatarURL = avatarURL;
    }
}
