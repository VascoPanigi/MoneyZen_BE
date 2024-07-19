package vascopanigi.MoneyZen.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "user_plan")
public class UserPlan {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
}
