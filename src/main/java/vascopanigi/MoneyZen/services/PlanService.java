package vascopanigi.MoneyZen.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vascopanigi.MoneyZen.entities.Plan;
import vascopanigi.MoneyZen.enums.plan.PlanType;
import vascopanigi.MoneyZen.exceptions.NotFoundException;
import vascopanigi.MoneyZen.repositories.PlanRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlanService {
    @Autowired
    private PlanRepository planRepository;

    public Plan findByPlanType(PlanType planType){
        return this.planRepository.findByPlanType(planType).orElseThrow(() -> new NotFoundException("Plan with name " + planType + " not found!"));
    }


    @Scheduled(cron = "0 0 0 * * ?")
    public void updatePlanStatuses() {
        List<Plan> plans = planRepository.findAll();
        plans.forEach(plan -> {
            if (plan.getSubscriptionEndingTime() != null && LocalDateTime.now().isAfter(plan.getSubscriptionEndingTime())) {
                plan.setIsValid(false);
                planRepository.save(plan);
            }
        });
    }
//    public Plan savePlan(NewPlanDTO body){
//
//    }
}
