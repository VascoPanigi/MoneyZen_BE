package vascopanigi.MoneyZen.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vascopanigi.MoneyZen.enums.plan.PlanDuration;
import vascopanigi.MoneyZen.enums.plan.PlanType;

import java.util.List;
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
    @Enumerated(EnumType.STRING)
    private PlanType planType;

//    @Setter(AccessLevel.NONE)
    private PlanDuration planDuration;
    
    private double planCost;
    public Plan(PlanType planType) {
        this.planType = planType;
    }

    @JsonIgnoreProperties("plan")
    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    private List<UserPlan> userPlans;

//    public void setPlanDuration(PlanDuration duration) {
//        this.planDuration = duration;
//        updatePlanCost();
//    }

    public void setPlanTypeAndDuration(PlanType type, PlanDuration planDuration) {
        this.planType = type;
        updatePlanFeatures(planDuration);
    }

    private double calculateAnnualCost(double monthlyCost, double discount){
        return (monthlyCost  * 12) * (1-discount);
    }

    private void updatePlanFeatures(PlanDuration planDuration) {
        this.planDuration = planDuration;

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

    @Override
    public String toString() {
        return "Plan{" +
                "allowPersonalWallet=" + allowPersonalWallet +
                ", allowSharedWallet=" + allowSharedWallet +
                ", allowAIAssistance=" + allowAIAssistance +
                ", maxPersonalWallets=" + maxPersonalWallets +
                ", maxSharedWallets=" + maxSharedWallets +
                ", planType=" + planType +
                ", planDuration=" + planDuration +
                ", planCost=" + planCost +
                '}';
    }
}
