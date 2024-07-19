package vascopanigi.MoneyZen.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vascopanigi.MoneyZen.entities.UserPlan;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserPlanRepository extends JpaRepository<UserPlan, UUID> {
    List<UserPlan> findByIsValidTrue();

}
