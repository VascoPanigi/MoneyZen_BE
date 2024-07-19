package vascopanigi.MoneyZen.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import vascopanigi.MoneyZen.entities.Plan;
import vascopanigi.MoneyZen.enums.plan.PlanType;
import vascopanigi.MoneyZen.exceptions.BadRequestException;
import vascopanigi.MoneyZen.repositories.PlanRepository;

@Configuration
public class PlansInitializer {
    @Autowired
    private PlanRepository planRepository;

    @PostConstruct
    public void initializeRoles() {
        try {
            if (planRepository.findByPlanType(PlanType.FREE).isEmpty()) {
                planRepository.save(new Plan(PlanType.FREE));
            }if (planRepository.findByPlanType(PlanType.STANDARD).isEmpty()) {
                planRepository.save(new Plan(PlanType.STANDARD));
            }if (planRepository.findByPlanType(PlanType.PREMIUM).isEmpty()) {
                planRepository.save(new Plan(PlanType.PREMIUM));
            }
            System.out.println("Plans successfully initialized!");


        } catch (BadRequestException e) {
            System.out.println("Roles initialization failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error during database initialization: " + e.getMessage());
        }
    }
}
