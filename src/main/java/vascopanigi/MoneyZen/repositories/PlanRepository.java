package vascopanigi.MoneyZen.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vascopanigi.MoneyZen.entities.Plan;
import vascopanigi.MoneyZen.entities.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlanRepository extends JpaRepository<Plan, UUID> {
    Optional<Plan> findByPlanType(String planType);

}
