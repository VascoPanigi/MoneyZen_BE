package vascopanigi.MoneyZen.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    private String roleName;

    @JsonBackReference
    @ManyToMany(mappedBy = "rolesList")
    private List<User> userList;

    public Role(String roleName) {
        this.roleName = roleName;
    }
}
