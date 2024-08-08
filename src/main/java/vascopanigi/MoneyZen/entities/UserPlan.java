package vascopanigi.MoneyZen.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_plans")
public class UserPlan {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @ManyToOne
    @JsonIgnoreProperties("userPlans")    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JsonIgnoreProperties("userPlans")    @JoinColumn(name = "plan_id")
    private Plan plan;

    private LocalDateTime subscriptionTime;
    private LocalDateTime subscriptionEndingTime;
    private boolean isValid;

    public UserPlan(User user, Plan plan, LocalDateTime subscriptionTime, LocalDateTime subscriptionEndingTime) {
        this.user = user;
        this.plan = plan;
        this.subscriptionTime = subscriptionTime;
        this.subscriptionEndingTime = subscriptionEndingTime;
        this.isValid = true;
    }

    public void checkAndDisableIfExpired() {
        if (this.subscriptionEndingTime != null && LocalDateTime.now().isAfter(this.subscriptionEndingTime)) {
            this.isValid = false;
        }
    }

    @Override
    public String toString() {
        return "UserPlan{" +
                "isValid=" + isValid +
                ", subscriptionEndingTime=" + subscriptionEndingTime +
                ", subscriptionTime=" + subscriptionTime +
                '}';
    }
}
