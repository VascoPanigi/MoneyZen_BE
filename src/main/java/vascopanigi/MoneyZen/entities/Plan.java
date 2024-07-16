package vascopanigi.MoneyZen.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vascopanigi.MoneyZen.enums.plan.PlanDuration;
import vascopanigi.MoneyZen.enums.plan.PlanType;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "plans")
public class Plan {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    private boolean allowPersonalWallet;
    private boolean allowSharedWallet;
    private boolean allowAIAssistance;

    private int maxPersonalWallets;
    private int maxSharedWallets;

    @Setter(AccessLevel.NONE)
    private PlanType planType;

//    @Setter(AccessLevel.NONE)
    private PlanDuration planDuration;

    private double planCost;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    public void setPlanDuration(PlanDuration duration) {
//        this.planDuration = duration;
//        updatePlanCost();
//    }

    public void setPlanType(PlanType type) {
        this.planType = type;
        updatePlanFeatures();
    }

    private double calculateAnnualCost(double monthlyCost, double discount){
        return (monthlyCost  * 12) * (1-discount);
    }

    private void updatePlanFeatures() {
        switch (planType) {
            case FREE:
                this.planCost = 0.0;
                this.allowPersonalWallet = true;
                this.allowSharedWallet = false;
                this.allowAIAssistance = false;
                this.maxPersonalWallets = 1;
                this.maxSharedWallets = 0;
                break;
            case STANDARD:
                if (planDuration == PlanDuration.MONTHLY) {
                    this.planCost = 2.99;
                } else {
                    this.planCost = calculateAnnualCost(2.99, 0.20);
                }
                this.allowPersonalWallet = true;
                this.allowSharedWallet = true;
                this.allowAIAssistance = false;
                this.maxPersonalWallets = 5;
                this.maxSharedWallets = 2;
                break;
            case PREMIUM:
                if (planDuration == PlanDuration.MONTHLY) {
                    this.planCost = 5.99;
                } else {
                    this.planCost = calculateAnnualCost(5.99, 0.25);
                }
                this.allowPersonalWallet = true;
                this.allowSharedWallet = true;
                this.allowAIAssistance = true;
                this.maxPersonalWallets = 15;
                this.maxSharedWallets = 10;
                break;
            default:
                throw new IllegalArgumentException("Unknown plan type: " + planType);
        }
    }
}
