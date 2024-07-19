package vascopanigi.MoneyZen.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vascopanigi.MoneyZen.entities.UserPlan;
import vascopanigi.MoneyZen.repositories.UserPlanRepository;

import java.util.List;

@Service
public class UserPlanService {
    @Autowired
    private UserPlanRepository userPlanRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void updatePlanStatuses() {
        List<UserPlan> activeUserPlans = userPlanRepository.findByIsValidTrue();
        activeUserPlans.forEach(userPlan -> {
            userPlan.checkAndDisableIfExpired();
            if (!userPlan.isValid()) {
                userPlanRepository.save(userPlan);
            }
        });
    }
}
